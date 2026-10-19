package com.startdemo.server.web.dto;

import java.math.BigDecimal;

public record WalletResult(String transactionId, BigDecimal balance, String status) {

    public static WalletResult success(String transactionId, BigDecimal balance) {
        return new WalletResult(transactionId, balance, "SUCCESS");
    }
}
