package au.com.anz.vwap.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class VWAPResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String currencyPair;
    private double vwap;
    private Long timestamp;

    public VWAPResult(String currencyPair, double vwap, long timestamp) {
        this.currencyPair = currencyPair;
        this.vwap = vwap;
        this.timestamp = timestamp;
    }
}
