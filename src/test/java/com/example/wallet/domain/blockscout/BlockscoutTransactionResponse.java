package com.example.wallet.domain.blockscout;

import java.util.List;

/**
 * Blockscout transaction response data model
 */
public class BlockscoutTransactionResponse {
    private List<Transaction> items;
    private int nextPageParams;
    
    public static class Transaction {
        private String blockNumber;
        private String from;
        private String to;
        private String hash;
        private String value;
        
        // Getters and setters
        public String getBlockNumber() { return blockNumber; }
        public void setBlockNumber(String blockNumber) { this.blockNumber = blockNumber; }
        public String getFrom() { return from; }
        public void setFrom(String from) { this.from = from; }
        public String getTo() { return to; }
        public void setTo(String to) { this.to = to; }
        public String getHash() { return hash; }
        public void setHash(String hash) { this.hash = hash; }
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }
    
    // Getters and setters
    public List<Transaction> getItems() { return items; }
    public void setItems(List<Transaction> items) { this.items = items; }
    public int getNextPageParams() { return nextPageParams; }
    public void setNextPageParams(int nextPageParams) { this.nextPageParams = nextPageParams; }
}
