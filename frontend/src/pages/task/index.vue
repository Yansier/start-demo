<template>
  <PageShell :show-tab="false">
    <AppNavBar title="任务中心" />
    <view class="head">
      <text class="head__title">任务中心</text>
      <text class="head__desc">完成任务赢取更多奖励</text>
    </view>
    <LoginRequired>
      <TaskTabs v-model="current" :tabs="taskTypes" />
      <TaskCard
        v-for="item in visibleTasks"
        :key="item.id"
        :task="item"
        @claim="onClaim"
        @go="onGo"
      />
    </LoginRequired>
  </PageShell>
</template>

<script setup>
import { computed, ref } from 'vue'
import PageShell from '@/components/layout/PageShell.vue'
import AppNavBar from '@/components/layout/AppNavBar.vue'
import LoginRequired from '@/components/auth/LoginRequired.vue'
import TaskTabs from '@/components/task/TaskTabs.vue'
import TaskCard from '@/components/task/TaskCard.vue'
import { taskTypes } from '@/mock/tasks'
import { useTaskStore } from '@/stores/task'
import { showToast } from '@/utils/toast'

const taskStore = useTaskStore()
taskStore.hydrate()

const current = ref(taskTypes[0].key)
const visibleTasks = computed(() => taskStore.list.filter((item) => item.type === current.value))

function onClaim(task) {
  const ok = taskStore.claim(task.id)
  if (ok) showToast('领取成功')
}

function onGo() {
  showToast('任务尚未完成')
}
</script>

<style scoped>
.head__title,
.head__desc {
  display: block;
}

.head__title {
  font-size: 24px;
  font-weight: 800;
}

.head__desc {
  margin-top: 6px;
  margin-bottom: 14px;
  color: var(--muted);
}
</style>
