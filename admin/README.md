# DEMO Admin

本地只读运营后台，用来查看 Spring Boot Game Wallet 里的真实数据。

当前没有登录。Phase 5A 只做本地演示查询，不提供改余额、审核或配置。

在把这个后台或对应 Admin API 部署到公网之前，必须先增加访问保护。现在不要把它当成可以公开使用的管理系统。

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
