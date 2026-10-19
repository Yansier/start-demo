<template>
  <view class="rebate glass-card">
    <text class="rebate__title">返水比例</text>
    <view v-for="item in items" :key="item.name" class="row">
      <view class="row__top">
        <text>{{ item.name }}</text>
        <text class="row__rate">{{ item.rate.toFixed(2) }}%</text>
      </view>
      <view class="bar">
        <view class="bar__fill" :style="{ width: `${Math.min(100, (item.rate / maxRate) * 100)}%` }" />
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  items: { type: Array, default: () => [] }
})

const maxRate = computed(() => Math.max(...props.items.map((item) => item.rate), 1))
</script>

<style scoped>
.rebate {
  margin-top: 14px;
  padding: 14px;
}

.rebate__title {
  display: block;
  margin-bottom: 8px;
  font-size: 16px;
  font-weight: 800;
}

.row {
  margin-top: 10px;
}

.row__top {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 13px;
}

.row__rate {
  color: var(--cyan);
  font-weight: 700;
}

.bar {
  height: 6px;
  margin-top: 6px;
  border-radius: 999px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.08);
}

.bar__fill {
  height: 100%;
  border-radius: inherit;
  background: linear-gradient(90deg, #5cefff, #8d4dff);
}
</style>
