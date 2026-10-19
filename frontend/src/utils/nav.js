import { TAB_PAGES } from '@/config/routes'

export function openPage(url) {
  uni.navigateTo({ url })
}

export function switchTab(url) {
  const current = getCurrentRoute()
  if (current === url.replace(/^\//, '')) return
  uni.redirectTo({ url })
}

export function relaunch(url) {
  uni.reLaunch({ url })
}

export function goBack(fallback) {
  const pages = getCurrentPages()
  if (pages.length > 1) {
    uni.navigateBack()
    return
  }
  if (fallback) {
    uni.redirectTo({ url: fallback })
  }
}

export function getCurrentRoute() {
  const pages = getCurrentPages()
  const current = pages[pages.length - 1]
  return current?.route || ''
}

export function isTabPage(url) {
  return TAB_PAGES.includes(url)
}
