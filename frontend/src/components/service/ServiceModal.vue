<template>
  <BaseModal :model-value="uiStore.serviceOpen" title="24小时在线客服" @update:model-value="onVisible">
    <view v-for="item in SERVICE_CHANNELS" :key="item.key" class="channel">
      <view class="channel__main">
        <text class="channel__name">{{ item.name }}</text>
        <text class="channel__desc">{{ item.desc }}</text>
      </view>
      <view class="channel__btn" @click="consult(item.url)">点击咨询</view>
    </view>
  </BaseModal>
</template>

<script setup>
import BaseModal from '@/components/modal/BaseModal.vue'
import { SERVICE_CHANNELS } from '@/config/app'
import { useUiStore } from '@/stores/ui'
import { openLink } from '@/utils/link'

const uiStore = useUiStore()

function onVisible(value) {
  if (!value) uiStore.closeService()
}

function consult(url) {
  openLink(url)
}
</script>

<style scoped>
.channel {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 10px;
  padding: 12px;
  border-radius: 16px;
  background: rgba(8, 12, 32, 0.35);
  border: 1px solid rgba(168, 186, 255, 0.14);
}

.channel__main {
  flex: 1;
  min-width: 0;
}

.channel__name,
.channel__desc {
  display: block;
}

.channel__name {
  font-size: 15px;
  font-weight: 700;
}

.channel__desc {
  margin-top: 4px;
  color: var(--muted);
  font-size: 12px;
}

.channel__btn {
  flex: none;
  height: 32px;
  padding: 0 12px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  color: #fff;
  font-size: 12px;
  font-weight: 700;
  background: linear-gradient(135deg, #6d7cff, #b14bff);
}
</style>
