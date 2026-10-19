<template>
  <view class="panel glass-card">
    <text class="panel__title">X钱包充值</text>
    <text class="panel__desc">首次使用 X 钱包充值，请联系客服办理和确认账户信息。</text>
    <view class="btn-primary" @click="uiStore.openService()">联系充值客服</view>
    <text class="panel__note">办理完成后可通过该渠道进行充值。</text>
  </view>
</template>

<script setup>
import { useUiStore } from '@/stores/ui'

const uiStore = useUiStore()
</script>

<style scoped>
.panel {
  margin-top: 14px;
  padding: 18px 16px;
}

.panel__title,
.panel__desc,
.panel__note {
  display: block;
}

.panel__title {
  font-size: 20px;
  font-weight: 800;
}

.panel__desc,
.panel__note {
  margin-top: 10px;
  color: var(--muted);
  line-height: 1.6;
}

.btn-primary {
  margin-top: 16px;
}
</style>
