export function showToast(title) {
  uni.showToast({ title, icon: 'none' })
}

export function showComingSoon() {
  showToast('功能即将开放')
}

export function showClosedFeature() {
  showToast('尚未开放功能')
}
