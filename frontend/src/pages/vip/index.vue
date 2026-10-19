<template>
  <PageShell :show-tab="false">
    <AppNavBar title="VIP" />
    <view class="head">
      <text class="head__title">VIP 中心</text>
      <text class="head__desc">滑动查看 VIP1 到 VIP12 的演示权益</text>
    </view>
    <LoginRequired>
      <VipOverview :profile="userStore.profile" :level="selectedLevel" :progress="progress" />
      <VipSelector
        :levels="vipLevels"
        :selected="selected"
        :current="currentLevel"
        @select="selected = $event"
      />
      <RebateList :items="selectedLevel.rebate" />
    </LoginRequired>
  </PageShell>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import PageShell from '@/components/layout/PageShell.vue'
import AppNavBar from '@/components/layout/AppNavBar.vue'
import LoginRequired from '@/components/auth/LoginRequired.vue'
import VipOverview from '@/components/vip/VipOverview.vue'
import VipSelector from '@/components/vip/VipSelector.vue'
import RebateList from '@/components/vip/RebateList.vue'
import { DEMO_TURNOVER, getVipLevel, vipLevels } from '@/mock/vip'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const selected = ref(3)

const currentLevel = computed(() => userStore.profile?.vipLevel || 3)
const selectedLevel = computed(() => getVipLevel(selected.value))

const progress = computed(() => {
  const currentVip = getVipLevel(currentLevel.value)
  const next = vipLevels.find((item) => item.level === currentLevel.value + 1)
  const current = DEMO_TURNOVER
  if (!next) {
    return { current, percent: 100, remain: 0, nextLevel: currentLevel.value }
  }
  const span = next.reachTurnover - currentVip.reachTurnover
  const moved = Math.max(0, current - currentVip.reachTurnover)
  return {
    current,
    percent: span ? Math.min(100, Math.round((moved / span) * 100)) : 100,
    remain: Math.max(0, next.reachTurnover - current),
    nextLevel: next.level
  }
})

watch(currentLevel, (value) => {
  selected.value = value
}, { immediate: true })
</script>

<style scoped>
.head__title,
.head__desc {
  display: block;
}

.head__title {
  font-size: 24px;
  font-weight: 800;
}

.head__desc {
  margin-top: 6px;
  margin-bottom: 14px;
  color: var(--muted);
}
</style>
