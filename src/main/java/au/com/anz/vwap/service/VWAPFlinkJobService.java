package au.com.anz.vwap.service;

import au.com.anz.vwap.model.PriceData;
import au.com.anz.vwap.util.PriceDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class VWAPFlinkJobService  implements CommandLineRunner {
    @Value("${flink.streamTimeCharacteristic}")
    private String streamTimeCharacteristic;

    @Value("${flink.restartAttempts}")
    private int restartAttempts;

    @Value("${flink.restartDelay}")
    private long restartDelay;

    @Value("${flink.checkpointInterval}")
    private long checkpointInterval;

    @Value("${flink.parallelism}")
    private int parallelism;

    @Value("${flink.bufferTimeout}")
    private long bufferTimeout;
    @Autowired
    protected VWAPCalculatorService vwapCalculatorService;

    @Override
    public void run(String... args) {

        startVWAPJob();
    }
    public void startVWAPJob() {
        try {
            StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
            env.setStreamTimeCharacteristic(TimeCharacteristic.valueOf(streamTimeCharacteristic));
            env.setRestartStrategy(RestartStrategies.fixedDelayRestart(restartAttempts, restartDelay));// Retry up to 3 times with a 10-second delay
            env.enableCheckpointing(checkpointInterval); // Checkpoint every 60 seconds
            env.setParallelism(parallelism); // Set parallelism to 4
            env.setBufferTimeout(bufferTimeout);// Set buffer timeout to 100 ms


            DataStream<PriceData> priceStream = env.addSource(new PriceDataSource())
                    .assignTimestampsAndWatermarks(
                            WatermarkStrategy.<PriceData>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                                    .withTimestampAssigner((event, timestamp) -> event.getTimestamp()) // Use the actual timestamp field from PriceData
                    ).map(priceData -> {
                        log.info("Currency Pair: " + priceData.getCurrencyPair() +
                                ", Timestamp: " + priceData.getTimestamp()+  ", Price: " +priceData.getPrice()+ ", Volume: " + priceData.getVolume());
                        return priceData;
                    });;


            priceStream
                    .keyBy(PriceData::getCurrencyPair)
                    .timeWindow(org.apache.flink.streaming.api.windowing.time.Time.seconds(10))  // 1-hour tumbling window
                    .apply(vwapCalculatorService)
                    .addSink(new VWAPSink());

            env.execute("VWAP Calculation Stream");
        } catch (Exception e) {
            log.error("Error in starting VWAP job", e);
            throw new RuntimeException(e);
        }
    }

}
