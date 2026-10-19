package com.startdemo.server.web.admin.dto;

import java.math.BigDecimal;

public record AdminDashboardResponse(
        long walletUsers,
        BigDecimal totalBalance,
        long activeSessions,
        long todayBetCount,
        BigDecimal todayBetAmount,
        long todaySettleCount,
        BigDecimal todaySettleAmount,
        long todayRollbackCount,
        long todayTransactionCount,
        long pendingDeposits,
        long pendingWithdrawals
) {
}
