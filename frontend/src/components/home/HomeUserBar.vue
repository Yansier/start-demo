<template>
  <view class="userbar">
    <LogoMark :size="44" />
    <view v-if="isLogin" class="userbar__meta">
      <text class="userbar__name">{{ profile.username }}</text>
      <text class="userbar__uid">UID {{ profile.uid }}</text>
    </view>
    <view v-else class="userbar__meta">
      <text class="userbar__name">{{ APP_NAME }}</text>
      <text class="userbar__uid">欢迎来到娱乐平台</text>
    </view>
    <view v-if="isLogin" class="userbar__side">
      <text class="userbar__label">余额</text>
      <text class="amount userbar__balance">{{ formatAmount(profile.balance) }}</text>
      <view class="btn-mini" @click="$emit('deposit')">充值</view>
    </view>
    <view v-else class="userbar__actions">
      <view class="ghost" @click="$emit('login')">登录</view>
      <view class="btn-mini" @click="$emit('register')">注册</view>
    </view>
  </view>
</template>

<script setup>
import LogoMark from '@/components/common/LogoMark.vue'
import { APP_NAME } from '@/config/app'
import { formatAmount } from '@/utils/format'

defineProps({
  isLogin: { type: Boolean, default: false },
  profile: { type: Object, default: null }
})

defineEmits(['login', 'register', 'deposit'])
</script>

<style scoped>
.userbar {
  display: flex;
  align-items: center;
  gap: 10px;
}

.userbar__meta {
  flex: 1;
  min-width: 0;
}

.userbar__name,
.userbar__uid,
.userbar__label,
.userbar__balance {
  display: block;
}

.userbar__name {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-size: 16px;
  font-weight: 800;
}

.userbar__uid,
.userbar__label {
  margin-top: 3px;
  color: var(--muted);
  font-size: 12px;
}

.userbar__side {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 2px;
}

.userbar__balance {
  margin: 1px 0 4px;
  font-size: 16px;
}

.userbar__actions {
  display: flex;
  align-items: center;
  gap: 8px;
}

.ghost {
  height: 32px;
  padding: 0 14px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  color: #fff;
  font-size: 13px;
  font-weight: 700;
  border: 1px solid rgba(168, 186, 255, 0.28);
  background: rgba(255, 255, 255, 0.04);
}
</style>
