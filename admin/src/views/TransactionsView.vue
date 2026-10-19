<template>
  <h1 class="page-title">游戏流水</h1>
  <form class="filters" @submit.prevent="search">
    <label>
      UID
      <input v-model.trim="uid" placeholder="可选" />
    </label>
    <label>
      Type
      <select v-model="type">
        <option value="">全部</option>
        <option value="BET">BET</option>
        <option value="SETTLE">SETTLE</option>
        <option value="ROLLBACK">ROLLBACK</option>
      </select>
    </label>
    <button class="btn" type="submit">查询</button>
    <button class="btn ghost" type="button" @click="reset">重置</button>
  </form>
  <DataState
    :loading="store.loading"
    :error="store.error"
    :empty="!store.transactions.length"
    empty-text="暂无流水"
    @retry="search"
  >
    <div class="panel table-wrap">
      <table>
        <thead>
          <tr>
            <th>Type</th>
            <th>UID</th>
            <th>Amount</th>
            <th>Balance Before</th>
            <th>Balance After</th>
            <th>Session</th>
            <th>Transaction ID</th>
            <th>Related ID</th>
            <th>Time</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in store.transactions" :key="item.transactionId">
            <td><span class="tag" :class="item.type">{{ item.type }}</span></td>
            <td>{{ item.uid }}</td>
            <td>{{ signedMoney(item.amount) }}</td>
            <td>{{ money(item.balanceBefore) }}</td>
            <td>{{ money(item.balanceAfter) }}</td>
            <td>{{ shortId(item.sessionId) }}</td>
            <td>{{ shortId(item.transactionId) }}</td>
            <td>{{ shortId(item.relatedTransactionId) }}</td>
            <td>{{ timeText(item.createdAt) }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </DataState>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import DataState from '@/components/DataState.vue'
import { useAdminStore } from '@/stores/admin'
import { money, shortId, signedMoney, timeText } from '@/utils/format'

const store = useAdminStore()
const uid = ref('')
const type = ref('')

function search() {
  store.loadTransactions({
    uid: uid.value,
    type: type.value,
    limit: 100
  })
}

function reset() {
  uid.value = ''
  type.value = ''
  search()
}

onMounted(search)
</script>
