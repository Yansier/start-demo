<template>
  <view class="game" @click="$emit('select', game)">
    <view class="game__cover" :style="{ background: game.cover }">
      <text class="game__short">{{ game.providerShort }}</text>
    </view>
    <text class="game__name">{{ game.name }}</text>
    <text class="game__provider">{{ game.provider }}</text>
  </view>
</template>

<script setup>
defineProps({
  game: { type: Object, required: true }
})

defineEmits(['select'])
</script>

<style scoped>
.game {
  min-width: 0;
}

.game__cover {
  height: 104px;
  border-radius: 16px;
  position: relative;
  overflow: hidden;
  display: flex;
  align-items: flex-end;
  padding: 8px;
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

.game__cover::after {
  content: '';
  position: absolute;
  width: 70px;
  height: 70px;
  right: -16px;
  top: -18px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.16);
}

.game__short {
  position: relative;
  z-index: 1;
  font-size: 18px;
  font-weight: 800;
  letter-spacing: 0.5px;
}

.game__name,
.game__provider {
  display: block;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.game__name {
  margin-top: 8px;
  font-size: 13px;
  font-weight: 700;
}

.game__provider {
  margin-top: 2px;
  color: var(--faint);
  font-size: 11px;
}
</style>
