package com.example.wallet.web;

import com.example.wallet.domain.BalanceResponse;
import com.example.wallet.service.BalanceService;
import com.example.wallet.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BtcController.class)
public class BtcControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BalanceService balanceService;

    @Test
    public void testGetBalance() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String address = TestUtils.generateBtcAddress();
        BalanceResponse mockResponse = new BalanceResponse("BTC", network, address, "123456789");

        // Configure mock service behavior
        when(balanceService.btcBalance(eq(network), eq(address))).thenReturn(mockResponse);

        // Execute request and verify results
        mockMvc.perform(get("/v1/btc/{network}/{address}/balance", network, address)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chain").value("BTC"))
                .andExpect(jsonPath("$.network").value(network))
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.balance").value("123456789"));
    }

    @Test
    public void testConfigApi() throws Exception {
        // Prepare test data
        Object mockConfig = new Object() {
            public final String mainnet = "https://blockstream.mock/api";
            public final String testnet = "https://blockstream.mock/testnet/api";
        };

        // Configure mock service behavior
        when(balanceService.effectiveBtcRpc()).thenReturn(mockConfig);

        // Execute request and verify results
        mockMvc.perform(get("/v1/btc/mainnet/config/api")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
