package com.example.wallet.infra.eth;

import java.io.IOException;
import java.util.Map;

import com.example.wallet.domain.eth.EthTransferRequest;
import com.example.wallet.domain.eth.EthTransferResponse;
import com.example.wallet.domain.eth.GasFeeSuggestion;
import com.example.wallet.domain.eth.NonceResponse;
import com.example.wallet.domain.eth.TokenBalanceResponse;
import com.example.wallet.domain.eth.TokenTransferRequest;
import com.example.wallet.domain.eth.TokenTransferResponse;
import com.example.wallet.domain.eth.TransactionStatusResponse;

public interface IEthClient {
    String getBalance(String network, String address);
    TokenBalanceResponse getTokenBalance(String network, String contractAddress, String ownerAddress);
    NonceResponse getNonce(String network, String address);
    GasFeeSuggestion getGasFees(String network);
    GasFeeSuggestion getGasFeeSuggestion(String network);
    EthTransferResponse sendTransaction(String network, EthTransferRequest request);
    EthTransferResponse sendTransaction(String network, String privateKey, String toAddress, String amount, String gasPrice, String gasLimit);
    TokenTransferResponse sendTokenTransaction(String network, TokenTransferRequest request);
    TokenTransferResponse sendTokenTransaction(String network, String privateKey, String contractAddress, String toAddress, String amount, String gasPrice, String gasLimit);
    TransactionStatusResponse getTransactionStatus(String network, String txHash);
    /**
     * Get the chain ID for the specified Ethereum network
     * @param network The network name (e.g., "mainnet", "sepolia")
     * @return The chain ID as a long value
     * @throws IOException If there's an error communicating with the Ethereum node
     */
    long getChainId(String network) throws IOException;
}
