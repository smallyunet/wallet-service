package com.example.wallet.domain.eth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Response object for ERC-20 token transfer
 * Contains transaction details and status
 */
public class TokenTransferResponse {

    @JsonProperty("transaction_hash")
    private String transactionHash;
    
    @JsonProperty("status")
    private String status; // pending, confirmed, failed
    
    @JsonProperty("from")
    private String from;
    
    @JsonProperty("to")
    private String to;
    
    @JsonProperty("token_address")
    private String tokenAddress;
    
    @JsonProperty("value")
    private String value;
    
    @JsonProperty("token_symbol")
    private String tokenSymbol;
    
    @JsonProperty("error")
    private String error;
    
    @JsonProperty("block_hash")
    private String blockHash;
    
    @JsonProperty("block_number")
    private String blockNumber;
    
    @JsonProperty("gas_used")
    private String gasUsed;

    // Getters and Setters
    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(String blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }
}
