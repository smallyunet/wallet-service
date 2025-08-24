package com.example.wallet.domain.eth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the total information for token transfers
 */
public class TransferTotal {
    @JsonProperty("decimals")
    private Integer decimals;
    
    @JsonProperty("value")
    private String value;

    // Getters and Setters
    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
