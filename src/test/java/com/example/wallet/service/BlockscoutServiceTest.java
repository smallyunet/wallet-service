package com.example.wallet.service;

import com.example.wallet.domain.blockscout.BlockscoutTransactionResponse;
import com.example.wallet.domain.eth.BlockscoutTokenInfo;
import com.example.wallet.domain.eth.BlockscoutTokenListResponse;
import com.example.wallet.domain.eth.TokenTransferListResponse;
import com.example.wallet.infra.eth.BlockscoutProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BlockscoutServiceTest {

    @Mock
    private BlockscoutProvider blockscoutProvider;

    @InjectMocks
    private BlockscoutService blockscoutService;

    private final String NETWORK = "mainnet";
    private final String ADDRESS = "0x1234567890abcdef1234567890abcdef12345678";
    private final String FILTER = "to";
    private final String RAW_RESPONSE = "{ \"items\": [] }";

    @Test
    public void testGetTransactions() {
        // Configure mock behavior
        when(blockscoutProvider.getTransactions(eq(NETWORK), eq(ADDRESS), eq(FILTER)))
                .thenReturn(RAW_RESPONSE);

        // Call service method
        String result = blockscoutService.getTransactions(NETWORK, ADDRESS, FILTER);

        // Verify results
        assertEquals(RAW_RESPONSE, result);
        
        // Verify calls
        verify(blockscoutProvider).getTransactions(NETWORK, ADDRESS, FILTER);
    }

    @Test
    public void testGetTransactionsAsObject() {
        // Prepare mock response object
        BlockscoutTransactionResponse mockTxResponse = new BlockscoutTransactionResponse();
        
        // Configure mock behavior
        when(blockscoutProvider.getTransactionsAsObject(eq(NETWORK), eq(ADDRESS), eq(FILTER)))
                .thenReturn(mockTxResponse);
        
        // Call service method
        BlockscoutTransactionResponse txResult = blockscoutService.getTransactionsAsObject(NETWORK, ADDRESS, FILTER);
        
        // Verify results
        assertNotNull(txResult);
        
        // Verify calls
        verify(blockscoutProvider).getTransactionsAsObject(NETWORK, ADDRESS, FILTER);
    }
    
    @Test
    public void testGetTokens() {
        // Prepare test data
        String tokenSymbol = "USDT";
        String type = "ERC-20";
        BlockscoutTokenListResponse mockResponse = new BlockscoutTokenListResponse();
        
        // Configure mock behavior
        when(blockscoutProvider.getTokens(eq(NETWORK), eq(tokenSymbol), eq(type)))
                .thenReturn(mockResponse);
        
        // Call service method
        BlockscoutTokenListResponse result = blockscoutService.getTokens(NETWORK, tokenSymbol, type);
        
        // Verify results
        assertNotNull(result);
        
        // Verify calls
        verify(blockscoutProvider).getTokens(NETWORK, tokenSymbol, type);
    }
    
    @Test
    public void testGetTokenByAddress() {
        // Prepare test data
        String tokenAddress = "0x1234567890abcdef1234567890abcdef12345678";
        BlockscoutTokenInfo mockResponse = new BlockscoutTokenInfo();
        
        // Configure mock behavior
        when(blockscoutProvider.getTokenByAddress(eq(NETWORK), eq(tokenAddress)))
                .thenReturn(mockResponse);
        
        // Call service method
        BlockscoutTokenInfo result = blockscoutService.getTokenByAddress(NETWORK, tokenAddress);
        
        // Verify results
        assertNotNull(result);
        
        // Verify calls
        verify(blockscoutProvider).getTokenByAddress(NETWORK, tokenAddress);
    }
    
    @Test
    public void testGetTokenTransfers() {
        // Prepare test data
        String tokenAddress = "0x1234567890abcdef1234567890abcdef12345678";
        String type = "from";
        TokenTransferListResponse mockResponse = new TokenTransferListResponse();
        
        // Configure mock behavior
        when(blockscoutProvider.getTokenTransfers(eq(NETWORK), eq(ADDRESS), eq(tokenAddress), eq(type)))
                .thenReturn(mockResponse);
        
        // Call service method
        TokenTransferListResponse result = blockscoutService.getTokenTransfers(NETWORK, ADDRESS, tokenAddress, type);
        
        // Verify results
        assertNotNull(result);
        
        // Verify calls
        verify(blockscoutProvider).getTokenTransfers(NETWORK, ADDRESS, tokenAddress, type);
    }
}
