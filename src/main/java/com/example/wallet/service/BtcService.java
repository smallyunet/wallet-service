package com.example.wallet.service;

import com.example.wallet.config.IAppProperties;
import com.example.wallet.domain.btc.BitcoinAddressInfo;
import com.example.wallet.infra.btc.IBtcClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;

@Service
public class BtcService {
    
    private static final Logger logger = LoggerFactory.getLogger(BtcService.class);
    private final IAppProperties appProperties;
    private final IBtcClient btcClient;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public BtcService(IAppProperties appProperties, IBtcClient btcClient) {
        this.appProperties = appProperties;
        this.btcClient = btcClient;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Get detailed information about a Bitcoin address
     *
     * @param network Bitcoin network (mainnet, testnet)
     * @param address Bitcoin address to query
     * @return Address details including balance, transactions count, etc.
     */
    public BitcoinAddressInfo getAddressInfo(String network, String address) {
        try {
            String apiUrl = getApiEndpoint(network, "default");
            
            if (network.equals("testnet") || network.contains("esplora")) {
                // Blockstream and Mempool.space Esplora API format
                String endpoint = String.format("%s/address/%s", apiUrl, address);
                ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
                JsonNode responseJson = objectMapper.readTree(response.getBody());
                
                // Parse stats
                JsonNode chainStats = responseJson.get("chain_stats");
                BigInteger funded = new BigInteger(chainStats.get("funded_txo_sum").asText());
                BigInteger spent = new BigInteger(chainStats.get("spent_txo_sum").asText());
                BigInteger balance = funded.subtract(spent);
                int txCount = chainStats.get("tx_count").asInt();
                
                return new BitcoinAddressInfo(
                    address,
                    network,
                    balance.toString(),
                    txCount,
                    chainStats.get("funded_txo_count").asInt() - chainStats.get("spent_txo_count").asInt(),
                    funded.toString(),
                    spent.toString()
                );
            } else if (network.contains("blockcypher")) {
                // BlockCypher API format
                String endpoint = String.format("%s/addrs/%s", apiUrl, address);
                ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
                JsonNode responseJson = objectMapper.readTree(response.getBody());
                
                return new BitcoinAddressInfo(
                    address,
                    network,
                    responseJson.get("final_balance").asText(),
                    responseJson.get("n_tx").asInt(),
                    responseJson.get("unconfirmed_n_tx").asInt(),
                    responseJson.get("total_received").asText(),
                    responseJson.get("total_sent").asText()
                );
            } else {
                // Default Blockstream API format (mainnet or any other)
                String endpoint = String.format("%s/address/%s", apiUrl, address);
                ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
                JsonNode responseJson = objectMapper.readTree(response.getBody());
                
                // Parse stats
                JsonNode chainStats = responseJson.get("chain_stats");
                BigInteger funded = new BigInteger(chainStats.get("funded_txo_sum").asText());
                BigInteger spent = new BigInteger(chainStats.get("spent_txo_sum").asText());
                BigInteger balance = funded.subtract(spent);
                int txCount = chainStats.get("tx_count").asInt();
                
                return new BitcoinAddressInfo(
                    address,
                    network,
                    balance.toString(),
                    txCount,
                    chainStats.get("funded_txo_count").asInt() - chainStats.get("spent_txo_count").asInt(),
                    funded.toString(),
                    spent.toString()
                );
            }
        } catch (Exception e) {
            logger.error("Failed to fetch BTC address info for {}: {}", address, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch BTC address info: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get UTXOs for a Bitcoin address
     *
     * @param network Bitcoin network (mainnet, testnet)
     * @param address Bitcoin address to query
     * @return List of UTXOs as a JSON string
     */
    public String getUtxos(String network, String address) {
        return btcClient.getUtxos(network, address);
    }
    
    /**
     * Get API endpoint for a specific Bitcoin network and API type
     *
     * @param network Bitcoin network (mainnet, testnet)
     * @param apiType API type (default or specific provider)
     * @return API endpoint URL
     */
    public String getApiEndpoint(String network, String apiType) {
        String key;
        
        if ("default".equals(apiType)) {
            // Use the network name directly if it exists
            if (appProperties.getRpc().getBtc().containsKey(network)) {
                key = network;
            } else {
                // Default to testnet if asking for a testnet variant
                key = network.contains("test") ? "testnet" : "mainnet";
            }
        } else {
            // Try to find the API type by name
            key = apiType + "-" + network;
            if (!appProperties.getRpc().getBtc().containsKey(key)) {
                key = network; // Fallback to just the network name
            }
        }
        
        String apiUrl = appProperties.getRpc().getBtc().get(key);
        if (apiUrl == null) {
            throw new IllegalArgumentException("Unsupported BTC network or API type: " + network + "/" + apiType);
        }
        
        return apiUrl;
    }
}
