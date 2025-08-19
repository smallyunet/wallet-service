package com.example.wallet.domain.eth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * Request object for ETH transfer
 * This accepts an already signed transaction from the client
 */
public class EthTransferRequest {
    
    @NotBlank(message = "Signed transaction data cannot be empty")
    @JsonProperty("signed_transaction")
    private String signedTransaction; // Hex string of the signed transaction
    
    @JsonProperty("from")
    private String from; // Optional, for information only
    
    @JsonProperty("to")
    private String to; // Optional, for information only
    
    @JsonProperty("value")
    private String value; // Optional, for information only
    
    public String getSignedTransaction() {
        return signedTransaction;
    }
    
    public void setSignedTransaction(String signedTransaction) {
        this.signedTransaction = signedTransaction;
    }
    
    public String getFrom() {
        return from;
    }
    
    public void setFrom(String from) {
        this.from = from;
    }
    
    public String getTo() {
        return to;
    }
    
    public void setTo(String to) {
        this.to = to;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
}
