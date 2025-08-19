package com.example.wallet.web;

import com.example.wallet.domain.BalanceResponse;
import com.example.wallet.service.BalanceService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/btc/{network}")
@Validated
public class BtcController {

    private final BalanceService balanceService;

    public BtcController(BalanceService balanceService) {
        this.balanceService = balanceService;
    }

    @GetMapping("/{address}/balance")
    public ResponseEntity<BalanceResponse> getBalance(
            @PathVariable String network,
            @PathVariable @NotBlank String address) {
        return ResponseEntity.ok(balanceService.btcBalance(network, address));
    }

    @GetMapping("/config/api")
    public ResponseEntity<Object> api() {
        return ResponseEntity.ok(balanceService.effectiveBtcRpc());
    }
}
