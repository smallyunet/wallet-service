package com.example.wallet.domain.blockscout;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Response object for Blockscout transaction API
 */
public class BlockscoutTransactionResponse {
    private List<Transaction> items;
    
    @JsonProperty("next_page_params")
    private Object nextPageParams;

    public List<Transaction> getItems() {
        return items;
    }

    public void setItems(List<Transaction> items) {
        this.items = items;
    }

    public Object getNextPageParams() {
        return nextPageParams;
    }

    public void setNextPageParams(Object nextPageParams) {
        this.nextPageParams = nextPageParams;
    }
}
