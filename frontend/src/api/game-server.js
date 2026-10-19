import { GAME_API_BASE } from '@/config/game'

export class GameServerError extends Error {
  constructor(message) {
    super(message)
    this.name = 'GameServerError'
  }
}

function request(path, { method = 'GET', data } = {}) {
  if (!GAME_API_BASE) {
    return Promise.reject(new GameServerError('Java游戏服务尚未部署'))
  }
  return new Promise((resolve, reject) => {
    uni.request({
      url: `${GAME_API_BASE}${path}`,
      method,
      data,
      header: { 'Content-Type': 'application/json' },
      timeout: 8000,
      success(res) {
        const payload = res.data
        const message = payload && typeof payload === 'object' ? payload.message : ''
        if (res.statusCode >= 200 && res.statusCode < 300 && payload?.success !== false) {
          resolve(payload)
          return
        }
        reject(new GameServerError(message || '游戏服务未启动'))
      },
      fail() {
        reject(new GameServerError('游戏服务未启动'))
      }
    })
  })
}

/**
 * DEMO ONLY.
 * 正式生产系统中，余额必须由服务器作为唯一数据源，不会由前端上传余额覆盖服务器。
 */
export function syncGameWallet(profile) {
  return request('/api/demo/wallet/sync', {
    method: 'POST',
    data: {
      uid: profile.uid,
      username: profile.username,
      balance: profile.balance
    }
  })
}

export function launchGame(uid, gameCode) {
  return request('/api/game/launch', {
    method: 'POST',
    data: { uid, gameCode }
  })
}

export function getGameBalance(uid) {
  return request(`/api/wallet/${encodeURIComponent(uid)}/balance`)
}

export function betGame({ sessionId, transactionId, amount }) {
  return request('/api/provider/mock/bet', {
    method: 'POST',
    data: { sessionId, transactionId, amount }
  })
}

export function settleGame({ sessionId, transactionId, relatedTransactionId, amount }) {
  return request('/api/provider/mock/settle', {
    method: 'POST',
    data: { sessionId, transactionId, relatedTransactionId, amount }
  })
}

export function rollbackGame({ sessionId, transactionId, relatedTransactionId }) {
  return request('/api/provider/mock/rollback', {
    method: 'POST',
    data: { sessionId, transactionId, relatedTransactionId }
  })
}

export function getGameTransactions(uid) {
  return request(`/api/wallet/${encodeURIComponent(uid)}/transactions`)
}

export function closeGameSession(sessionId) {
  return request(`/api/game/session/${encodeURIComponent(sessionId)}/close`, {
    method: 'POST'
  })
}
