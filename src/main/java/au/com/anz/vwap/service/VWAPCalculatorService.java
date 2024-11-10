package au.com.anz.vwap.service;

import au.com.anz.vwap.model.PriceData;
import au.com.anz.vwap.model.VWAPResult;

import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.springframework.stereotype.Service;

@Service
public class VWAPCalculatorService implements WindowFunction<PriceData, VWAPResult, String, TimeWindow> {

    @Override
    public void apply(String currencyPair, TimeWindow window, Iterable<PriceData> input, Collector<VWAPResult> out) {
        double totalPriceVolume = 0.0;
        double totalVolume = 0.0;
        for (PriceData data : input) {
            totalPriceVolume += data.getPrice() * data.getVolume();
            totalVolume += data.getVolume();
        }

        double vwap = totalVolume == 0 ? 0 : totalPriceVolume / totalVolume;
        out.collect(new VWAPResult(currencyPair, vwap, window.getEnd()));
    }
}

