package com.startdemo.server.web.admin;

import com.startdemo.server.service.AdminQueryService;
import com.startdemo.server.web.admin.dto.AdminDashboardResponse;
import com.startdemo.server.web.admin.dto.AdminSystemStatusResponse;
import com.startdemo.server.web.admin.dto.AdminTransactionResponse;
import com.startdemo.server.web.admin.dto.AdminUserDetailResponse;
import com.startdemo.server.web.admin.dto.AdminUserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * DEMO ONLY，READ ONLY。
 * 这些接口没有访问保护，不能当作可公开部署的管理接口。
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminQueryService adminQueryService;

    public AdminController(AdminQueryService adminQueryService) {
        this.adminQueryService = adminQueryService;
    }

    @GetMapping("/dashboard")
    public AdminDashboardResponse dashboard() {
        return adminQueryService.dashboard();
    }

    @GetMapping("/users")
    public List<AdminUserResponse> users() {
        return adminQueryService.users();
    }

    @GetMapping("/users/{uid}")
    public AdminUserDetailResponse user(@PathVariable String uid) {
        return adminQueryService.userDetail(uid);
    }

    @GetMapping("/transactions")
    public List<AdminTransactionResponse> transactions(
            @RequestParam(required = false) String uid,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer limit
    ) {
        return adminQueryService.transactions(uid, type, limit);
    }

    @GetMapping("/status")
    public AdminSystemStatusResponse status() {
        return adminQueryService.status();
    }
}
