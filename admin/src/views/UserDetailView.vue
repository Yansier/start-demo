<template>
  <h1 class="page-title">用户详情</h1>
  <DataState :loading="store.loading" :error="store.error" @retry="load">
    <template v-if="store.detail">
      <section class="panel meta">
        <span>UID</span><strong>{{ store.detail.wallet.uid }}</strong>
        <span>用户名</span><strong>{{ store.detail.wallet.username }}</strong>
        <span>余额</span><strong>{{ money(store.detail.wallet.balance) }}</strong>
      </section>
      <section class="panel">
        <h2>最近游戏 Session</h2>
        <div v-if="!store.detail.recentSessions.length" class="state">暂无 Session</div>
        <div v-else class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Session</th>
                <th>Game</th>
                <th>Provider</th>
                <th>Status</th>
                <th>Updated</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in store.detail.recentSessions" :key="item.sessionId">
                <td>{{ shortId(item.sessionId) }}</td>
                <td>{{ item.gameCode }}</td>
                <td>{{ item.provider }}</td>
                <td>{{ item.status }}</td>
                <td>{{ timeText(item.updatedAt) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
      <section class="panel">
        <h2>最近游戏流水</h2>
        <div v-if="!store.detail.recentTransactions.length" class="state">暂无流水</div>
        <div v-else class="table-wrap">
          <table>
            <thead>
              <tr>
                <th>Type</th>
                <th>Amount</th>
                <th>Before</th>
                <th>After</th>
                <th>Transaction</th>
                <th>Time</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in store.detail.recentTransactions" :key="item.transactionId">
                <td><span class="tag" :class="item.type">{{ item.type }}</span></td>
                <td>{{ signedMoney(item.amount) }}</td>
                <td>{{ money(item.balanceBefore) }}</td>
                <td>{{ money(item.balanceAfter) }}</td>
                <td>{{ shortId(item.transactionId) }}</td>
                <td>{{ timeText(item.createdAt) }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </section>
    </template>
  </DataState>
</template>

<script setup>
import { onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import DataState from '@/components/DataState.vue'
import { useAdminStore } from '@/stores/admin'
import { money, shortId, signedMoney, timeText } from '@/utils/format'

const route = useRoute()
const store = useAdminStore()

function load() {
  store.loadDetail(route.params.uid)
}

onMounted(load)
watch(() => route.params.uid, load)
</script>
