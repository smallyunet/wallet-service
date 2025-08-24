package com.example.wallet.domain.eth;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the response of token transfer records
 */
public class TokenTransferListResponse {
    @JsonProperty("items")
    private List<TokenTransferItem> items;

    public List<TokenTransferItem> getItems() {
        return items;
    }

    public void setItems(List<TokenTransferItem> items) {
        this.items = items;
    }
}
