import { defineStore } from 'pinia'
import { createDefaultWallet } from '@/mock/wallet'
import { useUserStore } from '@/stores/user'
import { readJSON, writeJSON } from '@/utils/storage'

function storageKey(uid) {
  return `start_demo_wallet_${uid}`
}

function roundMoney(value) {
  return Math.round(Number(value) * 100) / 100
}

function nowText() {
  const date = new Date()
  const pad = (value) => String(value).padStart(2, '0')
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}`
}

function emptyWallet() {
  return {
    address: '',
    withdrawPassword: '',
    deposits: [],
    withdrawals: [],
    transactions: [],
    requiredTurnover: 0
  }
}

export const useWalletStore = defineStore('wallet', {
  state: () => ({
    uid: '',
    ready: false,
    ...emptyWallet()
  }),
  getters: {
    hasPassword: (state) => Boolean(state.withdrawPassword),
    turnoverReady: (state) => Number(state.requiredTurnover) === 0
  },
  actions: {
    bind(uid) {
      if (typeof uni === 'undefined') return
      const next = String(uid || '')
      if (!next) {
        this.uid = ''
        this.ready = false
        Object.assign(this, emptyWallet())
        return
      }
      if (this.ready && this.uid === next) return
      const saved = readJSON(storageKey(next))
      const source = saved || createDefaultWallet()
      this.uid = next
      this.address = source.address || ''
      this.withdrawPassword = source.withdrawPassword || ''
      this.deposits = Array.isArray(source.deposits) ? source.deposits : []
      this.withdrawals = Array.isArray(source.withdrawals) ? source.withdrawals : []
      this.transactions = Array.isArray(source.transactions) ? source.transactions : []
      this.requiredTurnover = Number(source.requiredTurnover) || 0
      this.ready = true
    },
    persist() {
      if (!this.uid) return
      writeJSON(storageKey(this.uid), {
        address: this.address,
        withdrawPassword: this.withdrawPassword,
        deposits: this.deposits,
        withdrawals: this.withdrawals,
        transactions: this.transactions,
        requiredTurnover: this.requiredTurnover
      })
    },
    bindAddress(address) {
      const value = String(address || '').trim()
      if (!value) return '请输入提现地址'
      if (value.length < 16 || value.length > 64) return '请输入长度合理的地址'
      this.address = value
      this.persist()
      return ''
    },
    setWithdrawPassword(password, confirm) {
      const value = String(password || '')
      if (value.length < 6) return '提现密码至少6位'
      if (value !== String(confirm || '')) return '两次密码不一致'
      this.withdrawPassword = value
      this.persist()
      return ''
    },
    submitWithdraw({ channel, amount, password }) {
      if (!this.uid) return { ok: false, message: '请先登录' }
      const value = roundMoney(amount)
      if (channel === 'USDT' && !this.address) return { ok: false, message: '请先绑定提现地址' }
      if (!value) return { ok: false, message: '请输入提现金额' }
      if (value < 10) return { ok: false, message: '最低提现金额为10U' }
      const balance = Number(useUserStore().profile?.balance) || 0
      if (value > balance) return { ok: false, message: '余额不足' }
      if (!this.withdrawPassword) return { ok: false, message: '请先设置提现密码' }
      if (String(password || '') !== this.withdrawPassword) return { ok: false, message: '提现密码错误' }
      useUserStore().adjustBalance(-value)
      this.withdrawals = [
        {
          id: `wd-${Date.now()}`,
          channel,
          amount: value,
          fee: 0,
          status: '审核中',
          time: nowText(),
          address: channel === 'USDT' ? this.address : ''
        },
        ...this.withdrawals
      ]
      this.persist()
      return { ok: true }
    }
  }
})
