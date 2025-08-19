package com.example.wallet.domain.blockscout;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents fee information in Blockscout API response
 */
public class Fee {
    private String type;
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
