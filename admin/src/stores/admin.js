import { defineStore } from 'pinia'
import { getDashboard, getDeposits, getSystemStatus, getTransactions, getUserDetail, getUsers, getWithdrawals, approveDeposit, approveWithdrawal, rejectDeposit, rejectWithdrawal } from '@/api/admin'

export const useAdminStore = defineStore('admin', {
  state: () => ({
    dashboard: null,
    recentTransactions: [],
    users: [],
    detail: null,
    transactions: [],
    deposits: [],
    withdrawals: [],
    depositQuery: { limit: 100 },
    withdrawalQuery: { limit: 100 },
    system: null,
    loading: false,
    error: ''
  }),
  actions: {
    async run(task) {
      this.loading = true
      this.error = ''
      try {
        await task()
      } catch (error) {
        this.error = error.message || '无法连接 Demo Server'
      } finally {
        this.loading = false
      }
    },
    loadDashboard() {
      return this.run(async () => {
        const [dashboard, recentTransactions] = await Promise.all([
          getDashboard(),
          getTransactions({ limit: 8 })
        ])
        this.dashboard = dashboard
        this.recentTransactions = recentTransactions
      })
    },
    loadUsers() {
      return this.run(async () => {
        this.users = await getUsers()
      })
    },
    loadDetail(uid) {
      return this.run(async () => {
        this.detail = await getUserDetail(uid)
      })
    },
    loadTransactions(params) {
      return this.run(async () => {
        this.transactions = await getTransactions(params)
      })
    },
    loadStatus() {
      return this.run(async () => {
        this.system = await getSystemStatus()
      })
    },
    loadDeposits(params = { limit: 100 }) {
      this.depositQuery = params
      return this.run(async () => {
        this.deposits = await getDeposits(params)
      })
    },
    loadWithdrawals(params = { limit: 100 }) {
      this.withdrawalQuery = params
      return this.run(async () => {
        this.withdrawals = await getWithdrawals(params)
      })
    },
    async approveDeposit(orderNo) {
      await approveDeposit(orderNo)
      this.deposits = await getDeposits(this.depositQuery)
      this.dashboard = await getDashboard()
    },
    async rejectDeposit(orderNo, remark) {
      await rejectDeposit(orderNo, remark)
      this.deposits = await getDeposits(this.depositQuery)
      this.dashboard = await getDashboard()
    },
    async approveWithdrawal(orderNo) {
      await approveWithdrawal(orderNo)
      this.withdrawals = await getWithdrawals(this.withdrawalQuery)
      this.dashboard = await getDashboard()
    },
    async rejectWithdrawal(orderNo, remark) {
      await rejectWithdrawal(orderNo, remark)
      this.withdrawals = await getWithdrawals(this.withdrawalQuery)
      this.dashboard = await getDashboard()
    }
  }
})
