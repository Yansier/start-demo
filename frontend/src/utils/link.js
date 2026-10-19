export function openLink(url) {
  if (!url || typeof window === 'undefined') return
  window.open(url, '_blank', 'noopener')
}
