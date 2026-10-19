<template>
  <view class="record glass-card">
    <view class="record__top">
      <text class="record__title">{{ title }}</text>
      <text class="record__amount">{{ amountText }}</text>
    </view>
    <view class="record__foot">
      <text class="record__status" :class="tone">{{ status }}</text>
      <text class="record__time">{{ time }}</text>
    </view>
    <text v-if="extra" class="record__extra">{{ extra }}</text>
  </view>
</template>

<script setup>
defineProps({
  title: { type: String, default: '' },
  amountText: { type: String, default: '' },
  status: { type: String, default: '' },
  time: { type: String, default: '' },
  extra: { type: String, default: '' },
  tone: { type: String, default: '' }
})
</script>

<style scoped>
.record {
  margin-top: 12px;
  padding: 14px;
}

.record__top,
.record__foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
}

.record__title,
.record__amount {
  font-size: 15px;
  font-weight: 800;
}

.record__amount {
  color: var(--gold);
}

.record__foot {
  margin-top: 8px;
}

.record__status,
.record__time,
.record__extra {
  font-size: 12px;
}

.record__time,
.record__extra {
  color: var(--muted);
}

.record__extra {
  display: block;
  margin-top: 8px;
}

.record__status.ok {
  color: var(--cyan);
}

.record__status.wait {
  color: var(--gold);
}

.record__status.bad {
  color: #ff8aa6;
}
</style>
