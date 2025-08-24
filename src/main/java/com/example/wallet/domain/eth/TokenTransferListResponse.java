package com.example.wallet.domain.eth;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 代表代币转账记录的响应
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
