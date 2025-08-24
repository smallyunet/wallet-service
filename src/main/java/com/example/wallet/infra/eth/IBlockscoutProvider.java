package com.example.wallet.infra.eth;

import com.example.wallet.domain.blockscout.BlockscoutTransactionResponse;
import com.example.wallet.domain.eth.BlockscoutTokenInfo;
import com.example.wallet.domain.eth.BlockscoutTokenListResponse;
import com.example.wallet.domain.eth.TokenTransferListResponse;

public interface IBlockscoutProvider {
    BlockscoutTokenListResponse getTokens(String network, String tokenSymbol, String type);
    BlockscoutTokenInfo getTokenByAddress(String network, String tokenAddress);
    TokenTransferListResponse getTokenTransfers(String network, String address, String tokenAddress, String type);
    BlockscoutTransactionResponse getTransactionsAsObject(String network, String address, String filter);
    String getTransactions(String network, String address, String filter);
}
