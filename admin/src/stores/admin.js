import { defineStore } from 'pinia'
import { getDashboard, getSystemStatus, getTransactions, getUserDetail, getUsers } from '@/api/admin'

export const useAdminStore = defineStore('admin', {
  state: () => ({
    dashboard: null,
    recentTransactions: [],
    users: [],
    detail: null,
    transactions: [],
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
    }
  }
})
