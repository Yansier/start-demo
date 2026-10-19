<template>
  <view class="tabs">
    <view
      v-for="item in tabs"
      :key="item.key"
      class="tabs__item"
      :class="{ on: item.key === modelValue }"
      @click="$emit('update:modelValue', item.key)"
    >
      {{ item.name }}
    </view>
  </view>
</template>

<script setup>
defineProps({
  tabs: { type: Array, default: () => [] },
  modelValue: { type: String, default: '' }
})

defineEmits(['update:modelValue'])
</script>

<style scoped>
.tabs {
  display: flex;
  gap: 8px;
}

.tabs__item {
  flex: 1;
  height: 38px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #c9d2f5;
  font-size: 14px;
  font-weight: 700;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(168, 186, 255, 0.16);
}

.tabs__item.on {
  color: #fff;
  border-color: transparent;
  background: linear-gradient(135deg, #6d7cff, #b14bff);
}
</style>
