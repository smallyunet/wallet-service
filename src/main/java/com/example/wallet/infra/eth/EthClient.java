package com.example.wallet.infra.eth;

import com.example.wallet.config.IAppProperties;
import com.example.wallet.domain.eth.EthTransferRequest;
import com.example.wallet.domain.eth.EthTransferResponse;
import com.example.wallet.domain.eth.GasFeeSuggestion;
import com.example.wallet.domain.eth.NonceResponse;
import com.example.wallet.domain.eth.TokenBalanceResponse;
import com.example.wallet.domain.eth.TokenTransferRequest;
import com.example.wallet.domain.eth.TokenTransferResponse;
import com.example.wallet.domain.eth.TransactionStatusResponse;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
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
public class EthClient implements IEthClient {
    private static final Logger logger = LoggerFactory.getLogger(EthClient.class);
    private static final BigInteger DEFAULT_GAS_LIMIT = BigInteger.valueOf(21000); // Standard ETH transfer
    private final IAppProperties appProperties;

    public EthClient(IAppProperties appProperties) {
        this.appProperties = appProperties;
    }
    
    /**
     * Create a Web3j instance for the given RPC URL
     * This method is extracted for better testability
     * 
     * @param rpcUrl the RPC URL to connect to
     * @return a new Web3j instance
     */
    protected Web3j createWeb3j(String rpcUrl) {
        return Web3j.build(new HttpService(rpcUrl));
    }

    public String getBalance(String network, String address) {
        String rpcUrl = appProperties.getRpc().getEth().get(network);
        if (rpcUrl == null) {
            throw new IllegalArgumentException("Unsupported ETH network: " + network);
        }
        Web3j web3j = createWeb3j(rpcUrl);
        try {
            EthGetBalance ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
            return "0x" + ethGetBalance.getBalance().toString(16);  // Convert to hex string manually
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch ETH balance", e);
        }
    }
    
    /**
     * Get the current nonce for an address
     * 
     * @param network the Ethereum network
     * @param address the address to query
     * @return the current nonce as a hex string with 0x prefix
     */
    @Override
    public NonceResponse getNonce(String network, String address) {
        String rpcUrl = appProperties.getRpc().getEth().get(network);
        if (rpcUrl == null) {
            throw new IllegalArgumentException("Unsupported ETH network: " + network);
        }
        Web3j web3j = createWeb3j(rpcUrl);
        try {
            EthGetTransactionCount ethGetTransactionCount = web3j
                .ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
                .send();
            
            String nonce = "0x" + ethGetTransactionCount.getTransactionCount().toString(16);
            return new NonceResponse(network, address, nonce);
        } catch (IOException e) {
            logger.error("Failed to fetch nonce for address {}: {}", address, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch nonce: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get ERC-20 token balance for a specific address
     * 
     * @param network the Ethereum network
     * @param tokenAddress the token contract address
     * @param walletAddress the wallet address to check balance for
     * @return the token balance response with token details
     */
    public com.example.wallet.domain.eth.TokenBalanceResponse getTokenBalance(String network, String tokenAddress, String walletAddress) {
        String rpcUrl = appProperties.getRpc().getEth().get(network);
        if (rpcUrl == null) {
            throw new IllegalArgumentException("Unsupported ETH network: " + network);
        }
        
        Web3j web3j = createWeb3j(rpcUrl);
        try {
            // Get balance
            Function balanceFunction = new Function(
                "balanceOf", 
                List.of(new Address(walletAddress)), 
                List.of(new TypeReference<Uint256>() {})
            );
            String encodedBalanceFunction = FunctionEncoder.encode(balanceFunction);
            
            EthCall balanceCall = web3j.ethCall(
                Transaction.createEthCallTransaction(walletAddress, tokenAddress, encodedBalanceFunction),
                DefaultBlockParameterName.LATEST
            ).send();
            
            String balanceResult = balanceCall.getValue();
            List<Type> balanceDecoded = FunctionReturnDecoder.decode(balanceResult, balanceFunction.getOutputParameters());
            BigInteger balance = balanceDecoded.isEmpty() ? BigInteger.ZERO : (BigInteger) balanceDecoded.get(0).getValue();
            
            // Get decimals
            Function decimalsFunction = new Function(
                "decimals", 
                List.of(), 
                List.of(new TypeReference<Uint8>() {})
            );
            String encodedDecimalsFunction = FunctionEncoder.encode(decimalsFunction);
            
            EthCall decimalsCall = web3j.ethCall(
                Transaction.createEthCallTransaction(walletAddress, tokenAddress, encodedDecimalsFunction),
                DefaultBlockParameterName.LATEST
            ).send();
            
            String decimalsResult = decimalsCall.getValue();
            List<Type> decimalsDecoded = FunctionReturnDecoder.decode(decimalsResult, decimalsFunction.getOutputParameters());
            BigInteger decimals = decimalsDecoded.isEmpty() ? BigInteger.valueOf(18) : (BigInteger) decimalsDecoded.get(0).getValue();
            
            // Get symbol
            Function symbolFunction = new Function(
                "symbol", 
                List.of(), 
                List.of(new TypeReference<Utf8String>() {})
            );
            String encodedSymbolFunction = FunctionEncoder.encode(symbolFunction);
            
            EthCall symbolCall = web3j.ethCall(
                Transaction.createEthCallTransaction(walletAddress, tokenAddress, encodedSymbolFunction),
                DefaultBlockParameterName.LATEST
            ).send();
            
            String symbolResult = symbolCall.getValue();
            List<Type> symbolDecoded = FunctionReturnDecoder.decode(symbolResult, symbolFunction.getOutputParameters());
            String symbol = symbolDecoded.isEmpty() ? "" : (String) symbolDecoded.get(0).getValue();
            
            // Get name
            Function nameFunction = new Function(
                "name", 
                List.of(), 
                List.of(new TypeReference<Utf8String>() {})
            );
            String encodedNameFunction = FunctionEncoder.encode(nameFunction);
            
            EthCall nameCall = web3j.ethCall(
                Transaction.createEthCallTransaction(walletAddress, tokenAddress, encodedNameFunction),
                DefaultBlockParameterName.LATEST
            ).send();
            
            String nameResult = nameCall.getValue();
            List<Type> nameDecoded = FunctionReturnDecoder.decode(nameResult, nameFunction.getOutputParameters());
            String name = nameDecoded.isEmpty() ? "" : (String) nameDecoded.get(0).getValue();
            
            // Create response
            return new com.example.wallet.domain.eth.TokenBalanceResponse(
                tokenAddress, 
                network, 
                walletAddress, 
                "0x" + balance.toString(16), 
                symbol,
                name,
                decimals.toString()
            );
            
        } catch (IOException e) {
            logger.error("Failed to fetch token balance for token {} and address {}: {}", 
                         tokenAddress, walletAddress, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch token balance: " + e.getMessage(), e);
        }
    }
    
    @Override
    public GasFeeSuggestion getGasFees(String network) {
        return getGasFeeSuggestion(network);
    }

    /**
     * Get gas fee suggestions for transactions
     * This method combines on-chain data with estimated confirmation times
     */
    @Override
    public GasFeeSuggestion getGasFeeSuggestion(String network) {
        String rpcUrl = appProperties.getRpc().getEth().get(network);
        if (rpcUrl == null) {
            throw new IllegalArgumentException("Unsupported ETH network: " + network);
        }
        
        Web3j web3j = createWeb3j(rpcUrl);
        try {
            // Get current gas price from the network
            EthGasPrice gasPrice = web3j.ethGasPrice().send();
            BigInteger currentGasPrice = gasPrice.getGasPrice();
            
            // Log the raw gas price for debugging
            logger.debug("Raw gas price from {}: {} wei", network, currentGasPrice);
            
            // Check if gas price is below our minimum displayable precision (0.001 Gwei)
            if (currentGasPrice.equals(BigInteger.ZERO) || currentGasPrice.compareTo(BigInteger.valueOf(1_000_000L)) < 0) {
                // Gas price is either zero or too low to display with our precision
                logger.warn("Gas price from {} is too low ({} wei) for our precision, using minimum displayable value", network, currentGasPrice);
                // Use minimal value of 0.001 Gwei (1,000,000 wei) which is our minimum displayable precision
                currentGasPrice = BigInteger.valueOf(1_000_000L);
                logger.info("Using minimum gas price of {} Gwei", 
                    Convert.fromWei(new BigDecimal(currentGasPrice), Convert.Unit.GWEI));
            }
            
            // Convert wei to gwei for better readability
            // Use 4 decimal places to ensure we don't lose precision on low values
            // and maintain trailing zeros in the formatted output
            BigDecimal baseGasPriceGwei = Convert.fromWei(new BigDecimal(currentGasPrice), Convert.Unit.GWEI)
                                              .setScale(4, RoundingMode.HALF_UP);
            
            logger.debug("Calculated base gas price: {} gwei", baseGasPriceGwei);
            
            // Create gas fee suggestion object with different priority levels
            GasFeeSuggestion suggestion = new GasFeeSuggestion();
            
            // Set base fee - format to exactly match test expectations (0.0010 format)
            suggestion.setBaseFee(String.format("%.4f", baseGasPriceGwei));
            suggestion.setUnit("gwei"); // Explicitly set the unit
            
            // Set slow fee (100% of base)
            GasFeeSuggestion.GasFeeDetail slow = new GasFeeSuggestion.GasFeeDetail();
            slow.setMaxFee(String.format("%.4f", baseGasPriceGwei.multiply(BigDecimal.valueOf(1.0))));
            slow.setMaxPriorityFee(String.format("%.4f", baseGasPriceGwei.multiply(BigDecimal.valueOf(0.1))));
            slow.setEstimatedSeconds(120); // ~2 minutes
            suggestion.setSlow(slow);
            
            // Set average fee (120% of base)
            GasFeeSuggestion.GasFeeDetail average = new GasFeeSuggestion.GasFeeDetail();
            average.setMaxFee(String.format("%.4f", baseGasPriceGwei.multiply(BigDecimal.valueOf(1.2))));
            average.setMaxPriorityFee(String.format("%.4f", baseGasPriceGwei.multiply(BigDecimal.valueOf(0.2))));
            average.setEstimatedSeconds(60); // ~1 minute
            suggestion.setAverage(average);
            
            // Set fast fee (150% of base)
            GasFeeSuggestion.GasFeeDetail fast = new GasFeeSuggestion.GasFeeDetail();
            fast.setMaxFee(String.format("%.4f", baseGasPriceGwei.multiply(BigDecimal.valueOf(1.5))));
            fast.setMaxPriorityFee(String.format("%.4f", baseGasPriceGwei.multiply(BigDecimal.valueOf(0.3))));
            fast.setEstimatedSeconds(30); // ~30 seconds
            suggestion.setFast(fast);
            
            // Set fastest fee (200% of base)
            GasFeeSuggestion.GasFeeDetail fastest = new GasFeeSuggestion.GasFeeDetail();
            fastest.setMaxFee(String.format("%.4f", baseGasPriceGwei.multiply(BigDecimal.valueOf(2.0))));
            fastest.setMaxPriorityFee(String.format("%.4f", baseGasPriceGwei.multiply(BigDecimal.valueOf(0.5))));
            fastest.setEstimatedSeconds(15); // ~15 seconds
            suggestion.setFastest(fastest);
            
            // Log the final gas fee suggestion object for debugging
            logger.debug("Gas fee suggestion for {}: {}", network, suggestion);
            
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
        
        Web3j web3j = createWeb3j(rpcUrl);
        EthTransferResponse response = new EthTransferResponse();
        
        try {
            // Get the signed transaction hex value from the request
            String hexValue = request.getSignedTransaction();
            
            // Validate the hex value
            if (!hexValue.startsWith("0x")) {
                hexValue = "0x" + hexValue;
            }
            
            // Check if this is a transaction hash (32 bytes) instead of a signed transaction
            if (hexValue.length() == 66) { // 0x + 64 hex chars for 32 bytes
                response.setStatus("failed");
                response.setError("Provided value appears to be a transaction hash, not a signed transaction. A signed transaction is much longer and contains the complete transaction data.");
                return response;
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
    /**
     * Send an already signed token (ERC-20) transaction
     * This method accepts a transaction that was already signed by the client app
     * 
     * @param network the Ethereum network to use
     * @param request the token transfer request with signed transaction data
     * @return response with transaction details
     */
    public TokenTransferResponse sendTokenTransaction(String network, TokenTransferRequest request) {
        String rpcUrl = appProperties.getRpc().getEth().get(network);
        if (rpcUrl == null) {
            throw new IllegalArgumentException("Unsupported ETH network: " + network);
        }
        
        Web3j web3j = Web3j.build(new HttpService(rpcUrl));
        TokenTransferResponse response = new TokenTransferResponse();
        
        try {
            // Get the signed transaction hex value from the request
            String hexValue = request.getSignedTransaction();
            
            // Validate the hex value
            if (!hexValue.startsWith("0x")) {
                hexValue = "0x" + hexValue;
            }
            
            // Check if this is a transaction hash (32 bytes) instead of a signed transaction
            if (hexValue.length() == 66) { // 0x + 64 hex chars for 32 bytes
                response.setStatus("failed");
                response.setError("Provided value appears to be a transaction hash, not a signed transaction. A signed transaction is much longer and contains the complete transaction data.");
                return response;
            }
            
            // Store request metadata in response if available
            if (request.getFrom() != null) {
                response.setFrom(request.getFrom());
            }
            if (request.getTo() != null) {
                response.setTo(request.getTo());
            }
            if (request.getTokenAddress() != null) {
                response.setTokenAddress(request.getTokenAddress());
            }
            if (request.getValue() != null) {
                response.setValue(request.getValue());
            }
            if (request.getTokenSymbol() != null) {
                response.setTokenSymbol(request.getTokenSymbol());
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
            logger.error("Failed to send token transaction: {}", e.getMessage(), e);
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
    
    @Override
    public EthTransferResponse sendTransaction(String network, String privateKey, String toAddress, String amount, String gasPrice, String gasLimit) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }

    @Override
    public TokenTransferResponse sendTokenTransaction(String network, String privateKey, String contractAddress, String toAddress, String amount, String gasPrice, String gasLimit) {
        throw new UnsupportedOperationException("Method not implemented yet");
    }
    
    /**
     * Get the chain ID for the specified Ethereum network
     * 
     * @param network The network name (e.g., "mainnet", "sepolia")
     * @return The chain ID as a long value
     */
    @Override
    public long getChainId(String network) {
        try {
            // Get the RPC URL for the specified network
            String rpcUrl = appProperties.getRpc().getEth().get(network);
            if (rpcUrl == null) {
                logger.error("Network {} is not configured", network);
                throw new IllegalArgumentException("Network " + network + " is not configured");
            }
            
            // Create a Web3j instance for the RPC URL
            Web3j web3j = createWeb3j(rpcUrl);
            
            // Get the chain ID from the network
            EthChainId chainIdResponse = web3j.ethChainId().send();
            if (chainIdResponse.hasError()) {
                logger.error("Error fetching chain ID: {}", chainIdResponse.getError().getMessage());
                throw new IOException("Error fetching chain ID: " + chainIdResponse.getError().getMessage());
            }
            
            return chainIdResponse.getChainId().longValue();
        } catch (IOException e) {
            logger.error("Failed to get chain ID for network {}: {}", network, e.getMessage(), e);
            throw new RuntimeException("Failed to get chain ID for network " + network, e);
        }
    }
}
