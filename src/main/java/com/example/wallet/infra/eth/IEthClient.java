package com.example.wallet.infra.eth;

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
}
