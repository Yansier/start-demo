export function validateAccount(account) {
  const value = String(account || '').trim()
  if (!value) return '请输入账号'
  if (!/^[A-Za-z0-9]{4,16}$/.test(value)) return '账号为4-16位字母或数字'
  return ''
}

export function validatePassword(password) {
  const value = String(password || '')
  if (!value) return '请输入密码'
  if (value.length < 6) return '密码至少6位'
  return ''
}
