export function readJSON(key) {
  try {
    if (typeof uni === 'undefined') return null
    const raw = uni.getStorageSync(key)
    if (!raw) return null
    return typeof raw === 'string' ? JSON.parse(raw) : raw
  } catch (error) {
    return null
  }
}

export function writeJSON(key, value) {
  uni.setStorageSync(key, JSON.stringify(value))
}
