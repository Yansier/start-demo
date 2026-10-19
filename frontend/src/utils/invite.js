import { TELEGRAM_BOT } from '@/config/app'
import { showToast } from '@/utils/toast'

export function buildBotInviteLink(uid) {
  const base = String(TELEGRAM_BOT || '').replace(/\/$/, '')
  return `${base}?start=${encodeURIComponent(uid || '')}`
}

export function buildWebInviteLink(uid) {
  const origin = typeof window === 'undefined' ? '' : window.location.origin
  const pathname = typeof window === 'undefined' ? '' : window.location.pathname
  return `${origin}${pathname}#/pages/home/index?upcode=${encodeURIComponent(uid || '')}`
}

export function buildTelegramShareUrl(link) {
  const text = '邀请你来 DEMO 娱乐平台'
  return `https://t.me/share/url?url=${encodeURIComponent(link)}&text=${encodeURIComponent(text)}`
}

function copyByCommand(text) {
  const textarea = document.createElement('textarea')
  textarea.value = text
  textarea.setAttribute('readonly', 'readonly')
  textarea.style.position = 'fixed'
  textarea.style.top = '0'
  textarea.style.left = '0'
  textarea.style.opacity = '0'
  document.body.appendChild(textarea)
  textarea.focus()
  textarea.select()
  let copied = false
  try {
    copied = document.execCommand('copy')
  } catch (error) {
    copied = false
  }
  document.body.removeChild(textarea)
  return copied
}

export async function copyText(text) {
  const value = String(text || '')
  if (typeof navigator !== 'undefined' && navigator.clipboard?.writeText) {
    try {
      await navigator.clipboard.writeText(value)
      showToast('复制成功')
      return
    } catch (error) {
      // 继续走本地复制和 uni 剪贴板
    }
  }
  const copied = typeof document !== 'undefined' && copyByCommand(value)
  uni.setClipboardData({
    data: value,
    showToast: false,
    success() {
      if (!copied) showToast('复制成功')
    },
    fail() {
      if (!copied) showToast('复制失败')
    }
  })
  if (copied) showToast('复制成功')
}

export function shareTelegram(link) {
  const shareUrl = buildTelegramShareUrl(link)
  const popup = window.open(shareUrl, '_blank', 'noopener')
  if (!popup) copyText(link)
}
