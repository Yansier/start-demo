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

function queryText(params = {}) {
  const query = new URLSearchParams()
  if (params.uid) query.set('uid', params.uid)
  if (params.status) query.set('status', params.status)
  if (params.limit) query.set('limit', String(params.limit))
  const text = query.toString()
  return text ? `?${text}` : ''
}

export function getDeposits(params) {
  return http(`/api/admin/deposits${queryText(params)}`)
}

export function approveDeposit(orderNo, remark) {
  return http(`/api/admin/deposits/${encodeURIComponent(orderNo)}/approve`, {
    method: 'POST',
    data: remark ? { remark } : {}
  })
}

export function rejectDeposit(orderNo, remark) {
  return http(`/api/admin/deposits/${encodeURIComponent(orderNo)}/reject`, {
    method: 'POST',
    data: { remark }
  })
}

export function getWithdrawals(params) {
  return http(`/api/admin/withdrawals${queryText(params)}`)
}

export function approveWithdrawal(orderNo, remark) {
  return http(`/api/admin/withdrawals/${encodeURIComponent(orderNo)}/approve`, {
    method: 'POST',
    data: remark ? { remark } : {}
  })
}

export function rejectWithdrawal(orderNo, remark) {
  return http(`/api/admin/withdrawals/${encodeURIComponent(orderNo)}/reject`, {
    method: 'POST',
    data: { remark }
  })
}
