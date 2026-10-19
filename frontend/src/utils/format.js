export function formatAmount(value) {
  const amount = Number(value) || 0
  return amount.toLocaleString('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

export function shortText(value, head = 4, tail = 4) {
  const text = String(value || '')
  if (text.length <= head + tail + 1) return text
  return `${text.slice(0, head)}...${text.slice(-tail)}`
}

export function avatarText(name) {
  const value = String(name || '').trim()
  return value ? value.slice(0, 1).toUpperCase() : '游'
}
