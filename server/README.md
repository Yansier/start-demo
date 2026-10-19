# DEMO Game Wallet Server

这是一个可运行的 Demo 游戏钱包服务，用来模拟游戏平台和外部游戏供应商之间的核心流程：

- Game Session
- Balance
- Bet
- Settle
- Rollback
- Wallet Transaction

它不是正式生产系统。Phase 1–3 的用户账户、充值、提现和页面余额仍然以 Frontend Mock 为主。Java Server 目前只负责模拟游戏供应商和 Seamless Wallet 核心流程。

## 启动

要求：Java 17 或 21，Maven。

在 `server` 目录执行：

```bash
mvn spring-boot:run
```

默认端口：`8080`

## H2

Demo 数据库使用 H2 文件模式：

```text
jdbc:h2:file:./data/start-demo
```

数据写在 `server/data/`。重启 Spring Boot 后，钱包、会话和流水仍然保留。测试使用独立的内存库，不会改写这份文件。

## 金额

余额和流水金额使用 `BigDecimal`，保留两位小数。不要用 `double` 处理金额。

## API

### DEMO ONLY：同步前端余额

`POST /api/demo/wallet/sync`

```json
{ "uid": "161936", "username": "DemoPlayer", "balance": 1888.66 }
```

返回 `{ "uid": "161936", "balance": 1888.66 }`。

这个接口只用于当前 Demo，方便进入游戏前把前端 Mock 余额交给 Java Game Wallet。

正式系统中，用户账户、充值、提现、余额、游戏钱包都应该以服务器数据库为唯一数据源。前端不能上传余额并覆盖服务器。

### 查询余额

`GET /api/wallet/{uid}/balance`

### 启动游戏

`POST /api/game/launch`

```json
{ "uid": "161936", "gameCode": "demo-slots" }
```

钱包必须已存在。返回 `sessionId`、`provider: MOCK`、`status: ACTIVE`。本阶段不返回外部游戏地址。

### Mock Provider

`POST /api/provider/mock/bet`

```json
{ "sessionId": "...", "transactionId": "BET-001", "amount": 50 }
```

`POST /api/provider/mock/settle`

```json
{ "sessionId": "...", "transactionId": "SETTLE-001", "relatedTransactionId": "BET-001", "amount": 100 }
```

`POST /api/provider/mock/rollback`

```json
{ "sessionId": "...", "transactionId": "ROLLBACK-001", "relatedTransactionId": "BET-001" }
```

成功时返回 `transactionId`、`balance`、`status: SUCCESS`。

BET 记负数流水，SETTLE 记正数流水。ROLLBACK 按原 BET 金额退回。同一个 `transactionId` 重复请求不会再次改余额。同一个 BET 只能回滚一次。会话关闭后不能再 BET 或 SETTLE。

### 查询流水

`GET /api/wallet/{uid}/transactions`

按 `createdAt` 倒序。

### 关闭会话

`POST /api/game/session/{sessionId}/close`

### DEMO ONLY：重置

`POST /api/demo/reset/{uid}`

删除该用户的游戏会话和钱包流水，余额重置为 `0.00`。只用于开发调试。重置后需要重新调用 sync。

## 错误

业务错误返回：

```json
{ "success": false, "message": "余额不足" }
```

不会把 Java 堆栈返回给调用方。

## CORS

允许：

- `http://localhost:5173`
- `https://yansier.github.io`

不使用 `allowedOrigins("*")`。
