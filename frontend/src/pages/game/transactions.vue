<template>
  <PageShell :show-tab="false">
    <AppNavBar title="游戏流水" />
    <view v-if="screen === 'notice'" class="notice glass-card">
      <text class="notice__title">{{ noticeTitle }}</text>
      <text class="notice__text">{{ noticeText }}</text>
      <view class="btn-primary notice__btn" @click="goHome">返回首页</view>
    </view>
    <view v-else-if="screen === 'empty'" class="notice glass-card">
      <text class="notice__title">还没有游戏流水</text>
      <text class="notice__text">完成一局 Demo Slots 后，下注、派彩和撤单会出现在这里。</text>
    </view>
    <view v-else class="list">
      <view v-for="item in records" :key="item.transactionId" class="row glass-card" :class="item.type">
        <view class="row__head">
          <text class="row__type">{{ labelOf(item.type) }}</text>
          <text class="row__amount">{{ signedAmount(item.amount) }}</text>
        </view>
        <text class="row__flow">{{ formatAmount(item.balanceBefore) }} → {{ formatAmount(item.balanceAfter) }}</text>
        <view class="row__meta">
          <text>{{ shortText(item.transactionId, 8, 4) }}</text>
          <text>{{ formatTime(item.createdAt) }}</text>
        </view>
      </view>
    </view>
  </PageShell>
</template>

<script setup>
import { ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import PageShell from '@/components/layout/PageShell.vue'
import AppNavBar from '@/components/layout/AppNavBar.vue'
import { getGameTransactions } from '@/api/game-server'
import { GAME_API_BASE } from '@/config/game'
import { ROUTES } from '@/config/routes'
import { useUserStore } from '@/stores/user'
import { formatAmount, shortText } from '@/utils/format'
import { relaunch } from '@/utils/nav'

const userStore = useUserStore()
const screen = ref('loading')
const noticeTitle = ref('')
const noticeText = ref('')
const records = ref([])

onShow(() => {
  load()
})

function load() {
  if (!GAME_API_BASE) {
    showNotice('Java游戏服务尚未部署', '线上环境暂不请求本地游戏服务。')
    return
  }
  if (!userStore.isLogin) {
    showNotice('请先登录', '登录后才能查看游戏流水。')
    return
  }
  getGameTransactions(userStore.profile.uid)
    .then((list) => {
      records.value = Array.isArray(list) ? list : []
      screen.value = records.value.length ? 'list' : 'empty'
    })
    .catch((error) => {
      if (error.message === '游戏服务未启动') {
        showNotice('游戏服务未启动', '本地运行时请先启动 server/，执行 mvn spring-boot:run。')
        return
      }
      showNotice(error.message || '游戏服务未启动', '请稍后重试。')
    })
}

function showNotice(title, text) {
  noticeTitle.value = title
  noticeText.value = text
  screen.value = 'notice'
}

function labelOf(type) {
  if (type === 'SETTLE') return 'SETTLE'
  if (type === 'ROLLBACK') return 'ROLLBACK'
  return 'BET'
}

function signedAmount(value) {
  const amount = Number(value) || 0
  const text = formatAmount(Math.abs(amount))
  return amount < 0 ? `-${text}` : `+${text}`
}

function formatTime(value) {
  if (!value) return ''
  const date = new Date(value)
  if (Number.isNaN(date.getTime())) return String(value).replace('T', ' ').slice(0, 19)
  const pad = (n) => String(n).padStart(2, '0')
  return `${date.getMonth() + 1}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}`
}

function goHome() {
  relaunch(ROUTES.home)
}
</script>

<style scoped>
.notice {
  padding: 22px 18px;
}

.notice__title,
.notice__text,
.row__type,
.row__amount,
.row__flow,
.row__meta text {
  display: block;
}

.notice__title {
  font-size: 18px;
  font-weight: 800;
}

.notice__text {
  margin-top: 10px;
  color: var(--muted);
  line-height: 1.6;
}

.notice__btn {
  margin-top: 18px;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.row {
  padding: 14px 14px 12px;
}

.row__head,
.row__meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.row__type,
.row__amount {
  font-weight: 800;
}

.row__amount {
  font-size: 18px;
}

.row__flow {
  margin-top: 8px;
  color: #d7def8;
}

.row__meta {
  margin-top: 8px;
  color: var(--faint);
  font-size: 12px;
}

.row.BET .row__amount {
  color: #ff8aa6;
}

.row.SETTLE .row__amount {
  color: #7dffe1;
}

.row.ROLLBACK .row__amount {
  color: #f0c14b;
}
</style>
