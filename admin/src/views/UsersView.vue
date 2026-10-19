<template>
  <h1 class="page-title">游戏钱包用户</h1>
  <DataState
    :loading="store.loading"
    :error="store.error"
    :empty="!store.users.length"
    empty-text="还没有游戏钱包用户"
    @retry="store.loadUsers()"
  >
    <div class="panel table-wrap">
      <table>
        <thead>
          <tr>
            <th>UID</th>
            <th>用户名</th>
            <th>余额</th>
            <th>创建时间</th>
            <th>更新时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in store.users" :key="item.uid">
            <td>{{ item.uid }}</td>
            <td>{{ item.username }}</td>
            <td>{{ money(item.balance) }}</td>
            <td>{{ timeText(item.createdAt) }}</td>
            <td>{{ timeText(item.updatedAt) }}</td>
            <td><RouterLink class="link" :to="`/users/${item.uid}`">查看</RouterLink></td>
          </tr>
        </tbody>
      </table>
    </div>
  </DataState>
</template>

<script setup>
import { onMounted } from 'vue'
import DataState from '@/components/DataState.vue'
import { useAdminStore } from '@/stores/admin'
import { money, timeText } from '@/utils/format'

const store = useAdminStore()

onMounted(() => {
  store.loadUsers()
})
</script>
