package com.example.wallet.domain.eth;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response object for transaction receipt query
 */
public class TransactionStatusResponse {
    
    @JsonProperty("transaction_hash")
    private String transactionHash;
    
    @JsonProperty("block_hash")
    private String blockHash;
    
    @JsonProperty("block_number")
    private String blockNumber;
    
    @JsonProperty("from")
    private String from;
    
    @JsonProperty("to")
    private String to;
    
    @JsonProperty("gas_used")
    private String gasUsed;
    
    @JsonProperty("gas_price")
    private String gasPrice;
    
    @JsonProperty("effective_gas_price")
    private String effectiveGasPrice;
    
    @JsonProperty("value")
    private String value;
    
    @JsonProperty("status")
    private String status; // "success", "failed", "pending"
    
    @JsonProperty("cumulative_gas_used")
    private String cumulativeGasUsed;
    
    @JsonProperty("logs_bloom")
    private String logsBloom;
    
    @JsonProperty("contract_address")
    private String contractAddress;
    
    @JsonProperty("logs")
    private List<TransactionLog> logs;
    
    @JsonProperty("confirmation_count")
    private Integer confirmationCount;
    
    @JsonProperty("error")
    private String error;
    
    // Nested class for transaction logs
    public static class TransactionLog {
        @JsonProperty("address")
        private String address;
        
        @JsonProperty("topics")
        private List<String> topics;
        
        @JsonProperty("data")
        private String data;
        
        @JsonProperty("log_index")
        private String logIndex;
        
        @JsonProperty("block_number")
        private String blockNumber;
        
        @JsonProperty("block_hash") 
        private String blockHash;
        
        @JsonProperty("transaction_index")
        private String transactionIndex;

        // Getters and Setters
        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public List<String> getTopics() {
            return topics;
        }

        public void setTopics(List<String> topics) {
            this.topics = topics;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getLogIndex() {
            return logIndex;
        }

        public void setLogIndex(String logIndex) {
            this.logIndex = logIndex;
        }

        public String getBlockNumber() {
            return blockNumber;
        }

        public void setBlockNumber(String blockNumber) {
            this.blockNumber = blockNumber;
        }

        public String getBlockHash() {
            return blockHash;
        }

        public void setBlockHash(String blockHash) {
            this.blockHash = blockHash;
        }

        public String getTransactionIndex() {
            return transactionIndex;
        }

        public void setTransactionIndex(String transactionIndex) {
            this.transactionIndex = transactionIndex;
        }
    }
    
    // Getters and setters
    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
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

    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getEffectiveGasPrice() {
        return effectiveGasPrice;
    }

    public void setEffectiveGasPrice(String effectiveGasPrice) {
        this.effectiveGasPrice = effectiveGasPrice;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCumulativeGasUsed() {
        return cumulativeGasUsed;
    }

    public void setCumulativeGasUsed(String cumulativeGasUsed) {
        this.cumulativeGasUsed = cumulativeGasUsed;
    }

    public String getLogsBloom() {
        return logsBloom;
    }

    public void setLogsBloom(String logsBloom) {
        this.logsBloom = logsBloom;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public List<TransactionLog> getLogs() {
        return logs;
    }

    public void setLogs(List<TransactionLog> logs) {
        this.logs = logs;
    }

    public Integer getConfirmationCount() {
        return confirmationCount;
    }

    public void setConfirmationCount(Integer confirmationCount) {
        this.confirmationCount = confirmationCount;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
