<template>
  <view v-if="!userStore.isLogin" class="gate glass-card">
    <text class="gate__title">请先登录后查看</text>
    <text class="gate__desc">登录后可查看当前页面的演示数据</text>
    <view class="gate__actions">
      <view class="ghost" @click="openPage(ROUTES.login)">登录</view>
      <view class="btn-mini" @click="openPage(ROUTES.register)">注册</view>
    </view>
  </view>
  <slot v-else />
</template>

<script setup>
import { ROUTES } from '@/config/routes'
import { useUserStore } from '@/stores/user'
import { openPage } from '@/utils/nav'

const userStore = useUserStore()
</script>

<style scoped>
.gate {
  padding: 28px 18px 20px;
  text-align: center;
}

.gate__title,
.gate__desc {
  display: block;
}

.gate__title {
  font-size: 18px;
  font-weight: 800;
}

.gate__desc {
  margin-top: 8px;
  color: var(--muted);
  font-size: 13px;
}

.gate__actions {
  display: flex;
  gap: 10px;
  margin-top: 18px;
}

.ghost,
.gate__actions .btn-mini {
  flex: 1;
  height: 40px;
  border-radius: 999px;
}

.ghost {
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  border: 1px solid rgba(168, 186, 255, 0.28);
}
</style>
