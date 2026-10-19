export function formatAmount(value) {
  const amount = Number(value) || 0
  return amount.toLocaleString('en-US', {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2
  })
}

export function avatarText(name) {
  const value = String(name || '').trim()
  return value ? value.slice(0, 1).toUpperCase() : '游'
}
