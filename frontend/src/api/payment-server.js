import { javaRequest } from '@/api/java-server'

export function toServerChannel(channel) {
  if (channel === 'X钱包' || channel === 'X_WALLET') return 'X_WALLET'
  return 'USDT'
}

export function toDisplayChannel(channel) {
  return channel === 'X_WALLET' ? 'X钱包' : 'USDT'
}

export function depositStatusLabel(status) {
  if (status === 'APPROVED') return '已到账'
  if (status === 'REJECTED') return '已拒绝'
  return '处理中'
}

export function depositStatusTone(status) {
  if (status === 'APPROVED') return 'ok'
  if (status === 'REJECTED') return 'bad'
  return 'wait'
}

export function withdrawalStatusLabel(status) {
  if (status === 'APPROVED') return '已完成'
  if (status === 'REJECTED') return '已拒绝'
  return '审核中'
}

export function withdrawalStatusTone(status) {
  if (status === 'APPROVED') return 'ok'
  if (status === 'REJECTED') return 'bad'
  return 'wait'
}

export function createDeposit({ uid, channel, amount }) {
  return javaRequest('/api/payments/deposits', {
    method: 'POST',
    data: {
      uid,
      channel: toServerChannel(channel),
      amount
    }
  })
}

export function getDeposits(uid) {
  return javaRequest(`/api/payments/deposits/${encodeURIComponent(uid)}`)
}

export function createWithdrawal({ uid, channel, amount, address }) {
  return javaRequest('/api/payments/withdrawals', {
    method: 'POST',
    data: {
      uid,
      channel: toServerChannel(channel),
      amount,
      address: toServerChannel(channel) === 'USDT' ? address : null
    }
  })
}

export function getWithdrawals(uid) {
  return javaRequest(`/api/payments/withdrawals/${encodeURIComponent(uid)}`)
}
