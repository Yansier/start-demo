<template>
  <PageShell :show-tab="false">
    <AppNavBar title="充值记录" />
    <view class="head">
      <text class="head__title">充值记录</text>
    </view>
    <LoginRequired>
      <RecordCard
        v-for="item in walletStore.deposits"
        :key="item.id"
        :title="item.channel"
        :amount-text="amountText(item)"
        :status="item.status"
        :time="item.time"
        :extra="item.txid ? `Txid ${shortText(item.txid)}` : ''"
        :tone="tone(item.status)"
      />
      <text v-if="!walletStore.deposits.length" class="empty">暂无充值记录</text>
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

function amountText(item) {
  const amount = formatAmount(item.amount)
  return item.channel === 'USDT' ? `${amount} USDT` : amount
}

function tone(status) {
  if (status === '已到账') return 'ok'
  if (status === '处理中') return 'wait'
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
