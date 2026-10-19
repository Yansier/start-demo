package com.startdemo.server.web.dto;

import java.math.BigDecimal;

public record BalanceResponse(String uid, BigDecimal balance) {
}
