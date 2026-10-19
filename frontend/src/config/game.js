const envBase = import.meta.env.VITE_GAME_API_BASE

function resolveGameApiBase() {
  if (typeof envBase === 'string' && envBase.trim()) {
    return envBase.trim().replace(/\/$/, '')
  }
  if (typeof window !== 'undefined') {
    const host = window.location.hostname
    if (host === 'localhost' || host === '127.0.0.1') {
      return 'http://localhost:8080'
    }
  }
  return ''
}

export const GAME_API_BASE = resolveGameApiBase()
export const DEMO_GAME_ID = 'demo-slots'
export const DEMO_GAME_CODE = 'demo-slots'
