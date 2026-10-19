<template>
  <view class="balance glass-card">
    <text class="balance__label">{{ label }}</text>
    <text class="amount balance__value">{{ formatAmount(balance) }}</text>
    <text v-if="extra" class="balance__extra">{{ extra }}</text>
  </view>
</template>

<script setup>
import { formatAmount } from '@/utils/format'

defineProps({
  balance: { type: Number, default: 0 },
  label: { type: String, default: '可用余额' },
  extra: { type: String, default: '' }
})
</script>

<style scoped>
.balance {
  padding: 16px;
}

.balance__label,
.balance__value,
.balance__extra {
  display: block;
}

.balance__label,
.balance__extra {
  color: var(--muted);
  font-size: 12px;
}

.balance__value {
  margin-top: 6px;
  font-size: 28px;
}

.balance__extra {
  margin-top: 6px;
}
</style>
