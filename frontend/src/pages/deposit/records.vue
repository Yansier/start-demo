<template>
  <PageShell :show-tab="false">
    <AppNavBar title="充值记录" />
    <view class="head">
      <text class="head__title">充值记录</text>
    </view>
    <LoginRequired>
      <text v-if="loading" class="empty">正在加载</text>
      <template v-else>
        <RecordCard
          v-for="item in walletStore.deposits"
          :key="item.orderNo"
          :title="toDisplayChannel(item.channel)"
          :amount-text="amountText(item)"
          :status="depositStatusLabel(item.status)"
          :time="formatServerTime(item.createdAt)"
          :extra="extra(item)"
          :tone="depositStatusTone(item.status)"
        />
        <text v-if="!walletStore.deposits.length" class="empty">暂无充值记录</text>
      </template>
    </LoginRequired>
  </PageShell>
</template>

<script setup>
import { ref, watch } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import PageShell from '@/components/layout/PageShell.vue'
import AppNavBar from '@/components/layout/AppNavBar.vue'
import LoginRequired from '@/components/auth/LoginRequired.vue'
import RecordCard from '@/components/wallet/RecordCard.vue'
import { depositStatusLabel, depositStatusTone, toDisplayChannel } from '@/api/payment-server'
import { useUserStore } from '@/stores/user'
import { useWalletStore } from '@/stores/wallet'
import { formatAmount, formatServerTime } from '@/utils/format'
import { showToast } from '@/utils/toast'

const userStore = useUserStore()
const walletStore = useWalletStore()
const loading = ref(false)

watch(() => userStore.profile?.uid || '', (uid) => {
  walletStore.bind(uid)
}, { immediate: true })

onShow(() => {
  load()
})

async function load() {
  if (!userStore.isLogin || !userStore.profile) return
  loading.value = true
  try {
    await walletStore.ensureServerWallet(userStore.profile)
    await walletStore.loadDeposits()
  } catch (error) {
    showToast(error.message || '充值记录加载失败')
  } finally {
    loading.value = false
  }
}

function amountText(item) {
  const amount = formatAmount(item.amount)
  return toDisplayChannel(item.channel) === 'USDT' ? `${amount} USDT` : amount
}

function extra(item) {
  const parts = [`订单号 ${item.orderNo}`]
  if (item.reviewedAt) parts.push(`审核 ${formatServerTime(item.reviewedAt)}`)
  return parts.join('  ')
}
</script>

<style scoped>
.head__title {
  display: block;
  margin-bottom: 4px;
  font-size: 24px;
  font-weight: 800;
}

.empty {
  display: block;
  margin-top: 24px;
  color: var(--muted);
  text-align: center;
}
</style>
