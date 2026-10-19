package com.startdemo.server.web.admin.dto;

import java.util.List;

public record AdminUserDetailResponse(
        AdminUserResponse wallet,
        List<AdminTransactionResponse> recentTransactions,
        List<AdminSessionResponse> recentSessions
) {
}
