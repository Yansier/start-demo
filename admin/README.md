# DEMO Admin

本地只读运营后台，用来查看 Spring Boot Game Wallet 里的真实数据。

本地只读查询之外，Phase 5B.1 增加了充值和提现审核。这些写接口没有登录。

当前没有登录。这是本地 Demo，不要部署到公网。在增加 Admin 访问保护之前，不能公开这些审核接口。

## 安装

```bash
npm install
```

## 启动

```bash
npm run dev
```

默认地址：`http://localhost:5174`

需要先启动 Spring Boot：`http://localhost:8080`

开发环境接口地址在 `.env.development`：

```text
VITE_ADMIN_API_BASE=http://localhost:8080
```
