<template>
  <view class="hero glass-card">
    <view class="hero__user">
      <view class="avatar">{{ avatarText(profile.username) }}</view>
      <view class="hero__meta">
        <view class="hero__row">
          <text class="hero__name">{{ profile.username }}</text>
          <text class="vip">VIP{{ profile.vipLevel }}</text>
        </view>
        <text class="hero__uid">UID {{ profile.uid }}</text>
      </view>
    </view>
    <view class="progress">
      <view class="progress__top">
        <text>当前 VIP{{ profile.vipLevel }} 升级进度</text>
        <text>{{ progress.percent }}%</text>
      </view>
      <view class="bar">
        <view class="bar__fill" :style="{ width: `${progress.percent}%` }" />
      </view>
      <text class="progress__hint">
        累计流水 {{ formatAmount(progress.current) }}，距离 VIP{{ progress.nextLevel }} 还需 {{ formatAmount(progress.remain) }}
      </text>
    </view>
    <view class="stats">
      <view v-for="item in stats" :key="item.label" class="stat">
        <text class="stat__label">{{ item.label }}</text>
        <text class="stat__value">{{ item.value }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { computed } from 'vue'
import { avatarText, formatAmount } from '@/utils/format'

const props = defineProps({
  profile: { type: Object, required: true },
  level: { type: Object, required: true },
  progress: { type: Object, required: true }
})

const stats = computed(() => [
  { label: '升级所需流水', value: props.level.upgradeTurnover ? formatAmount(props.level.upgradeTurnover) : '已满级' },
  { label: '累计流水', value: formatAmount(props.level.turnover) },
  { label: '周有效投注', value: formatAmount(props.level.weeklyValidBet) },
  { label: '周礼金', value: formatAmount(props.level.weeklyGift) },
  { label: '充值要求', value: formatAmount(props.level.depositRequirement) }
])
</script>

<style scoped>
.hero {
  padding: 16px;
  background:
    radial-gradient(90% 80% at 100% 0%, rgba(246, 197, 107, 0.16), transparent 46%),
    linear-gradient(180deg, rgba(255, 255, 255, 0.09), rgba(255, 255, 255, 0.035));
}

.hero__user,
.hero__row,
.progress__top {
  display: flex;
  align-items: center;
}

.hero__user {
  gap: 12px;
}

.avatar {
  width: 52px;
  height: 52px;
  border-radius: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 22px;
  font-weight: 800;
  background: linear-gradient(145deg, #7a8cff, #b14bff 70%, #5cefff);
}

.hero__meta {
  flex: 1;
  min-width: 0;
}

.hero__row {
  gap: 8px;
}

.hero__name,
.hero__uid,
.progress__hint,
.stat__label,
.stat__value {
  display: block;
}

.hero__name {
  font-size: 18px;
  font-weight: 800;
}

.hero__uid,
.stat__label,
.progress__hint {
  margin-top: 4px;
  color: var(--muted);
  font-size: 12px;
}

.vip {
  height: 20px;
  padding: 0 8px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  color: #3a2504;
  font-size: 11px;
  font-weight: 800;
  background: linear-gradient(135deg, #ffe7a3, #f6c56b);
}

.progress {
  margin-top: 16px;
}

.progress__top {
  justify-content: space-between;
  font-size: 13px;
}

.bar {
  height: 8px;
  margin-top: 8px;
  border-radius: 999px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.08);
}

.bar__fill {
  height: 100%;
  border-radius: inherit;
  background: linear-gradient(90deg, #f6c56b, #8d4dff);
}

.stats {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 8px;
  margin-top: 14px;
}

.stat {
  padding: 10px;
  border-radius: 14px;
  background: rgba(6, 10, 28, 0.35);
  border: 1px solid rgba(168, 186, 255, 0.12);
}

.stat:last-child {
  grid-column: 1 / -1;
}

.stat__value {
  margin-top: 4px;
  color: var(--gold);
  font-size: 16px;
  font-weight: 800;
}
</style>
