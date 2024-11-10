package au.com.anz.vwap.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PriceData {
    private String currencyPair;
    private double price;
    private double volume;
    private long timestamp;
}

