import { http } from './http'

export function getDashboard() {
  return http('/api/admin/dashboard')
}

export function getUsers() {
  return http('/api/admin/users')
}

export function getUserDetail(uid) {
  return http(`/api/admin/users/${encodeURIComponent(uid)}`)
}

export function getTransactions(params = {}) {
  const query = new URLSearchParams()
  if (params.uid) query.set('uid', params.uid)
  if (params.type) query.set('type', params.type)
  if (params.limit) query.set('limit', String(params.limit))
  const text = query.toString()
  return http(`/api/admin/transactions${text ? `?${text}` : ''}`)
}

export function getSystemStatus() {
  return http('/api/admin/status')
}
