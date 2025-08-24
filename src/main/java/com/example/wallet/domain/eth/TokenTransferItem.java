package com.example.wallet.domain.eth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 代表单个代币转账记录
 */
public class TokenTransferItem {
    @JsonProperty("block_hash")
    private String blockHash;
    
    @JsonProperty("block_number")
    private long blockNumber;
    
    @JsonProperty("from")
    private AddressInfo from;
    
    @JsonProperty("log_index")
    private int logIndex;
    
    private String method;
    
    private String timestamp;
    
    @JsonProperty("to")
    private AddressInfo to;
    
    private TokenInfo token;
    
    private TransferTotal total;
    
    @JsonProperty("transaction_hash")
    private String transactionHash;
    
    private String type;

    // Getters and Setters
    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public long getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(long blockNumber) {
        this.blockNumber = blockNumber;
    }

    public AddressInfo getFrom() {
        return from;
    }

    public void setFrom(AddressInfo from) {
        this.from = from;
    }

    public int getLogIndex() {
        return logIndex;
    }

    public void setLogIndex(int logIndex) {
        this.logIndex = logIndex;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public AddressInfo getTo() {
        return to;
    }

    public void setTo(AddressInfo to) {
        this.to = to;
    }

    public TokenInfo getToken() {
        return token;
    }

    public void setToken(TokenInfo token) {
        this.token = token;
    }

    public TransferTotal getTotal() {
        return total;
    }

    public void setTotal(TransferTotal total) {
        this.total = total;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
