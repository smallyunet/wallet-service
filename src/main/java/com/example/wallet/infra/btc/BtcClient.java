package com.example.wallet.infra.btc;

import com.example.wallet.config.IAppProperties;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;

/**
 * Implementation of the Bitcoin client interface
 */
@Component
public class BtcClient implements IBtcClient {
    
    private static final Logger logger = LoggerFactory.getLogger(BtcClient.class);
    private final IAppProperties appProperties;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    public BtcClient(IAppProperties appProperties) {
        this.appProperties = appProperties;
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    @Override
    public String getBalance(String network, String address) {
        try {
            String apiUrl = appProperties.getRpc().getBtc().get(network);
            if (apiUrl == null) {
                throw new IllegalArgumentException("Unsupported BTC network: " + network);
            }
            
            if (network.equals("testnet") || network.contains("esplora")) {
                // Blockstream and Mempool.space Esplora API format
                String endpoint = String.format("%s/address/%s", apiUrl, address);
                ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
                JsonNode responseJson = objectMapper.readTree(response.getBody());
                
                // Get confirmed balance (chain_stats.funded_txo_sum - chain_stats.spent_txo_sum)
                JsonNode chainStats = responseJson.get("chain_stats");
                BigInteger funded = new BigInteger(chainStats.get("funded_txo_sum").asText());
                BigInteger spent = new BigInteger(chainStats.get("spent_txo_sum").asText());
                BigInteger balance = funded.subtract(spent);
                
                return balance.toString();
            } else if (network.contains("blockcypher")) {
                // BlockCypher API format
                String endpoint = String.format("%s/addrs/%s/balance", apiUrl, address);
                ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
                JsonNode responseJson = objectMapper.readTree(response.getBody());
                
                // Get confirmed balance in satoshis
                return responseJson.get("final_balance").asText();
            } else if (network.contains("blockchain")) {
                // Blockchain.info API format
                String endpoint = String.format("%s/rawaddr/%s", apiUrl, address);
                ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
                JsonNode responseJson = objectMapper.readTree(response.getBody());
                
                // Get confirmed balance in satoshis
                return responseJson.get("final_balance").asText();
            } else {
                // Default to mainnet Blockstream API
                String endpoint = String.format("%s/address/%s", apiUrl, address);
                ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
                JsonNode responseJson = objectMapper.readTree(response.getBody());
                
                // Get confirmed balance
                JsonNode chainStats = responseJson.get("chain_stats");
                BigInteger funded = new BigInteger(chainStats.get("funded_txo_sum").asText());
                BigInteger spent = new BigInteger(chainStats.get("spent_txo_sum").asText());
                BigInteger balance = funded.subtract(spent);
                
                return balance.toString();
            }
        } catch (Exception e) {
            logger.error("Failed to fetch BTC balance for address {}: {}", address, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch BTC balance: " + e.getMessage(), e);
        }
    }
    
    @Override
    public String getTransactionStatus(String network, String txHash) {
        // TODO: Implement actual transaction status logic
        return "{}";
    }
    
    @Override
    public String getUtxos(String network, String address) {
        try {
            String apiUrl = appProperties.getRpc().getBtc().get(network);
            if (apiUrl == null) {
                throw new IllegalArgumentException("Unsupported BTC network: " + network);
            }
            
            if (network.equals("testnet") || network.contains("esplora")) {
                // Blockstream and Mempool.space Esplora API format
                String endpoint = String.format("%s/address/%s/utxo", apiUrl, address);
                ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
                return response.getBody();
            } else if (network.contains("blockcypher")) {
                // BlockCypher API format
                String endpoint = String.format("%s/addrs/%s?unspentOnly=true", apiUrl, address);
                ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
                return response.getBody();
            } else {
                // Default to mainnet Blockstream API
                String endpoint = String.format("%s/address/%s/utxo", apiUrl, address);
                ResponseEntity<String> response = restTemplate.getForEntity(endpoint, String.class);
                return response.getBody();
            }
        } catch (Exception e) {
            logger.error("Failed to fetch BTC UTXOs for address {}: {}", address, e.getMessage(), e);
            throw new RuntimeException("Failed to fetch BTC UTXOs: " + e.getMessage(), e);
        }
    }
}
