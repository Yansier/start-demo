<template>
  <PageShell :show-tab="false">
    <AppNavBar title="充值" />
    <view class="head">
      <text class="head__title">充值</text>
      <text class="head__link" @click="openPage(ROUTES.depositRecords)">充值记录</text>
    </view>
    <LoginRequired>
      <WalletTabs v-model="current" :tabs="tabs" />
      <UsdtDeposit v-if="current === 'usdt'" />
      <ChannelDeposit v-else />
    </LoginRequired>
  </PageShell>
</template>

<script setup>
import { ref } from 'vue'
import PageShell from '@/components/layout/PageShell.vue'
import AppNavBar from '@/components/layout/AppNavBar.vue'
import LoginRequired from '@/components/auth/LoginRequired.vue'
import WalletTabs from '@/components/wallet/WalletTabs.vue'
import UsdtDeposit from '@/components/deposit/UsdtDeposit.vue'
import ChannelDeposit from '@/components/deposit/ChannelDeposit.vue'
import { ROUTES } from '@/config/routes'
import { openPage } from '@/utils/nav'

const tabs = [
  { key: 'usdt', name: 'USDT' },
  { key: 'channel', name: 'X钱包' }
]
const current = ref('usdt')
</script>

<style scoped>
.head {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  margin-bottom: 14px;
}

.head__title {
  font-size: 24px;
  font-weight: 800;
}

.head__link {
  color: var(--cyan);
  font-size: 13px;
  font-weight: 700;
}
</style>
