<template>
  <view class="list glass-card">
    <text class="list__title">最近邀请</text>
    <view v-for="item in items" :key="item.id" class="row">
      <view class="avatar">{{ avatarText(item.name) }}</view>
      <view class="row__main">
        <text class="row__name">{{ item.name }}</text>
        <text class="row__time">{{ item.time }}</text>
      </view>
      <view class="row__side">
        <text class="vip">VIP{{ item.vip }}</text>
        <text class="row__money">+{{ formatAmount(item.commission) }}</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { avatarText, formatAmount } from '@/utils/format'

defineProps({
  items: { type: Array, default: () => [] }
})
</script>

<style scoped>
.list {
  margin-top: 14px;
  padding: 14px;
}

.list__title {
  display: block;
  margin-bottom: 4px;
  font-size: 16px;
  font-weight: 800;
}

.row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 12px;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
  background: rgba(109, 124, 255, 0.2);
}

.row__main {
  flex: 1;
  min-width: 0;
}

.row__name,
.row__time,
.row__money {
  display: block;
}

.row__name {
  font-weight: 700;
}

.row__time,
.row__money {
  margin-top: 3px;
  color: var(--muted);
  font-size: 12px;
}

.row__side {
  text-align: right;
}

.vip {
  height: 18px;
  padding: 0 6px;
  border-radius: 999px;
  display: inline-flex;
  align-items: center;
  color: #3a2504;
  font-size: 11px;
  font-weight: 800;
  background: linear-gradient(135deg, #ffe7a3, #f6c56b);
}

.row__money {
  color: var(--gold);
}
</style>
