const API_BASE = (import.meta.env.VITE_ADMIN_API_BASE || 'http://localhost:8080').replace(/\/$/, '')

export async function http(path, { method = 'GET', data } = {}) {
  let response
  try {
    response = await fetch(`${API_BASE}${path}`, {
      method,
      headers: data ? { 'Content-Type': 'application/json' } : undefined,
      body: data ? JSON.stringify(data) : undefined
    })
  } catch (error) {
    throw new Error('无法连接 Demo Server')
  }
  let payload = null
  try {
    payload = await response.json()
  } catch (error) {
    payload = null
  }
  if (!response.ok || payload?.success === false) {
    throw new Error(payload?.message || '无法连接 Demo Server')
  }
  return payload
}
