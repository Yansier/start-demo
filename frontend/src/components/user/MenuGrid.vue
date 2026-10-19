<template>
  <view class="menu glass-card">
    <view v-for="item in items" :key="item.key" class="menu__item" @click="$emit('select', item)">
      <view class="menu__icon">
        <SvgIcon :name="item.icon" :size="20" />
      </view>
      <text class="menu__name">{{ item.name }}</text>
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
.menu {
  margin-top: 14px;
  padding: 8px 6px 12px;
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  row-gap: 14px;
}

.menu__item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.menu__icon {
  width: 42px;
  height: 42px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(109, 124, 255, 0.12);
  border: 1px solid rgba(168, 186, 255, 0.16);
}

.menu__name {
  font-size: 12px;
  color: #e6ebff;
}
</style>
