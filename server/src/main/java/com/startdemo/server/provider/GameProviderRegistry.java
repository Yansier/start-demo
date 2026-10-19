package com.startdemo.server.provider;

import com.startdemo.server.error.BusinessException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GameProviderRegistry {

    private final Map<String, GameProvider> providers;

    public GameProviderRegistry(List<GameProvider> providers) {
        this.providers = providers.stream()
                .collect(Collectors.toUnmodifiableMap(GameProvider::getCode, Function.identity()));
    }

    public GameProvider require(String code) {
        GameProvider provider = providers.get(code);
        if (provider == null) {
            throw new BusinessException("游戏供应商不存在");
        }
        return provider;
    }
}
