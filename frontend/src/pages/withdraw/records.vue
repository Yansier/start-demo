<template>
  <PageShell :show-tab="false">
    <AppNavBar title="提现记录" />
    <view class="head">
      <text class="head__title">提现记录</text>
    </view>
    <LoginRequired>
      <RecordCard
        v-for="item in walletStore.withdrawals"
        :key="item.id"
        :title="item.channel"
        :amount-text="`-${formatAmount(item.amount)}`"
        :status="item.status"
        :time="item.time"
        :extra="extra(item)"
        :tone="tone(item.status)"
      />
      <text v-if="!walletStore.withdrawals.length" class="empty">暂无提现记录</text>
    </LoginRequired>
  </PageShell>
</template>

<script setup>
import { watch } from 'vue'
import PageShell from '@/components/layout/PageShell.vue'
import AppNavBar from '@/components/layout/AppNavBar.vue'
import LoginRequired from '@/components/auth/LoginRequired.vue'
import RecordCard from '@/components/wallet/RecordCard.vue'
import { useUserStore } from '@/stores/user'
import { useWalletStore } from '@/stores/wallet'
import { formatAmount, shortText } from '@/utils/format'

const userStore = useUserStore()
const walletStore = useWalletStore()

watch(() => userStore.profile?.uid || '', (uid) => {
  walletStore.bind(uid)
}, { immediate: true })

function extra(item) {
  const parts = [`手续费 ${formatAmount(item.fee)}`]
  if (item.channel === 'USDT' && item.address) parts.push(`地址 ${shortText(item.address)}`)
  return parts.join('  ')
}

function tone(status) {
  if (status === '已完成') return 'ok'
  if (status === '审核中') return 'wait'
  return 'bad'
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
