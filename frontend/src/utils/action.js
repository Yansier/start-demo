import { useUiStore } from '@/stores/ui'
import { openPage } from '@/utils/nav'
import { showComingSoon } from '@/utils/toast'

export function runMenuAction(entry) {
  const action = typeof entry === 'string' ? entry : entry?.action
  const url = typeof entry === 'object' ? entry?.url : ''
  const uiStore = useUiStore()
  if (action === 'service') {
    uiStore.openService()
    return
  }
  if (action === 'download') {
    uiStore.openDownload()
    return
  }
  if (action === 'navigate' && url) {
    openPage(url)
    return
  }
  showComingSoon()
}
