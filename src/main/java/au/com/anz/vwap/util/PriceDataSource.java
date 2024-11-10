package au.com.anz.vwap.util;

import au.com.anz.vwap.model.PriceData;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PriceDataSource implements SourceFunction<PriceData> {
    private boolean running = true;
    private Random rand = new Random();

    @Override
    public void run(SourceContext<PriceData> ctx) throws Exception {
        while (running) {
            // Simulate a stream of price data
            int randomNumber = rand.nextInt(3);
            List<String> currencyPairOptions = new ArrayList<>();
            currencyPairOptions.add("USD/EUR");
            currencyPairOptions.add("USD/JPY");
            currencyPairOptions.add("NZD/GBP");
            // Simulated currency pair
            double price = rand.nextDouble() * 100; // Random price between 0 and 100
            long timestamp = System.currentTimeMillis();
            long volume = rand.nextInt(1000); // Random volume

            // Emit data
            ctx.collect(new PriceData(currencyPairOptions.get(randomNumber), price, volume,timestamp));

            // Sleep to simulate real-time stream
            Thread.sleep(500);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}