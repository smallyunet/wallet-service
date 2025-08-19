package com.example.wallet.domain.eth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the Ethereum gas fee suggestions
 */
public class GasFeeSuggestion {
    @JsonProperty("slow")
    private GasFeeDetail slow;
    
    @JsonProperty("average")
    private GasFeeDetail average;
    
    @JsonProperty("fast")
    private GasFeeDetail fast;
    
    @JsonProperty("fastest")
    private GasFeeDetail fastest;
    
    @JsonProperty("base_fee")
    private String baseFee;
    
    @JsonProperty("unit")
    private String unit = "gwei";
    
    // Nested class to represent fee details at each priority level
    public static class GasFeeDetail {
        @JsonProperty("max_fee")
        private String maxFee;
        
        @JsonProperty("max_priority_fee")
        private String maxPriorityFee;
        
        @JsonProperty("estimated_seconds")
        private Integer estimatedSeconds;
        
        public String getMaxFee() {
            return maxFee;
        }
        
        public void setMaxFee(String maxFee) {
            this.maxFee = maxFee;
        }
        
        public String getMaxPriorityFee() {
            return maxPriorityFee;
        }
        
        public void setMaxPriorityFee(String maxPriorityFee) {
            this.maxPriorityFee = maxPriorityFee;
        }
        
        public Integer getEstimatedSeconds() {
            return estimatedSeconds;
        }
        
        public void setEstimatedSeconds(Integer estimatedSeconds) {
            this.estimatedSeconds = estimatedSeconds;
        }
    }
    
    // Getters and Setters
    public GasFeeDetail getSlow() {
        return slow;
    }
    
    public void setSlow(GasFeeDetail slow) {
        this.slow = slow;
    }
    
    public GasFeeDetail getAverage() {
        return average;
    }
    
    public void setAverage(GasFeeDetail average) {
        this.average = average;
    }
    
    public GasFeeDetail getFast() {
        return fast;
    }
    
    public void setFast(GasFeeDetail fast) {
        this.fast = fast;
    }
    
    public GasFeeDetail getFastest() {
        return fastest;
    }
    
    public void setFastest(GasFeeDetail fastest) {
        this.fastest = fastest;
    }
    
    public String getBaseFee() {
        return baseFee;
    }
    
    public void setBaseFee(String baseFee) {
        this.baseFee = baseFee;
    }
    
    public String getUnit() {
        return unit;
    }
    
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
