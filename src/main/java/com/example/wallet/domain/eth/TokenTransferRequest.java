package com.example.wallet.domain.eth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

/**
 * Request object for ERC-20 token transfer
 * This accepts an already signed transaction from the client for token transfer
 */
public class TokenTransferRequest {
    
    @NotBlank(message = "Signed transaction data cannot be empty")
    @JsonProperty("signed_transaction")
    private String signedTransaction; // Hex string of the signed transaction
    
    @JsonProperty("from")
    private String from; // Optional, for information only
    
    @JsonProperty("to")
    private String to; // Optional, for information only
    
    @JsonProperty("token_address")
    private String tokenAddress; // Token contract address
    
    @JsonProperty("value")
    private String value; // Optional, for information only - token amount
    
    @JsonProperty("token_symbol")
    private String tokenSymbol; // Optional, for information only

    // Getters and Setters
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
    
    public String getTokenAddress() {
        return tokenAddress;
    }
    
    public void setTokenAddress(String tokenAddress) {
        this.tokenAddress = tokenAddress;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getTokenSymbol() {
        return tokenSymbol;
    }
    
    public void setTokenSymbol(String tokenSymbol) {
        this.tokenSymbol = tokenSymbol;
    }
}
