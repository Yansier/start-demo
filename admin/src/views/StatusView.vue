<template>
  <h1 class="page-title">系统状态</h1>
  <DataState :loading="store.loading" :error="store.error" @retry="store.loadStatus()">
    <section v-if="store.system" class="panel meta">
      <span>Server</span><strong>{{ store.system.status }}</strong>
      <span>Service</span><strong>{{ store.system.service }}</strong>
      <span>Database</span><strong>{{ store.system.database }}</strong>
      <span>Game Provider</span><strong>{{ store.system.gameProvider }}</strong>
      <span>Version</span><strong>{{ store.system.version }}</strong>
      <span>Server Time</span><strong>{{ timeText(store.system.serverTime) }}</strong>
    </section>
  </DataState>
  <p class="note">当前管理后台仅用于展示游戏平台核心业务数据，暂未开放运营写操作。</p>
</template>

<script setup>
import { onMounted } from 'vue'
import DataState from '@/components/DataState.vue'
import { useAdminStore } from '@/stores/admin'
import { timeText } from '@/utils/format'

const store = useAdminStore()

onMounted(() => {
  store.loadStatus()
})
</script>
