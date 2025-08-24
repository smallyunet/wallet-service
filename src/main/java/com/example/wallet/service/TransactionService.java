package com.example.wallet.service;

import com.example.wallet.domain.eth.EthTransferRequest;
import com.example.wallet.domain.eth.EthTransferResponse;
import com.example.wallet.domain.eth.GasFeeSuggestion;
import com.example.wallet.domain.eth.NonceResponse;
import com.example.wallet.domain.eth.TokenTransferRequest;
import com.example.wallet.domain.eth.TokenTransferResponse;
import com.example.wallet.domain.eth.TransactionStatusResponse;
import com.example.wallet.infra.eth.EthClient;
import org.springframework.stereotype.Service;

/**
 * Service class for handling all transaction-related operations
 */
@Service
public class TransactionService {

    private final EthClient ethClient;

    public TransactionService(EthClient ethClient) {
        this.ethClient = ethClient;
    }
    
    /**
     * Get gas fee suggestions for Ethereum transactions
     */
    public GasFeeSuggestion getGasFeeSuggestion(String network) {
        return ethClient.getGasFeeSuggestion(network);
    }
    
    /**
     * Send an Ethereum transaction
     * Takes a signed transaction from client and broadcasts it to the network
     */
    public EthTransferResponse sendEthTransaction(String network, EthTransferRequest request) {
        return ethClient.sendTransaction(network, request);
    }
    
    /**
     * Send a token (ERC-20) transfer transaction
     * Takes a signed transaction from client and broadcasts it to the network
     * 
     * @param network the Ethereum network name
     * @param request the transfer request containing the signed transaction
     * @return response with transaction details
     */
    public TokenTransferResponse sendTokenTransaction(String network, TokenTransferRequest request) {
        return ethClient.sendTokenTransaction(network, request);
    }
    
    /**
     * Get transaction status and receipt by transaction hash
     *
     * @param network the Ethereum network name
     * @param txHash the transaction hash to query
     * @return detailed transaction status and receipt information
     */
    public TransactionStatusResponse getTransactionStatus(String network, String txHash) {
        return ethClient.getTransactionStatus(network, txHash);
    }
    
    /**
     * Get the current nonce for an Ethereum address
     *
     * @param network the Ethereum network name
     * @param address the address to query
     * @return the current nonce value
     */
    public NonceResponse getNonce(String network, String address) {
        String nonce = ethClient.getNonce(network, address);
        return new NonceResponse(network, address, nonce);
    }
}
