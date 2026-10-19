package com.startdemo.server.web;

import com.startdemo.server.provider.GameProvider;
import com.startdemo.server.provider.GameProviderRegistry;
import com.startdemo.server.provider.MockGameProvider;
import com.startdemo.server.web.dto.BetRequest;
import com.startdemo.server.web.dto.RollbackRequest;
import com.startdemo.server.web.dto.SettleRequest;
import com.startdemo.server.web.dto.WalletResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/provider/mock")
public class MockProviderController {

    private final GameProviderRegistry providerRegistry;

    public MockProviderController(GameProviderRegistry providerRegistry) {
        this.providerRegistry = providerRegistry;
    }

    @PostMapping("/bet")
    public WalletResult bet(@Valid @RequestBody BetRequest request) {
        return provider().bet(request.sessionId(), request.transactionId(), request.amount());
    }

    @PostMapping("/settle")
    public WalletResult settle(@Valid @RequestBody SettleRequest request) {
        return provider().settle(
                request.sessionId(),
                request.transactionId(),
                request.relatedTransactionId(),
                request.amount()
        );
    }

    @PostMapping("/rollback")
    public WalletResult rollback(@Valid @RequestBody RollbackRequest request) {
        return provider().rollback(
                request.sessionId(),
                request.transactionId(),
                request.relatedTransactionId()
        );
    }

    private GameProvider provider() {
        return providerRegistry.require(MockGameProvider.CODE);
    }
}
