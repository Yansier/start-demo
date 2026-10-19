<template>
  <h1 class="page-title">提现订单</h1>
  <form class="filters" @submit.prevent="search">
    <label>
      UID
      <input v-model.trim="uid" placeholder="可选" />
    </label>
    <label>
      Status
      <select v-model="status">
        <option value="">全部</option>
        <option value="PENDING">PENDING</option>
        <option value="APPROVED">APPROVED</option>
        <option value="REJECTED">REJECTED</option>
      </select>
    </label>
    <button class="btn" type="submit">查询</button>
    <button class="btn ghost" type="button" @click="reset">重置</button>
  </form>
  <p v-if="actionError" class="alert">{{ actionError }}</p>
  <DataState
    :loading="store.loading"
    :error="store.error"
    :empty="!store.withdrawals.length"
    empty-text="暂无提现订单"
    @retry="search"
  >
    <div class="panel table-wrap">
      <table>
        <thead>
          <tr>
            <th>Order</th>
            <th>UID</th>
            <th>Channel</th>
            <th>Amount</th>
            <th>Fee</th>
            <th>Address</th>
            <th>Status</th>
            <th>Created At</th>
            <th>Reviewed At</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in store.withdrawals" :key="item.orderNo">
            <td>{{ item.orderNo }}</td>
            <td>{{ item.uid }}</td>
            <td>{{ item.channel }}</td>
            <td>{{ money(item.amount) }}</td>
            <td>{{ money(item.fee) }}</td>
            <td>{{ item.address || '-' }}</td>
            <td><span class="tag" :class="item.status">{{ item.status }}</span></td>
            <td>{{ timeText(item.createdAt) }}</td>
            <td>{{ timeText(item.reviewedAt) }}</td>
            <td>
              <div v-if="item.status === 'PENDING'" class="actions">
                <button class="btn small" type="button" :disabled="acting" @click="approve(item)">Approve</button>
                <button class="btn small ghost" type="button" :disabled="acting" @click="reject(item)">Reject</button>
              </div>
            </td>
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
import { money, timeText } from '@/utils/format'

const store = useAdminStore()
const uid = ref('')
const status = ref('')
const acting = ref(false)
const actionError = ref('')

function search() {
  actionError.value = ''
  store.loadWithdrawals({ uid: uid.value, status: status.value, limit: 100 })
}

function reset() {
  uid.value = ''
  status.value = ''
  search()
}

async function approve(item) {
  if (acting.value) return
  if (!window.confirm('确认通过该提现订单？')) return
  acting.value = true
  actionError.value = ''
  try {
    await store.approveWithdrawal(item.orderNo)
  } catch (error) {
    actionError.value = error.message || '审核失败'
  } finally {
    acting.value = false
  }
}

async function reject(item) {
  if (acting.value) return
  const remark = window.prompt('请输入拒绝原因', 'Demo reject')
  if (remark === null) return
  acting.value = true
  actionError.value = ''
  try {
    await store.rejectWithdrawal(item.orderNo, remark)
  } catch (error) {
    actionError.value = error.message || '审核失败'
  } finally {
    acting.value = false
  }
}

onMounted(search)
</script>
