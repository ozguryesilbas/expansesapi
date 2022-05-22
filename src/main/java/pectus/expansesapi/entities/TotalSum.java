package pectus.expansesapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import pectus.expansesapi.constants.ExpansesContansts;

public class TotalSum {

    private String aggregatesBy;
    private Double totalSum;
    private String totalSumWithCurrency;

    public TotalSum(String aggregatesBy, Double totalSum) {
        this.aggregatesBy = aggregatesBy;
        this.totalSum = totalSum;
        this.setTotalSumWithCurrency(totalSum);
    }

    public String getAggregatesBy() {
        return aggregatesBy;
    }

    public void setAggregatesBy(String aggregatesBy) {
        this.aggregatesBy = aggregatesBy;
    }

    @JsonIgnore
    public Double getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Double totalSum) {
        this.totalSum = totalSum;
    }

    @JsonProperty("totalSum")
    public String getTotalSumWithCurrency() {
        return totalSumWithCurrency;
    }

    public void setTotalSumWithCurrency(Double totalSum) {
        this.totalSumWithCurrency = totalSum.toString() + ExpansesContansts.CURRENCYEXTENSIONONEDECIMAL;
    }

}
