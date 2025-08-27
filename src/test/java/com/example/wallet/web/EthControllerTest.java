package com.example.wallet.web;

import com.example.wallet.domain.eth.ChainIdResponse;
import com.example.wallet.service.BalanceService;
import com.example.wallet.service.BlockscoutService;
import com.example.wallet.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EthControllerTest {

    @Mock
    private TransactionService transactionService;
    
    @Mock
    private BalanceService balanceService;
    
    @Mock
    private BlockscoutService blockscoutService;

    @InjectMocks
    private EthController ethController;

    @Test
    public void testGetChainId() {
        // Setup
        String network = "sepolia";
        long expectedChainId = 11155111L;
        
        when(transactionService.getChainId(network)).thenReturn(expectedChainId);
        
        // Execute
        ResponseEntity<ChainIdResponse> response = ethController.getChainId(network);
        
        // Verify
        assertEquals(HttpStatus.OK, response.getStatusCode());
        
        ChainIdResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(expectedChainId, body.getChainid());
    }
}
