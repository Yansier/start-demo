import { javaRequest } from '@/api/java-server'

export { GameServerError } from '@/api/java-server'

/**
 * DEMO ONLY.
 * 钱包不存在时用前端初始余额创建。钱包已存在时，Java 返回当前余额，不会被这次请求覆盖。
 */
export function syncGameWallet(profile) {
  return javaRequest('/api/demo/wallet/sync', {
    method: 'POST',
    data: {
      uid: profile.uid,
      username: profile.username,
      balance: profile.balance
    }
  })
}

export function launchGame(uid, gameCode) {
  return javaRequest('/api/game/launch', {
    method: 'POST',
    data: { uid, gameCode }
  })
}

export function getGameBalance(uid) {
  return javaRequest(`/api/wallet/${encodeURIComponent(uid)}/balance`)
}

export function betGame({ sessionId, transactionId, amount }) {
  return javaRequest('/api/provider/mock/bet', {
    method: 'POST',
    data: { sessionId, transactionId, amount }
  })
}

export function settleGame({ sessionId, transactionId, relatedTransactionId, amount }) {
  return javaRequest('/api/provider/mock/settle', {
    method: 'POST',
    data: { sessionId, transactionId, relatedTransactionId, amount }
  })
}

export function rollbackGame({ sessionId, transactionId, relatedTransactionId }) {
  return javaRequest('/api/provider/mock/rollback', {
    method: 'POST',
    data: { sessionId, transactionId, relatedTransactionId }
  })
}

export function getGameTransactions(uid) {
  return javaRequest(`/api/wallet/${encodeURIComponent(uid)}/transactions`)
}

export function closeGameSession(sessionId) {
  return javaRequest(`/api/game/session/${encodeURIComponent(sessionId)}/close`, {
    method: 'POST'
  })
}
