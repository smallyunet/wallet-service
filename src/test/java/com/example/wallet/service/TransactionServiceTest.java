package com.example.wallet.service;

import com.example.wallet.domain.eth.*;
import com.example.wallet.infra.eth.EthClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    private TransactionService transactionService;

    @Mock
    private EthClient ethClient;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        transactionService = new TransactionService(ethClient);
    }

    @Test
    public void testGetGasFeeSuggestion() {
        // Prepare test data
        String network = "mainnet";
        GasFeeSuggestion mockResponse = new GasFeeSuggestion();
        mockResponse.setBaseFee("20.0");
        
        // Set up details for slow
        GasFeeSuggestion.GasFeeDetail slow = new GasFeeSuggestion.GasFeeDetail();
        slow.setMaxFee("20.0");
        slow.setMaxPriorityFee("2.0");
        slow.setEstimatedSeconds(120);
        mockResponse.setSlow(slow);
        
        // Set up details for average
        GasFeeSuggestion.GasFeeDetail average = new GasFeeSuggestion.GasFeeDetail();
        average.setMaxFee("24.0");
        average.setMaxPriorityFee("4.0");
        average.setEstimatedSeconds(60);
        mockResponse.setAverage(average);
        
        // Configure mock behavior
        when(ethClient.getGasFeeSuggestion(eq(network))).thenReturn(mockResponse);

        // Execute test
        GasFeeSuggestion result = transactionService.getGasFeeSuggestion(network);

        // Verify results
        assertNotNull(result);
        assertEquals("20.0", result.getBaseFee());
        assertEquals("20.0", result.getSlow().getMaxFee());
        assertEquals("2.0", result.getSlow().getMaxPriorityFee());
        assertEquals(Integer.valueOf(120), result.getSlow().getEstimatedSeconds());
        assertEquals("24.0", result.getAverage().getMaxFee());

        // Verify interactions
        verify(ethClient, times(1)).getGasFeeSuggestion(eq(network));
    }

    @Test
    public void testSendEthTransaction() {
        // Prepare test data
        String network = "mainnet";
        EthTransferRequest request = new EthTransferRequest();
        request.setSignedTransaction("0x1234567890abcdef");
        request.setFrom("0xSenderAddress");
        request.setTo("0xRecipientAddress");
        request.setValue("0.1");

        EthTransferResponse mockResponse = new EthTransferResponse();
        mockResponse.setTransactionHash("0xTransactionHash");
        mockResponse.setFrom(request.getFrom());
        mockResponse.setTo(request.getTo());
        mockResponse.setValue(request.getValue());

        // Configure mock behavior
        when(ethClient.sendTransaction(eq(network), any(EthTransferRequest.class))).thenReturn(mockResponse);

        // Execute test
        EthTransferResponse result = transactionService.sendEthTransaction(network, request);

        // Verify results
        assertNotNull(result);
        assertEquals("0xTransactionHash", result.getTransactionHash());
        assertEquals(request.getFrom(), result.getFrom());
        assertEquals(request.getTo(), result.getTo());
        assertEquals(request.getValue(), result.getValue());

        // Verify interactions
        verify(ethClient, times(1)).sendTransaction(eq(network), eq(request));
    }

    @Test
    public void testSendTokenTransaction() {
        // Prepare test data
        String network = "mainnet";
        TokenTransferRequest request = new TokenTransferRequest();
        request.setSignedTransaction("0x1234567890abcdef");
        request.setFrom("0xSenderAddress");
        request.setTo("0xRecipientAddress");
        request.setTokenAddress("0xTokenContractAddress");
        request.setValue("100");
        
        TokenTransferResponse mockResponse = new TokenTransferResponse();
        mockResponse.setTransactionHash("0xTokenTransactionHash");
        mockResponse.setFrom(request.getFrom());
        mockResponse.setTo(request.getTo());
        mockResponse.setTokenAddress(request.getTokenAddress());
        mockResponse.setValue(request.getValue());

        // Configure mock behavior
        when(ethClient.sendTokenTransaction(eq(network), any(TokenTransferRequest.class))).thenReturn(mockResponse);

        // Execute test
        TokenTransferResponse result = transactionService.sendTokenTransaction(network, request);

        // Verify results
        assertNotNull(result);
        assertEquals("0xTokenTransactionHash", result.getTransactionHash());
        assertEquals(request.getFrom(), result.getFrom());
        assertEquals(request.getTo(), result.getTo());
        assertEquals(request.getTokenAddress(), result.getTokenAddress());
        assertEquals(request.getValue(), result.getValue());

        // Verify interactions
        verify(ethClient, times(1)).sendTokenTransaction(eq(network), eq(request));
    }

    @Test
    public void testGetTransactionStatus() {
        // Prepare test data
        String network = "mainnet";
        String txHash = "0x1234567890abcdef";
        
        TransactionStatusResponse mockResponse = new TransactionStatusResponse();
        mockResponse.setTransactionHash(txHash);
        mockResponse.setStatus("confirmed");
        mockResponse.setBlockNumber("123456");
        mockResponse.setFrom("0xSenderAddress");
        mockResponse.setTo("0xRecipientAddress");

        // Configure mock behavior
        when(ethClient.getTransactionStatus(eq(network), eq(txHash))).thenReturn(mockResponse);

        // Execute test
        TransactionStatusResponse result = transactionService.getTransactionStatus(network, txHash);

        // Verify results
        assertNotNull(result);
        assertEquals(txHash, result.getTransactionHash());
        assertEquals("confirmed", result.getStatus());
        assertEquals("123456", result.getBlockNumber());
        assertEquals("0xSenderAddress", result.getFrom());
        assertEquals("0xRecipientAddress", result.getTo());

        // Verify interactions
        verify(ethClient, times(1)).getTransactionStatus(eq(network), eq(txHash));
    }

    @Test
    public void testGetNonce() {
        // Prepare test data
        String network = "mainnet";
        String address = "0x1234567890abcdef";
        String nonceValue = "0x1";

        // Configure mock behavior
        when(ethClient.getNonce(eq(network), eq(address))).thenReturn(nonceValue);

        // Execute test
        NonceResponse result = transactionService.getNonce(network, address);

        // Verify results
        assertNotNull(result);
        assertEquals(network, result.getNetwork());
        assertEquals(address, result.getAddress());
        assertEquals(nonceValue, result.getNonce());

        // Verify interactions
        verify(ethClient, times(1)).getNonce(eq(network), eq(address));
    }
}
