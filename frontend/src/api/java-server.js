import { GAME_API_BASE } from '@/config/game'

export class GameServerError extends Error {
  constructor(message) {
    super(message)
    this.name = 'GameServerError'
  }
}

export function javaRequest(path, { method = 'GET', data } = {}) {
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
        const message = payload && typeof payload === 'object' && !Array.isArray(payload) ? payload.message : ''
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
