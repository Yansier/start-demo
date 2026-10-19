<template>
  <view class="tabbar">
    <view
      v-for="item in tabs"
      :key="item.key"
      class="tab"
      :class="{ on: item.path && current === item.path.slice(1) }"
      @click="onTab(item)"
    >
      <view class="tab__icon">
        <SvgIcon :name="item.icon" :size="20" />
      </view>
      <text class="tab__text">{{ item.text }}</text>
    </view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import SvgIcon from '@/components/common/SvgIcon.vue'
import { ROUTES } from '@/config/routes'
import { runMenuAction } from '@/utils/action'
import { getCurrentRoute, switchTab } from '@/utils/nav'

const current = ref('')

const tabs = [
  { key: 'home', text: '首页', icon: 'home', path: ROUTES.home },
  { key: 'activity', text: '活动', icon: 'activity', path: ROUTES.activity },
  { key: 'service', text: '客服', icon: 'service', action: 'service' },
  { key: 'download', text: '下载', icon: 'download', action: 'download' },
  { key: 'user', text: '我的', icon: 'user', path: ROUTES.user }
]

function syncRoute() {
  current.value = getCurrentRoute()
}

onShow(syncRoute)
syncRoute()

function onTab(item) {
  if (item.action) {
    runMenuAction(item.action)
    return
  }
  switchTab(item.path)
}
</script>

<style scoped>
.tabbar {
  position: fixed;
  z-index: 40;
  left: 50%;
  bottom: 0;
  transform: translateX(-50%);
  width: 100%;
  max-width: 430px;
  display: flex;
  padding: 8px 4px calc(8px + env(safe-area-inset-bottom));
  background: rgba(10, 13, 30, 0.9);
  border-top: 1px solid rgba(168, 186, 255, 0.16);
  backdrop-filter: blur(18px);
  box-shadow: 0 -8px 24px rgba(0, 0, 0, 0.25);
}

.tab {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 3px;
  color: #8d98bb;
}

.tab__icon {
  width: 34px;
  height: 28px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.tab.on {
  color: #f4f7ff;
}

.tab.on .tab__icon {
  background: linear-gradient(135deg, rgba(109, 124, 255, 0.38), rgba(177, 75, 255, 0.32));
  box-shadow: 0 0 14px rgba(110, 140, 255, 0.28);
}

.tab__text {
  font-size: 11px;
  line-height: 1.2;
}
</style>
