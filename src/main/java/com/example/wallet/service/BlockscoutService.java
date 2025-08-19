package com.example.wallet.service;

import com.example.wallet.infra.eth.BlockscoutProvider;
import org.springframework.stereotype.Service;

@Service
public class BlockscoutService {
    private final BlockscoutProvider blockscoutProvider;

    public BlockscoutService(BlockscoutProvider blockscoutProvider) {
        this.blockscoutProvider = blockscoutProvider;
    }

    public String getTransactions(String network, String address, String filter) {
        return blockscoutProvider.getTransactions(network, address, filter);
    }
}
