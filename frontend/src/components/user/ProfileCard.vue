<template>
  <GlassCard>
    <view v-if="isLogin" class="profile">
      <view class="avatar">{{ avatarText(profile.username) }}</view>
      <view class="profile__main">
        <view class="profile__row">
          <text class="profile__name">{{ profile.username }}</text>
          <text class="vip">VIP{{ profile.vipLevel }}</text>
        </view>
        <text class="profile__uid">UID {{ profile.uid }}</text>
      </view>
      <view class="balance">
        <text class="balance__label">余额</text>
        <text class="amount balance__value">{{ formatAmount(profile.balance) }}</text>
      </view>
    </view>
    <view v-else class="guest">
      <view class="avatar">游</view>
      <view class="guest__main">
        <text class="profile__name">游客状态</text>
        <text class="profile__uid">登录后查看余额与会员等级</text>
      </view>
      <view class="guest__actions">
        <view class="ghost" @click="$emit('login')">登录</view>
        <view class="btn-mini" @click="$emit('register')">注册</view>
      </view>
    </view>
  </GlassCard>
</template>

<script setup>
import GlassCard from '@/components/common/GlassCard.vue'
import { avatarText, formatAmount } from '@/utils/format'

defineProps({
  isLogin: { type: Boolean, default: false },
  profile: { type: Object, default: null }
})

defineEmits(['login', 'register'])
</script>

<style scoped>
.profile,
.guest {
  padding: 16px;
}

.profile {
  display: grid;
  grid-template-columns: auto 1fr;
  column-gap: 12px;
  row-gap: 14px;
}

.guest {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
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
  box-shadow: 0 8px 16px rgba(98, 82, 255, 0.3);
}

.profile__main,
.guest__main {
  min-width: 0;
}

.guest__main {
  flex: 1;
}

.profile__row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.profile__name,
.profile__uid,
.balance__label,
.balance__value {
  display: block;
}

.profile__name {
  font-size: 18px;
  font-weight: 800;
}

.profile__uid,
.balance__label {
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

.balance {
  grid-column: 1 / -1;
  padding: 12px 14px;
  border-radius: 14px;
  background: rgba(6, 10, 28, 0.35);
  border: 1px solid rgba(168, 186, 255, 0.12);
}

.balance__value {
  margin-top: 4px;
  font-size: 26px;
}

.guest__actions {
  width: 100%;
  display: flex;
  gap: 8px;
}

.ghost,
.guest__actions .btn-mini {
  flex: 1;
}

.ghost {
  height: 36px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  font-weight: 700;
  border: 1px solid rgba(168, 186, 255, 0.28);
}
</style>
