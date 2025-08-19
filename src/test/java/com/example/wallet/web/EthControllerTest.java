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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EthController.class)
public class EthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BalanceService balanceService;

    @MockBean
    private BlockscoutService blockscoutService;

    @Test
    public void testGetBalance() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String address = TestUtils.generateEthAddress();
        BalanceResponse mockResponse = new BalanceResponse("ETH", network, address, "0x1234567890");

        // Configure mock service behavior
        when(balanceService.ethBalance(eq(network), eq(address))).thenReturn(mockResponse);

        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/{address}/balance", network, address)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.chain").value("ETH"))
                .andExpect(jsonPath("$.network").value(network))
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.balance").value("0x1234567890"));
    }

    @Test
    public void testGetNonce() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String address = TestUtils.generateEthAddress();
        String nonce = "0x1";

        // Configure mock service behavior
        when(balanceService.getNonce(eq(network), eq(address))).thenReturn(new com.example.wallet.domain.eth.NonceResponse(network, address, nonce));

        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/{address}/nonce", network, address)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.network").value(network))
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.nonce").value(nonce));
    }

    @Test
    public void testGetGasFees() throws Exception {
        // Prepare test data
        String network = "mainnet";
        com.example.wallet.domain.eth.GasFeeSuggestion mockResponse = new com.example.wallet.domain.eth.GasFeeSuggestion();
        mockResponse.setBaseFee("20.0");
        mockResponse.setSlowFee("16.0");
        mockResponse.setStandardFee("20.0");
        mockResponse.setFastFee("24.0");
        mockResponse.setRapidFee("30.0");

        // Configure mock service behavior
        when(balanceService.getGasFeeSuggestion(eq(network))).thenReturn(mockResponse);

        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/gas-fees", network)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseFee").value("20.0"))
                .andExpect(jsonPath("$.slowFee").value("16.0"))
                .andExpect(jsonPath("$.standardFee").value("20.0"))
                .andExpect(jsonPath("$.fastFee").value("24.0"))
                .andExpect(jsonPath("$.rapidFee").value("30.0"));
    }

    @Test
    public void testConfigRpc() throws Exception {
        // Prepare test data
        String network = "mainnet";
        String rpcUrl = "https://eth-mainnet.mock";

        // Configure mock service behavior
        when(balanceService.effectiveEthRpc(eq(network))).thenReturn(rpcUrl);

        // Execute request and verify results
        mockMvc.perform(get("/v1/eth/{network}/config/rpc", network)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string(rpcUrl));
    }
}
