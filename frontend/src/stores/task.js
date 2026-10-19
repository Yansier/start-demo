import { defineStore } from 'pinia'
import { tasks } from '@/mock/tasks'
import { readJSON, writeJSON } from '@/utils/storage'

const STORAGE_KEY = 'start_demo_tasks'

export const useTaskStore = defineStore('task', {
  state: () => ({
    statusMap: {},
    ready: false
  }),
  getters: {
    list() {
      return tasks.map((item) => ({
        ...item,
        status: this.statusMap[item.id] || item.status
      }))
    }
  },
  actions: {
    hydrate() {
      if (this.ready || typeof uni === 'undefined') return
      const saved = readJSON(STORAGE_KEY)
      this.statusMap = saved?.statusMap && typeof saved.statusMap === 'object' ? saved.statusMap : {}
      this.ready = true
    },
    persist() {
      writeJSON(STORAGE_KEY, { statusMap: this.statusMap })
    },
    claim(id) {
      this.hydrate()
      const task = this.list.find((item) => item.id === id)
      if (!task || task.status !== 'claimable') return false
      this.statusMap = { ...this.statusMap, [id]: 'claimed' }
      this.persist()
      return true
    }
  }
})
