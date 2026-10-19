<template>
  <view class="stats">
    <view v-for="item in items" :key="item.label" class="stat glass-card">
      <text class="stat__value">{{ item.value }}</text>
      <text class="stat__label">{{ item.label }}</text>
    </view>
  </view>
</template>

<script setup>
defineProps({
  items: { type: Array, default: () => [] }
})
</script>

<style scoped>
.stats {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
  margin-top: 14px;
}

.stat {
  padding: 14px 12px;
}

.stat__value,
.stat__label {
  display: block;
}

.stat__value {
  color: var(--gold);
  font-size: 22px;
  font-weight: 800;
}

.stat__label {
  margin-top: 4px;
  color: var(--muted);
  font-size: 12px;
}
</style>
