<template>
  <view class="quick glass-card">
    <view v-for="item in items" :key="item.key" class="quick__item" @click="$emit('select', item)">
      <view class="quick__icon">
        <SvgIcon :name="item.icon" :size="22" />
      </view>
      <text class="quick__name">{{ item.name }}</text>
    </view>
  </view>
</template>

<script setup>
import SvgIcon from '@/components/common/SvgIcon.vue'

defineProps({
  items: { type: Array, default: () => [] }
})

defineEmits(['select'])
</script>

<style scoped>
.quick {
  margin-top: 14px;
  padding: 14px 8px 12px;
  display: flex;
}

.quick__item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.quick__icon {
  width: 46px;
  height: 46px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(160deg, rgba(109, 124, 255, 0.28), rgba(92, 239, 255, 0.08));
  border: 1px solid rgba(168, 186, 255, 0.2);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.16);
}

.quick__name {
  font-size: 12px;
  color: #e7ecff;
}
</style>
