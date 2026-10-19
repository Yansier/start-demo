import { defineStore } from 'pinia'

export const useUiStore = defineStore('ui', {
  state: () => ({
    serviceOpen: false,
    downloadOpen: false
  }),
  actions: {
    openService() {
      this.serviceOpen = true
    },
    closeService() {
      this.serviceOpen = false
    },
    openDownload() {
      this.downloadOpen = true
    },
    closeDownload() {
      this.downloadOpen = false
    }
  }
})
