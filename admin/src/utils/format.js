export function money(value) {
  const amount = Number(value)
  if (!Number.isFinite(amount)) return '0.00'
  return amount.toLocaleString('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

export function timeText(value) {
  if (!value) return '-'
  return String(value).replace('T', ' ').slice(0, 19)
}

export function shortId(value) {
  const text = String(value || '')
  if (!text) return '-'
  if (text.length <= 16) return text
  return `${text.slice(0, 8)}...${text.slice(-4)}`
}

export function signedMoney(value) {
  const amount = Number(value)
  if (!Number.isFinite(amount)) return '0.00'
  const text = money(Math.abs(amount))
  return amount < 0 ? `-${text}` : `+${text}`
}
