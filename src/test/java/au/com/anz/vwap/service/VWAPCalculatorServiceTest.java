package au.com.anz.vwap.service;

import au.com.anz.vwap.model.PriceData;
import au.com.anz.vwap.model.VWAPResult;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class VWAPCalculatorServiceTest {

    private VWAPCalculatorService vwapCalculatorService;
    private Collector<VWAPResult> collector;

    @BeforeEach
    public void setUp() {
        vwapCalculatorService = new VWAPCalculatorService();
        collector = mock(Collector.class);
    }

    @Test
    public void testApply_NormalCase() {
        PriceData data1 = new PriceData("USD/EUR", 1.1, 100,System.currentTimeMillis());
        PriceData data2 = new PriceData("USD/EUR", 1.2, 200,System.currentTimeMillis());
        Iterable<PriceData> input = Arrays.asList(data1, data2);
        TimeWindow window = new TimeWindow(0, 1000);

        vwapCalculatorService.apply("USD/EUR", window, input, collector);

        VWAPResult expectedResult = new VWAPResult("USD/EUR", 1.1666666666666667, 1000);
        verify(collector).collect(expectedResult);
    }

    @Test
    public void testApply_EmptyInput() {
        Iterable<PriceData> input = Collections.emptyList();
        TimeWindow window = new TimeWindow(0, 1000);

        vwapCalculatorService.apply("USD/EUR", window, input, collector);

        VWAPResult expectedResult = new VWAPResult("USD/EUR", 0.0, 1000);
        verify(collector).collect(expectedResult);
    }

    @Test
    public void testApply_ZeroVolume() {
        PriceData data1 = new PriceData("USD/EUR", 1.1, 0, System.currentTimeMillis());
        Iterable<PriceData> input = Collections.singletonList(data1);
        TimeWindow window = new TimeWindow(0, 1000);

        vwapCalculatorService.apply("USD/EUR", window, input, collector);

        VWAPResult expectedResult = new VWAPResult("USD/EUR", 0.0, 1000);
        verify(collector).collect(expectedResult);
    }
    @Test
    public void testApply_NegativePrice() {
        PriceData data1 = new PriceData("USD/EUR", -1.1, 100, System.currentTimeMillis());
        Iterable<PriceData> input = Collections.singletonList(data1);
        TimeWindow window = new TimeWindow(0, 1000);

        vwapCalculatorService.apply("USD/EUR", window, input, collector);

        VWAPResult expectedResult = new VWAPResult("USD/EUR", -1.1, 1000);
        verify(collector).collect(expectedResult);
    }
}