<template>
  <BaseModal :model-value="uiStore.downloadOpen" title="如何下载" @update:model-value="onVisible">
    <text class="intro">使用 Safari 将网站添加到主屏幕，下次可以从桌面直接打开。</text>
    <view v-for="step in steps" :key="step.no" class="step">
      <text class="step__no">{{ step.no }}</text>
      <view class="step__body">
        <text class="step__title">{{ step.title }}</text>
        <text class="step__desc">{{ step.desc }}</text>
      </view>
    </view>
  </BaseModal>
</template>

<script setup>
import BaseModal from '@/components/modal/BaseModal.vue'
import { useUiStore } from '@/stores/ui'

const uiStore = useUiStore()

const steps = [
  { no: '1', title: '使用 Safari 打开', desc: '使用 Safari 浏览器打开网站' },
  { no: '2', title: '点击分享', desc: '点击下面工具栏的分享按钮' },
  { no: '3', title: '添加到主屏幕', desc: '选择“添加到主屏幕”' }
]

function onVisible(value) {
  if (!value) uiStore.closeDownload()
}
</script>

<style scoped>
.intro {
  display: block;
  margin-bottom: 12px;
  color: var(--muted);
  font-size: 13px;
  line-height: 1.6;
}

.step {
  display: flex;
  gap: 12px;
  margin-bottom: 10px;
  padding: 12px;
  border-radius: 16px;
  background: rgba(8, 12, 32, 0.4);
  border: 1px solid rgba(120, 170, 255, 0.28);
  box-shadow: inset 0 0 0 1px rgba(92, 239, 255, 0.05);
}

.step__no {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex: none;
  color: #07101f;
  font-weight: 800;
  background: linear-gradient(135deg, #7d8cff, #5cefff);
}

.step__title,
.step__desc {
  display: block;
}

.step__title {
  font-size: 15px;
  font-weight: 700;
}

.step__desc {
  margin-top: 4px;
  color: var(--muted);
  font-size: 13px;
  line-height: 1.5;
}
</style>
