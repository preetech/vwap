package au.com.anz.vwap.service;

import au.com.anz.vwap.model.VWAPResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

@Slf4j
public class VWAPSink implements SinkFunction<VWAPResult> {
    @Override
    public void invoke(VWAPResult value, Context context) {
        log.info("VWAP for " + value.getCurrencyPair() + ": " + value.getVwap());
    }
}
