<template>
  <h1 class="page-title">Dashboard</h1>
  <DataState :loading="store.loading" :error="store.error" @retry="store.loadDashboard()">
    <section v-if="store.dashboard" class="cards">
      <article class="card"><em>游戏钱包用户</em><strong>{{ store.dashboard.walletUsers }}</strong></article>
      <article class="card"><em>钱包总余额</em><strong>{{ money(store.dashboard.totalBalance) }}</strong></article>
      <article class="card"><em>活跃 Session</em><strong>{{ store.dashboard.activeSessions }}</strong></article>
      <article class="card"><em>今日 BET 金额</em><strong>{{ money(store.dashboard.todayBetAmount) }}</strong></article>
      <article class="card"><em>今日派奖金额</em><strong>{{ money(store.dashboard.todaySettleAmount) }}</strong></article>
      <article class="card"><em>今日 ROLLBACK</em><strong>{{ store.dashboard.todayRollbackCount }}</strong></article>
      <article class="card"><em>今日流水</em><strong>{{ store.dashboard.todayTransactionCount }}</strong></article>
      <article class="card"><em>待审核充值</em><strong>{{ store.dashboard.pendingDeposits }}</strong></article>
      <article class="card"><em>待审核提现</em><strong>{{ store.dashboard.pendingWithdrawals }}</strong></article>
    </section>
    <section class="panel">
      <h2>最近游戏流水</h2>
      <div v-if="!store.recentTransactions.length" class="state">暂无流水</div>
      <div v-else class="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Type</th>
              <th>UID</th>
              <th>Amount</th>
              <th>Balance After</th>
              <th>Time</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in store.recentTransactions" :key="item.transactionId">
              <td><span class="tag" :class="item.type">{{ item.type }}</span></td>
              <td>{{ item.uid }}</td>
              <td>{{ signedMoney(item.amount) }}</td>
              <td>{{ money(item.balanceAfter) }}</td>
              <td>{{ timeText(item.createdAt) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </section>
  </DataState>
</template>

<script setup>
import { onMounted } from 'vue'
import DataState from '@/components/DataState.vue'
import { useAdminStore } from '@/stores/admin'
import { money, signedMoney, timeText } from '@/utils/format'

const store = useAdminStore()

onMounted(() => {
  store.loadDashboard()
})
</script>
