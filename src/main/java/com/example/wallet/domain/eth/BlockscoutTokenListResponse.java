package com.example.wallet.domain.eth;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BlockscoutTokenListResponse {
    @JsonProperty("items")
    private List<BlockscoutTokenInfo> items;

    public List<BlockscoutTokenInfo> getItems() {
        return items;
    }

    public void setItems(List<BlockscoutTokenInfo> items) {
        this.items = items;
    }
}
