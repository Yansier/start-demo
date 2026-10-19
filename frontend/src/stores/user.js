import { defineStore } from 'pinia'
import { loginByPassword, loginByTelegram, registerByPassword } from '@/api/auth'
import { readJSON, writeJSON } from '@/utils/storage'

const STORAGE_KEY = 'zzyl_demo_session'

function emptyRemember() {
  return { enabled: false, account: '', password: '' }
}

export const useUserStore = defineStore('user', {
  state: () => ({
    token: '',
    profile: null,
    remember: emptyRemember(),
    localUsers: [],
    pendingInviteCode: '',
    ready: false
  }),
  getters: {
    isLogin: (state) => Boolean(state.token && state.profile)
  },
  actions: {
    hydrate() {
      if (this.ready || typeof uni === 'undefined') return
      const saved = readJSON(STORAGE_KEY)
      if (saved) {
        this.token = saved.token || ''
        this.profile = saved.profile || null
        this.remember = { ...emptyRemember(), ...(saved.remember || {}) }
        this.localUsers = Array.isArray(saved.localUsers) ? saved.localUsers : []
        this.pendingInviteCode = saved.pendingInviteCode || ''
      }
      this.ready = true
    },
    persist() {
      writeJSON(STORAGE_KEY, {
        token: this.token,
        profile: this.profile,
        remember: this.remember,
        localUsers: this.localUsers,
        pendingInviteCode: this.pendingInviteCode
      })
    },
    setPendingInviteCode(code) {
      const value = String(code || '').trim()
      if (!value || value === this.pendingInviteCode) return
      this.pendingInviteCode = value
      this.persist()
    },
    async login({ account, password, remember }) {
      const result = await loginByPassword({
        account,
        password,
        localUsers: this.localUsers
      })
      this.token = result.token
      this.profile = result.profile
      this.remember = remember
        ? { enabled: true, account: String(account).trim(), password }
        : emptyRemember()
      this.persist()
      return result
    },
    async loginWithTelegram() {
      const result = await loginByTelegram()
      this.token = result.token
      this.profile = result.profile
      this.persist()
      return result
    },
    async register({ account, password, inviteCode }) {
      const result = await registerByPassword({
        account,
        password,
        inviteCode,
        localUsers: this.localUsers
      })
      this.localUsers = [...this.localUsers, result.user]
      this.token = result.token
      this.profile = result.profile
      this.persist()
      return result
    },
    logout() {
      this.token = ''
      this.profile = null
      this.persist()
    }
  }
})
