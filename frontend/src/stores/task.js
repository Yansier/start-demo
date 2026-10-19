import { defineStore } from 'pinia'
import { tasks } from '@/mock/tasks'
import { readJSON, writeJSON } from '@/utils/storage'

function storageKey(uid) {
  return `start_demo_tasks_${uid}`
}

export const useTaskStore = defineStore('task', {
  state: () => ({
    uid: '',
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
    hydrate(uid) {
      if (typeof uni === 'undefined') return
      const next = String(uid || '')
      if (!next) {
        this.uid = ''
        this.statusMap = {}
        this.ready = false
        return
      }
      if (this.ready && this.uid === next) return
      const saved = readJSON(storageKey(next))
      this.uid = next
      this.statusMap = saved?.statusMap && typeof saved.statusMap === 'object' ? saved.statusMap : {}
      this.ready = true
    },
    persist() {
      if (!this.uid) return
      writeJSON(storageKey(this.uid), { statusMap: this.statusMap })
    },
    claim(id) {
      if (!this.uid) return false
      const task = this.list.find((item) => item.id === id)
      if (!task || task.status !== 'claimable') return false
      this.statusMap = { ...this.statusMap, [id]: 'claimed' }
      this.persist()
      return true
    }
  }
})
