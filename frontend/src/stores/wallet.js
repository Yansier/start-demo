import { defineStore } from 'pinia'
import { getGameBalance, syncGameWallet } from '@/api/game-server'
import { createDeposit, createWithdrawal, getDeposits, getWithdrawals, toServerChannel } from '@/api/payment-server'
import { useUserStore } from '@/stores/user'
import { readJSON, writeJSON } from '@/utils/storage'

function storageKey(uid) {
  return `start_demo_wallet_${uid}`
}

function roundMoney(value) {
  return Math.round(Number(value) * 100) / 100
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
      const saved = readJSON(storageKey(next)) || {}
      this.uid = next
      this.address = saved.address || ''
      this.withdrawPassword = saved.withdrawPassword || ''
      this.deposits = []
      this.withdrawals = []
      this.transactions = []
      this.requiredTurnover = Number(saved.requiredTurnover) || 0
      this.ready = true
    },
    persist() {
      if (!this.uid) return
      writeJSON(storageKey(this.uid), {
        address: this.address,
        withdrawPassword: this.withdrawPassword,
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
    async ensureServerWallet(profile) {
      if (!profile?.uid) {
        return null
      }
      this.bind(profile.uid)
      const wallet = await syncGameWallet(profile)
      const user = useUserStore()
      if (user.profile?.uid === profile.uid) {
        user.setBalance(wallet.balance)
      }
      return wallet
    },
    async refreshBalance() {
      const user = useUserStore()
      if (!user.profile?.uid) return null
      const result = await getGameBalance(user.profile.uid)
      user.setBalance(result.balance)
      return result
    },
    async loadDeposits() {
      if (!this.uid) return []
      const list = await getDeposits(this.uid)
      this.deposits = Array.isArray(list) ? list : []
      return this.deposits
    },
    async loadWithdrawals() {
      if (!this.uid) return []
      const list = await getWithdrawals(this.uid)
      this.withdrawals = Array.isArray(list) ? list : []
      return this.withdrawals
    },
    async createDeposit({ channel, amount }) {
      const user = useUserStore()
      if (!user.isLogin || !user.profile) return { ok: false, message: '请先登录' }
      const value = roundMoney(amount)
      if (!value) return { ok: false, message: '请输入充值金额' }
      if (value < 10) return { ok: false, message: '最低充值金额为10' }
      try {
        await this.ensureServerWallet(user.profile)
        await createDeposit({
          uid: user.profile.uid,
          channel,
          amount: value
        })
      } catch (error) {
        return { ok: false, message: error.message || '充值订单提交失败' }
      }
      try {
        await this.loadDeposits()
      } catch {
        // 订单已经创建，列表留到记录页重新请求。
      }
      return { ok: true }
    },
    async submitWithdraw({ channel, amount, password }) {
      const user = useUserStore()
      if (!user.isLogin || !this.uid) return { ok: false, message: '请先登录' }
      const value = roundMoney(amount)
      const serverChannel = toServerChannel(channel)
      if (serverChannel === 'USDT' && !this.address) return { ok: false, message: '请先绑定提现地址' }
      if (!value) return { ok: false, message: '请输入提现金额' }
      if (value < 10) return { ok: false, message: '最低提现金额为10U' }
      if (!this.withdrawPassword) return { ok: false, message: '请先设置提现密码' }
      if (String(password || '') !== this.withdrawPassword) return { ok: false, message: '提现密码错误' }
      try {
        await this.ensureServerWallet(user.profile)
        await createWithdrawal({
          uid: this.uid,
          channel,
          amount: value,
          address: serverChannel === 'USDT' ? this.address : null
        })
      } catch (error) {
        return { ok: false, message: error.message || '提现申请失败' }
      }
      try {
        await this.refreshBalance()
        await this.loadWithdrawals()
      } catch {
        // 扣款已经由 Java 完成，页面再次进入时会重新读取余额。
      }
      return { ok: true }
    }
  }
})
