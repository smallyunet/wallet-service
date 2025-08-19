package com.example.wallet.domain.eth;

/**
 * Gas fee suggestions for Ethereum transactions
 */
public class GasFeeSuggestion {
    private String baseFee;     // Base fee (unit: Gwei)
    
    private String slowFee;     // Low priority fee (unit: Gwei) - Economic, may take longer to confirm
    private String standardFee; // Standard priority fee (unit: Gwei) - Balances speed and cost
    private String fastFee;     // High priority fee (unit: Gwei) - Faster confirmation
    private String rapidFee;    // Express priority fee (unit: Gwei) - Fastest confirmation

    public String getBaseFee() {
        return baseFee;
    }

    public void setBaseFee(String baseFee) {
        this.baseFee = baseFee;
    }

    public String getSlowFee() {
        return slowFee;
    }

    public void setSlowFee(String slowFee) {
        this.slowFee = slowFee;
    }

    public String getStandardFee() {
        return standardFee;
    }

    public void setStandardFee(String standardFee) {
        this.standardFee = standardFee;
    }

    public String getFastFee() {
        return fastFee;
    }

    public void setFastFee(String fastFee) {
        this.fastFee = fastFee;
    }

    public String getRapidFee() {
        return rapidFee;
    }

    public void setRapidFee(String rapidFee) {
        this.rapidFee = rapidFee;
    }
}
