package com.example.wallet.web;

import com.example.wallet.domain.BalanceResponse;
import com.example.wallet.domain.btc.BitcoinAddressInfo;
import com.example.wallet.service.BalanceService;
import com.example.wallet.service.BtcService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/btc/{network}")
@Validated
public class BtcController {

    private final BalanceService balanceService;
    private final BtcService btcService;

    public BtcController(BalanceService balanceService, BtcService btcService) {
        this.balanceService = balanceService;
        this.btcService = btcService;
    }

    /**
     * Get balance for a Bitcoin address
     * 
     * @param network Bitcoin network (mainnet, testnet)
     * @param address Bitcoin address to query
     * @return Balance information
     */
    @GetMapping("/{address}/balance")
    public ResponseEntity<BalanceResponse> getBalance(
            @PathVariable String network,
            @PathVariable @NotBlank String address) {
        return ResponseEntity.ok(balanceService.btcBalance(network, address));
    }

    /**
     * Get detailed information about a Bitcoin address
     * 
     * @param network Bitcoin network (mainnet, testnet)
     * @param address Bitcoin address to query
     * @return Address details including balance, transactions count, etc.
     */
    @GetMapping("/{address}")
    public ResponseEntity<BitcoinAddressInfo> getAddressInfo(
            @PathVariable String network,
            @PathVariable @NotBlank String address) {
        return ResponseEntity.ok(btcService.getAddressInfo(network, address));
    }

    /**
     * Get UTXOs for a Bitcoin address
     * 
     * @param network Bitcoin network (mainnet, testnet)
     * @param address Bitcoin address to query
     * @return List of UTXOs
     */
    @GetMapping("/{address}/utxos")
    public ResponseEntity<String> getUtxos(
            @PathVariable String network,
            @PathVariable @NotBlank String address) {
        return ResponseEntity.ok(btcService.getUtxos(network, address));
    }

    /**
     * Get configuration for all available Bitcoin APIs
     * 
     * @return Map of network names to API endpoints
     */
    @GetMapping("/config/api")
    public ResponseEntity<Object> api() {
        return ResponseEntity.ok(balanceService.effectiveBtcRpc());
    }
    
    /**
     * Get configuration for a specific Bitcoin network API
     * 
     * @param network Bitcoin network (mainnet, testnet, etc.)
     * @return API endpoint URL
     */
    @GetMapping("/config/api/{apiType}")
    public ResponseEntity<String> api(@PathVariable String network, @PathVariable String apiType) {
        return ResponseEntity.ok(btcService.getApiEndpoint(network, apiType));
    }
}
