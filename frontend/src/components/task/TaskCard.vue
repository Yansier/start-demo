<template>
  <view class="task glass-card">
    <view class="task__top">
      <text class="task__title">{{ task.title }}</text>
      <text class="task__reward">{{ task.reward }}</text>
    </view>
    <text class="task__desc">{{ task.description }}</text>
    <view class="bar">
      <view class="bar__fill" :style="{ width: `${percent}%` }" />
    </view>
    <view class="task__foot">
      <text class="task__progress">{{ task.current }}/{{ task.target }}</text>
      <view class="task__btn" :class="task.status" @click="onClick">{{ label }}</view>
    </view>
  </view>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  task: { type: Object, required: true }
})

const emit = defineEmits(['claim', 'go'])

const percent = computed(() => {
  const target = Number(props.task.target) || 0
  if (!target) return 0
  return Math.min(100, Math.round((Number(props.task.current) / target) * 100))
})

const label = computed(() => {
  if (props.task.status === 'claimed') return '已领取'
  if (props.task.status === 'claimable') return '领取'
  return '去完成'
})

function onClick() {
  if (props.task.status === 'claimable') emit('claim', props.task)
  if (props.task.status === 'pending') emit('go', props.task)
}
</script>

<style scoped>
.task {
  margin-top: 12px;
  padding: 14px;
}

.task__top,
.task__foot {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
}

.task__title {
  font-size: 16px;
  font-weight: 800;
}

.task__reward {
  flex: none;
  color: var(--gold);
  font-size: 13px;
  font-weight: 700;
}

.task__desc,
.task__progress {
  display: block;
}

.task__desc {
  margin-top: 6px;
  color: var(--muted);
  font-size: 13px;
}

.bar {
  height: 6px;
  margin-top: 12px;
  border-radius: 999px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.08);
}

.bar__fill {
  height: 100%;
  border-radius: inherit;
  background: linear-gradient(90deg, #6d7cff, #5cefff);
}

.task__foot {
  margin-top: 12px;
}

.task__progress {
  color: var(--faint);
  font-size: 12px;
}

.task__btn {
  min-width: 76px;
  height: 32px;
  padding: 0 12px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
}

.task__btn.pending {
  border: 1px solid rgba(168, 186, 255, 0.28);
}

.task__btn.claimable {
  background: linear-gradient(135deg, #6d7cff, #b14bff);
}

.task__btn.claimed {
  color: var(--faint);
  background: rgba(255, 255, 255, 0.05);
}
</style>
