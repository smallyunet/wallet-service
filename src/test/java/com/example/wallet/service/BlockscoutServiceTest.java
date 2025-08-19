package com.example.wallet.service;

import com.example.wallet.domain.blockscout.BlockscoutTransactionResponse;
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
        BlockscoutTransactionResponse mockResponse = new BlockscoutTransactionResponse();
        
        // Configure mock behavior
        when(blockscoutProvider.getTransactionsAsObject(eq(NETWORK), eq(ADDRESS), eq(FILTER)))
                .thenReturn(mockResponse);

        // Call service method
        BlockscoutTransactionResponse result = blockscoutService.getTransactionsAsObject(NETWORK, ADDRESS, FILTER);

        // Verify results
        assertNotNull(result);
        
        // Verify calls
        verify(blockscoutProvider).getTransactionsAsObject(NETWORK, ADDRESS, FILTER);
    }
}
