package com.example.wallet.web;

import com.example.wallet.domain.BalanceResponse;
import com.example.wallet.service.BalanceService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/eth/{network}")
@Validated
public class EthController {

    private final BalanceService balanceService;

    public EthController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/{address}/balance")
    public ResponseEntity<BalanceResponse> getBalance(
            @PathVariable String network,
            @PathVariable @NotBlank String address) {
        return ResponseEntity.ok(balanceService.ethBalance(network, address));
    }

    @GetMapping("/config/rpc")
    public ResponseEntity<String> rpc(@PathVariable String network) {
        return ResponseEntity.ok(balanceService.effectiveEthRpc(network));
    }
}
