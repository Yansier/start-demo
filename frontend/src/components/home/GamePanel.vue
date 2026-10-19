<template>
  <view class="panel">
    <view class="panel__head">
      <text class="panel__title">游戏大厅</text>
      <text class="panel__count">{{ visibleGames.length }} 款</text>
    </view>
    <scroll-view class="cats" scroll-x :show-scrollbar="false">
      <view class="cats__row">
        <view
          v-for="item in categories"
          :key="item.key"
          class="cat"
          :class="{ on: item.key === current }"
          @click="current = item.key"
        >
          {{ item.name }}
        </view>
      </view>
    </scroll-view>
    <view class="grid">
      <GameCard v-for="game in visibleGames" :key="game.id" :game="game" @select="$emit('select', $event)" />
    </view>
  </view>
</template>

<script setup>
import { computed, ref } from 'vue'
import GameCard from '@/components/home/GameCard.vue'

const props = defineProps({
  categories: { type: Array, default: () => [] },
  games: { type: Array, default: () => [] }
})

defineEmits(['select'])

const current = ref('hot')

const visibleGames = computed(() => {
  if (current.value === 'hot') return props.games.filter((item) => item.hot)
  return props.games.filter((item) => item.category === current.value)
})
</script>

<style scoped>
.panel {
  margin-top: 16px;
}

.panel__head {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
}

.panel__title {
  font-size: 18px;
  font-weight: 800;
}

.panel__count {
  color: var(--faint);
  font-size: 12px;
}

.cats {
  width: 100%;
  height: 36px;
  margin-top: 12px;
  white-space: nowrap;
}

.cats__row {
  display: inline-flex;
  gap: 8px;
  padding-bottom: 2px;
}

.cat {
  height: 32px;
  padding: 0 14px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  color: #c9d2f5;
  font-size: 13px;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(168, 186, 255, 0.14);
}

.cat.on {
  color: #fff;
  border-color: transparent;
  background: linear-gradient(135deg, #6d7cff, #b14bff);
  box-shadow: 0 6px 14px rgba(112, 78, 255, 0.28);
}

.grid {
  margin-top: 12px;
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 12px 10px;
}
</style>
