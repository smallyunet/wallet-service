package com.example.wallet.infra.eth;

import com.example.wallet.config.AppProperties;
import com.example.wallet.domain.eth.EthTransferRequest;
import com.example.wallet.domain.eth.EthTransferResponse;
import com.example.wallet.domain.eth.GasFeeSuggestion;
import com.example.wallet.domain.eth.TransactionStatusResponse;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class EthClient {
    private static final Logger logger = LoggerFactory.getLogger(EthClient.class);
    private static final BigInteger DEFAULT_GAS_LIMIT = BigInteger.valueOf(21000); // Standard ETH transfer
    private final AppProperties appProperties;

    public EthClient(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public String getBalance(String network, String address) {
        String rpcUrl = appProperties.getRpc().getEth().get(network);
        if (rpcUrl == null) {
            throw new IllegalArgumentException("Unsupported ETH network: " + network);
        }
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            return "0x" + ethGetBalance.getBalance().toString(16);  // Convert to hex string manually
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch ETH balance", e);
        }
    }
    
    /**
     * Get gas fee suggestions for transactions
     * This method combines on-chain data with estimated confirmation times
     */
    public GasFeeSuggestion getGasFeeSuggestion(String network) {
        String rpcUrl = appProperties.getRpc().getEth().get(network);
        if (rpcUrl == null) {
            throw new IllegalArgumentException("Unsupported ETH network: " + network);
        }
        
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        try {
            // Get current gas price from the network
            EthGasPrice gasPrice = web3j.ethGasPrice().send();
            BigInteger currentGasPrice = gasPrice.getGasPrice();
            
            // Convert wei to gwei for better readability
            BigDecimal baseGasPriceGwei = Convert.fromWei(new BigDecimal(currentGasPrice), Convert.Unit.GWEI)
                                              .setScale(2, RoundingMode.HALF_UP);
            
            // Create gas fee suggestion object with different priority levels
            GasFeeSuggestion suggestion = new GasFeeSuggestion();
            
            // Set base fee
            suggestion.setBaseFee(baseGasPriceGwei.toString());
            
            // Set slow fee (80% of base)
            GasFeeSuggestion.GasFeeDetail slow = new GasFeeSuggestion.GasFeeDetail();
            slow.setMaxFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(1.0)).toString());
            slow.setMaxPriorityFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(0.1)).toString());
            slow.setEstimatedSeconds(120); // ~2 minutes
            suggestion.setSlow(slow);
            
            // Set average fee (100% of base)
            GasFeeSuggestion.GasFeeDetail average = new GasFeeSuggestion.GasFeeDetail();
            average.setMaxFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(1.2)).toString());
            average.setMaxPriorityFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(0.2)).toString());
            average.setEstimatedSeconds(60); // ~1 minute
            suggestion.setAverage(average);
            
            // Set fast fee (120% of base)
            GasFeeSuggestion.GasFeeDetail fast = new GasFeeSuggestion.GasFeeDetail();
            fast.setMaxFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(1.5)).toString());
            fast.setMaxPriorityFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(0.3)).toString());
            fast.setEstimatedSeconds(30); // ~30 seconds
            suggestion.setFast(fast);
            
            // Set fastest fee (150% of base)
            GasFeeSuggestion.GasFeeDetail fastest = new GasFeeSuggestion.GasFeeDetail();
            fastest.setMaxFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(2.0)).toString());
            fastest.setMaxPriorityFee(baseGasPriceGwei.multiply(BigDecimal.valueOf(0.5)).toString());
            fastest.setEstimatedSeconds(15); // ~15 seconds
            suggestion.setFastest(fastest);
            
            return suggestion;
            
        } catch (IOException e) {
            logger.error("Failed to fetch gas price", e);
            throw new RuntimeException("Failed to fetch gas price: " + e.getMessage(), e);
        }
    }
    
    /**
     * Send an already signed Ethereum transaction
     * This method accepts a transaction that was already signed by the client app
     * 
     * @param network the Ethereum network to use
     * @param request the transfer request with signed transaction data
     * @return response with transaction details
     */
    public EthTransferResponse sendTransaction(String network, EthTransferRequest request) {
        String rpcUrl = appProperties.getRpc().getEth().get(network);
        if (rpcUrl == null) {
            throw new IllegalArgumentException("Unsupported ETH network: " + network);
        }
        
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        EthTransferResponse response = new EthTransferResponse();
        
        try {
            // Get the signed transaction hex value from the request
            String hexValue = request.getSignedTransaction();
            
            // Validate the hex value
            if (!hexValue.startsWith("0x")) {
                hexValue = "0x" + hexValue;
            }
            
            // Store request metadata in response if available
            if (request.getFrom() != null) {
                response.setFrom(request.getFrom());
            }
            if (request.getTo() != null) {
                response.setTo(request.getTo());
            }
            if (request.getValue() != null) {
                response.setValue(request.getValue());
            }
            
            // Send transaction
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();
            
            if (ethSendTransaction.hasError()) {
                response.setStatus("failed");
                response.setError(ethSendTransaction.getError().getMessage());
                return response;
            }
            
            String transactionHash = ethSendTransaction.getTransactionHash();
            
            // Set transaction hash in response
            response.setTransactionHash(transactionHash);
            response.setStatus("pending");
            
            // Wait for transaction receipt if needed
            // Note: In production, this should be done asynchronously
            EthGetTransactionReceipt transactionReceipt = web3j
                .ethGetTransactionReceipt(transactionHash)
                .send();
            
            if (transactionReceipt.getTransactionReceipt().isPresent()) {
                TransactionReceipt receipt = transactionReceipt.getTransactionReceipt().get();
                
                // If transaction info not provided in request, get it from receipt
                if (response.getFrom() == null) {
                    response.setFrom(receipt.getFrom());
                }
                if (response.getTo() == null) {
                    response.setTo(receipt.getTo());
                }
                
                response.setBlockHash(receipt.getBlockHash());
                response.setBlockNumber(receipt.getBlockNumber().toString());
                response.setGasUsed(receipt.getGasUsed().toString());
                
                // Check transaction status (only for post-Byzantium)
                if (receipt.getStatus() != null) {
                    boolean success = receipt.isStatusOK();
                    response.setStatus(success ? "confirmed" : "failed");
                    if (!success) {
                        response.setError("Transaction execution failed");
                    }
                }
            }
            
            return response;
            
        } catch (IOException e) {
            logger.error("Failed to send transaction: {}", e.getMessage(), e);
            response.setStatus("failed");
            response.setError("Transaction failed: " + e.getMessage());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error: {}", e.getMessage(), e);
            response.setStatus("failed");
            response.setError("Unexpected error: " + e.getMessage());
            return response;
        }
    }
    
    /**
     * Get transaction status and receipt by transaction hash
     * 
     * @param network the Ethereum network to use
     * @param txHash the transaction hash
     * @return transaction status and receipt details
     */
    public TransactionStatusResponse getTransactionStatus(String network, String txHash) {
        String rpcUrl = appProperties.getRpc().getEth().get(network);
        if (rpcUrl == null) {
            throw new IllegalArgumentException("Unsupported ETH network: " + network);
        }
        
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        TransactionStatusResponse response = new TransactionStatusResponse();
        response.setTransactionHash(txHash);
        
        try {
            // First get transaction details
            EthTransaction ethTransaction = web3j.ethGetTransactionByHash(txHash).send();
            
            if (!ethTransaction.hasError() && ethTransaction.getTransaction().isPresent()) {
                org.web3j.protocol.core.methods.response.Transaction transaction = 
                    ethTransaction.getTransaction().get();
                
                // Set transaction details in response
                response.setFrom(transaction.getFrom());
                response.setTo(transaction.getTo());
                response.setValue(transaction.getValue().toString());
                response.setGasPrice(transaction.getGasPrice().toString());
                
                if (transaction.getBlockHash() != null && !transaction.getBlockHash().equals("0x0")) {
                    response.setBlockHash(transaction.getBlockHash());
                    response.setBlockNumber(transaction.getBlockNumber().toString());
                    
                    // Now get the receipt since the transaction is mined
                    EthGetTransactionReceipt receiptResponse = web3j.ethGetTransactionReceipt(txHash).send();
                    
                    if (!receiptResponse.hasError() && receiptResponse.getTransactionReceipt().isPresent()) {
                        TransactionReceipt receipt = receiptResponse.getTransactionReceipt().get();
                        
                        response.setBlockHash(receipt.getBlockHash());
                        response.setBlockNumber(receipt.getBlockNumber().toString());
                        response.setGasUsed(receipt.getGasUsed().toString());
                        response.setCumulativeGasUsed(receipt.getCumulativeGasUsed().toString());
                        
                        // For post-Byzantium transactions
                        if (receipt.getStatus() != null) {
                            boolean success = receipt.isStatusOK();
                            response.setStatus(success ? "success" : "failed");
                            if (!success) {
                                response.setError("Transaction execution failed");
                            }
                        }
                        
                        // If transaction created a contract
                        if (receipt.getContractAddress() != null) {
                            response.setContractAddress(receipt.getContractAddress());
                        }
                        
                        // Get effective gas price if available (EIP-1559)
                        if (receipt.getEffectiveGasPrice() != null) {
                            response.setEffectiveGasPrice(receipt.getEffectiveGasPrice().toString());
                        }
                        
                        // Get logs bloom
                        response.setLogsBloom(receipt.getLogsBloom());
                        
                        // Process logs
                        List<TransactionStatusResponse.TransactionLog> logs = new ArrayList<>();
                        for (Log log : receipt.getLogs()) {
                            TransactionStatusResponse.TransactionLog txLog = new TransactionStatusResponse.TransactionLog();
                            txLog.setAddress(log.getAddress());
                            txLog.setData(log.getData());
                            txLog.setBlockHash(log.getBlockHash());
                            txLog.setBlockNumber(log.getBlockNumber().toString());
                            txLog.setLogIndex(log.getLogIndex().toString());
                            txLog.setTransactionIndex(log.getTransactionIndex().toString());
                            txLog.setTopics(log.getTopics());
                            logs.add(txLog);
                        }
                        response.setLogs(logs);
                        
                        // Get confirmation count
                        EthBlockNumber currentBlock = web3j.ethBlockNumber().send();
                        BigInteger confirmations = currentBlock.getBlockNumber()
                                .subtract(receipt.getBlockNumber());
                        response.setConfirmationCount(confirmations.intValue());
                    }
                } else {
                    // Transaction is pending
                    response.setStatus("pending");
                }
            } else {
                // Transaction not found
                response.setStatus("not_found");
                response.setError("Transaction not found");
            }
            
            return response;
            
        } catch (IOException e) {
            logger.error("Failed to get transaction status: {}", e.getMessage(), e);
            response.setStatus("error");
            response.setError("Error fetching transaction: " + e.getMessage());
            return response;
        } catch (Exception e) {
            logger.error("Unexpected error getting transaction status: {}", e.getMessage(), e);
            response.setStatus("error");
            response.setError("Unexpected error: " + e.getMessage());
            return response;
        }
    }
}
