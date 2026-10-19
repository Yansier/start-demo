import { useUiStore } from '@/stores/ui'
import { showComingSoon } from '@/utils/toast'

export function runMenuAction(action) {
  const uiStore = useUiStore()
  if (action === 'service') {
    uiStore.openService()
    return
  }
  if (action === 'download') {
    uiStore.openDownload()
    return
  }
  showComingSoon()
}
