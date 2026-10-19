<template>
  <scroll-view class="levels" scroll-x :show-scrollbar="false" :scroll-into-view="`vip-level-${selected}`">
    <view class="levels__row">
      <view
        v-for="item in levels"
        :key="item.level"
        :id="`vip-level-${item.level}`"
        class="level"
        :class="{ on: item.level === selected, current: item.level === current }"
        @click="$emit('select', item.level)"
      >
        <text class="level__name">VIP{{ item.level }}</text>
        <text class="level__mark">{{ item.level === current ? '当前' : '查看' }}</text>
      </view>
    </view>
  </scroll-view>
</template>

<script setup>
defineProps({
  levels: { type: Array, default: () => [] },
  selected: { type: Number, default: 3 },
  current: { type: Number, default: 3 }
})

defineEmits(['select'])
</script>

<style scoped>
.levels {
  width: 100%;
  height: 74px;
  margin-top: 14px;
  white-space: nowrap;
}

.levels__row {
  display: inline-flex;
  gap: 8px;
  padding-bottom: 2px;
}

.level {
  width: 76px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(168, 186, 255, 0.16);
}

.level__name,
.level__mark {
  display: block;
}

.level__name {
  font-size: 14px;
  font-weight: 800;
}

.level__mark {
  margin-top: 4px;
  color: var(--faint);
  font-size: 11px;
}

.level.current {
  border-color: rgba(246, 197, 107, 0.7);
}

.level.on {
  border-color: transparent;
  background: linear-gradient(160deg, #6d7cff, #b14bff);
  box-shadow: 0 8px 16px rgba(112, 78, 255, 0.28);
}

.level.on .level__mark {
  color: rgba(255, 255, 255, 0.82);
}
</style>
