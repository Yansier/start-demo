<template>
  <view class="card glass-card" @click="$emit('select', activity)">
    <view class="card__cover" :style="{ background: activity.gradient }">
      <text class="card__tag">{{ activity.tag }}</text>
      <text class="card__status">{{ activity.status }}</text>
    </view>
    <view class="card__body">
      <text class="card__title">{{ activity.title }}</text>
      <text class="card__desc">{{ activity.subtitle }}</text>
      <text class="card__time">{{ activity.startTime }} ~ {{ activity.endTime }}</text>
      <view class="card__btn">{{ activity.buttonText }}</view>
    </view>
  </view>
</template>

<script setup>
defineProps({
  activity: { type: Object, required: true }
})

defineEmits(['select'])
</script>

<style scoped>
.card {
  margin-top: 12px;
  overflow: hidden;
}

.card__cover {
  height: 92px;
  padding: 14px;
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
}

.card__tag,
.card__status {
  height: 24px;
  padding: 0 8px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  font-size: 12px;
  font-weight: 700;
  background: rgba(5, 8, 24, 0.28);
}

.card__body {
  padding: 14px 14px 16px;
}

.card__title,
.card__desc,
.card__time {
  display: block;
}

.card__title {
  font-size: 17px;
  font-weight: 800;
}

.card__desc {
  margin-top: 6px;
  color: var(--muted);
  font-size: 13px;
  line-height: 1.5;
}

.card__time {
  margin-top: 8px;
  color: var(--faint);
  font-size: 12px;
}

.card__btn {
  height: 36px;
  margin-top: 12px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
  background: linear-gradient(135deg, #6d7cff, #b14bff);
}
</style>
