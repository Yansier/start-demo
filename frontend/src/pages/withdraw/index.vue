<template>
  <PageShell :show-tab="false">
    <AppNavBar title="提现" />
    <view class="head">
      <text class="head__title">提现</text>
      <text class="head__link" @click="openPage(ROUTES.withdrawRecords)">提现记录</text>
    </view>
    <LoginRequired>
      <WalletTabs v-model="current" :tabs="tabs" />
      <UsdtWithdraw v-if="current === 'usdt'" :balance="balance" />
      <ChannelWithdraw v-else :balance="balance" />
    </LoginRequired>
  </PageShell>
</template>

<script setup>
import { computed, ref, watch } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import PageShell from '@/components/layout/PageShell.vue'
import AppNavBar from '@/components/layout/AppNavBar.vue'
import LoginRequired from '@/components/auth/LoginRequired.vue'
import WalletTabs from '@/components/wallet/WalletTabs.vue'
import UsdtWithdraw from '@/components/withdraw/UsdtWithdraw.vue'
import ChannelWithdraw from '@/components/withdraw/ChannelWithdraw.vue'
import { ROUTES } from '@/config/routes'
import { useUserStore } from '@/stores/user'
import { useWalletStore } from '@/stores/wallet'
import { openPage } from '@/utils/nav'
import { showToast } from '@/utils/toast'

const tabs = [
  { key: 'usdt', name: 'USDT' },
  { key: 'channel', name: 'X钱包' }
]
const current = ref('usdt')
const userStore = useUserStore()
const walletStore = useWalletStore()
const balance = computed(() => Number(userStore.profile?.balance) || 0)

watch(() => userStore.profile?.uid || '', (uid) => {
  walletStore.bind(uid)
}, { immediate: true })

onShow(() => {
  if (!userStore.isLogin || !userStore.profile) return
  walletStore.ensureServerWallet(userStore.profile)
    .then(() => walletStore.refreshBalance())
    .catch((error) => {
      showToast(error.message || '余额同步失败')
    })
})
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
