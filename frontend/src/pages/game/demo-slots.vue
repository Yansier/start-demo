<template>
  <PageShell :show-tab="false">
    <view class="top">
      <view class="top__back" :class="{ 'is-locked': roundLocked }" @click="leave">
        <view class="top__chevron" />
      </view>
      <view class="top__title">
        <text class="top__brand">DEMO PROVIDER</text>
        <text class="top__name">Demo Slots</text>
      </view>
      <text class="top__link" :class="{ 'is-locked': roundLocked }" @click="openRecords">游戏流水</text>
    </view>

    <view v-if="screen !== 'play'" class="notice glass-card">
      <text class="notice__title">{{ noticeTitle }}</text>
      <text class="notice__text">{{ noticeText }}</text>
      <view class="btn-primary notice__btn" @click="leave">返回首页</view>
    </view>

    <view v-else class="stage">
      <view class="balance glass-card">
        <text class="balance__label">当前余额</text>
        <text class="balance__value">{{ formatAmount(userStore.profile?.balance) }}</text>
      </view>

      <view class="machine">
        <view class="machine__light" />
        <view class="reels" :class="{ spinning }">
          <view v-for="(symbol, index) in reels" :key="index" class="reel">
            <text class="reel__symbol">{{ symbol }}</text>
          </view>
        </view>
        <text class="result" :class="{ win: winAmount > 0 }">{{ resultText }}</text>
      </view>

      <view class="stakes">
        <view
          v-for="item in stakes"
          :key="item"
          class="stake"
          :class="{ on: item === stake, off: busy }"
          @click="chooseStake(item)"
        >
          {{ item }}
        </view>
      </view>

      <view class="btn-primary spin" :class="{ 'is-disabled': busy }" @click="startRound">开始游戏</view>

      <view class="tools">
        <text class="tools__toggle" @click="toolsOpen = !toolsOpen">{{ toolsOpen ? '收起演示工具' : '演示工具' }}</text>
        <view v-if="toolsOpen" class="tools__body glass-card">
          <view class="tools__row" @click="pauseSettle = !pauseSettle">
            <text>下注后暂停结算</text>
            <text class="tools__flag">{{ pauseSettle ? '开' : '关' }}</text>
          </view>
          <view class="btn-ghost" :class="{ 'is-disabled': !pendingBet || busy }" @click="rollbackRound">模拟撤单</view>
        </view>
      </view>

      <text class="footnote">
        当前游戏由 Mock Game Provider 模拟。平台已实现 Session、BET、SETTLE、ROLLBACK 和 Wallet Transaction。正式接入供应商后，游戏内容和结果由供应商提供，平台负责账户、钱包和接口对接。
      </text>
    </view>
  </PageShell>
</template>

<script setup>
import { computed, onUnmounted, ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import PageShell from '@/components/layout/PageShell.vue'
import { betGame, closeGameSession, getGameBalance, rollbackGame, settleGame } from '@/api/game-server'
import { GAME_API_BASE } from '@/config/game'
import { ROUTES } from '@/config/routes'
import { useUserStore } from '@/stores/user'
import { formatAmount } from '@/utils/format'
import { openPage, relaunch } from '@/utils/nav'
import { showToast } from '@/utils/toast'

const SYMBOLS = ['🍒', '⭐', '7', '💎', 'BAR']
const stakes = [10, 50, 100]

const userStore = useUserStore()
const screen = ref('loading')
const noticeTitle = ref('')
const noticeText = ref('')
const sessionId = ref('')
const stake = ref(50)
const reels = ref(['🍒', '⭐', '7'])
const spinning = ref(false)
const busy = ref(false)
const toolsOpen = ref(false)
const pauseSettle = ref(false)
const pendingBet = ref(null)
const resultText = ref('选择投注，开始一局')
const winAmount = ref(0)
const leaving = ref(false)
let spinTimer = null

const canPlay = computed(() => screen.value === 'play' && sessionId.value)
const roundLocked = computed(() => busy.value || pendingBet.value !== null)

onLoad((query) => {
  sessionId.value = query?.sessionId || ''
  prepare(query?.gameCode)
})

onUnmounted(() => {
  clearSpin()
  if (busy.value || pendingBet.value) return
  if (canPlay.value) closeQuietly()
})

function prepare() {
  if (!GAME_API_BASE) {
    showNotice('Java游戏服务尚未部署', '当前线上环境只展示前端。本地演示请打开 http://localhost:5173/ 。')
    return
  }
  if (!userStore.isLogin) {
    showNotice('请先登录', '登录后从首页进入 Demo Slots。')
    return
  }
  if (!sessionId.value) {
    showNotice('游戏服务未启动', '本地运行时请先启动 server/，执行 mvn spring-boot:run，再从首页进入。')
    return
  }
  getGameBalance(userStore.profile.uid)
    .then((result) => {
      userStore.setBalance(result.balance)
      screen.value = 'play'
    })
    .catch((error) => {
      if (error.message === '游戏服务未启动') {
        showNotice('游戏服务未启动', '本地运行时请先启动 server/，执行 mvn spring-boot:run。')
        return
      }
      showNotice(error.message || '游戏服务未启动', '请返回首页后重试。')
    })
}

function showNotice(title, text) {
  noticeTitle.value = title
  noticeText.value = text
  screen.value = 'notice'
}

function chooseStake(value) {
  if (busy.value) return
  stake.value = value
}

function startRound() {
  if (!canPlay.value || busy.value) return
  busy.value = true
  spinning.value = true
  winAmount.value = 0
  resultText.value = '正在下注'
  const transactionId = createId('BET')
  betGame({ sessionId: sessionId.value, transactionId, amount: stake.value })
    .then(async (result) => {
      userStore.setBalance(result.balance)
      pendingBet.value = { transactionId, amount: stake.value }
      if (pauseSettle.value) {
        resultText.value = '本局等待结算'
        return
      }
      await spinReels()
      await finishSettle(transactionId)
    })
    .catch((error) => {
      resultText.value = error.message || '下注失败'
      showToast(resultText.value)
    })
    .finally(() => {
      spinning.value = false
      busy.value = false
    })
}

async function finishSettle(betId) {
  const amount = rollWin()
  const settled = await settleGame({
    sessionId: sessionId.value,
    transactionId: createId('SETTLE'),
    relatedTransactionId: betId,
    amount
  })
  userStore.setBalance(settled.balance)
  pendingBet.value = null
  winAmount.value = amount
  resultText.value = amount > 0 ? `恭喜中奖 ${amount}` : '未中奖'
}

function rollbackRound() {
  if (!pendingBet.value || busy.value) return
  busy.value = true
  const relatedTransactionId = pendingBet.value.transactionId
  rollbackGame({
    sessionId: sessionId.value,
    transactionId: createId('ROLLBACK'),
    relatedTransactionId
  })
    .then((result) => {
      userStore.setBalance(result.balance)
      pendingBet.value = null
      winAmount.value = 0
      resultText.value = '本局已撤单'
    })
    .catch((error) => {
      showToast(error.message || '撤单失败')
    })
    .finally(() => {
      busy.value = false
    })
}

function rollWin() {
  // DEMO PROVIDER SIMULATOR。正式游戏结果由外部游戏供应商服务器决定，不会由平台前端随机生成。
  const roll = Math.random()
  if (roll < 0.5) return 0
  if (roll < 0.75) return 50
  if (roll < 0.9) return 100
  if (roll < 0.98) return 200
  return 500
}

function spinReels() {
  return new Promise((resolve) => {
    const started = Date.now()
    clearSpin()
    spinTimer = setInterval(() => {
      reels.value = [0, 1, 2].map(() => SYMBOLS[Math.floor(Math.random() * SYMBOLS.length)])
      if (Date.now() - started < 1000) return
      clearSpin()
      resolve()
    }, 120)
  })
}

function clearSpin() {
  if (!spinTimer) return
  clearInterval(spinTimer)
  spinTimer = null
}

function createId(prefix) {
  return `${prefix}-${Date.now()}-${Math.random().toString(36).slice(2, 8)}`
}

function openRecords() {
  if (busy.value) {
    showToast('本局正在处理中，请稍候')
    return
  }
  if (pendingBet.value) {
    showToast('本局尚未完成，请先结算或撤单')
    return
  }
  if (!userStore.isLogin) {
    showToast('请先登录')
    return
  }
  openPage(ROUTES.gameTransactions)
}

async function leave() {
  if (busy.value) {
    showToast('本局正在处理中，请稍候')
    return
  }
  if (pendingBet.value) {
    showToast('本局尚未完成，请先完成结算或模拟撤单')
    return
  }
  if (leaving.value) return
  leaving.value = true
  clearSpin()
  if (sessionId.value && GAME_API_BASE) {
    try {
      await closeGameSession(sessionId.value)
      sessionId.value = ''
    } catch (error) {
      showToast('会话关闭失败')
    }
  }
  relaunch(ROUTES.home)
}

function closeQuietly() {
  const current = sessionId.value
  if (!current || !GAME_API_BASE || leaving.value) return
  sessionId.value = ''
  closeGameSession(current).catch(() => {})
}
</script>

<style scoped>
.top {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 14px;
}

.top__back,
.top__link {
  width: 72px;
}

.top__back {
  height: 36px;
  display: flex;
  align-items: center;
}

.top__chevron {
  width: 10px;
  height: 10px;
  border-left: 2px solid #fff;
  border-bottom: 2px solid #fff;
  transform: rotate(45deg);
  margin-left: 4px;
}

.top__title {
  flex: 1;
  text-align: center;
}

.top__brand,
.top__name,
.balance__label,
.balance__value,
.result,
.notice__title,
.notice__text,
.footnote {
  display: block;
}

.top__brand {
  color: #d7b0ff;
  font-size: 11px;
  letter-spacing: 1px;
}

.top__name {
  font-size: 18px;
  font-weight: 800;
}

.top__link {
  color: #8ef6ff;
  font-size: 13px;
  text-align: right;
}

.top__back.is-locked,
.top__link.is-locked {
  opacity: 0.4;
}

.notice {
  padding: 22px 18px;
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

.balance {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  padding: 14px 16px;
}

.balance__label {
  color: var(--muted);
}

.balance__value {
  color: var(--gold);
  font-size: 26px;
  font-weight: 800;
}

.machine {
  margin-top: 14px;
  padding: 16px 12px 14px;
  border-radius: 22px;
  background:
    radial-gradient(80% 80% at 50% 0%, rgba(255, 214, 120, 0.18), transparent 55%),
    linear-gradient(180deg, #2a1458, #12091f);
  border: 1px solid rgba(255, 214, 140, 0.28);
  box-shadow: inset 0 0 0 4px rgba(88, 42, 150, 0.45);
}

.reels {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 8px;
}

.reel {
  height: 108px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(180deg, #fff8ea, #f2d7a2);
  box-shadow: inset 0 -10px 16px rgba(120, 70, 20, 0.16);
}

.reel__symbol {
  color: #4a2410;
  font-size: 42px;
  font-weight: 800;
}

.spinning .reel__symbol {
  animation: blink 0.12s linear infinite;
}

.result {
  min-height: 24px;
  margin-top: 12px;
  text-align: center;
  color: #f7e7ff;
  font-weight: 700;
}

.result.win {
  color: #7dffe1;
}

.stakes {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 8px;
  margin-top: 14px;
}

.stake {
  height: 40px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #efe7ff;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(196, 170, 255, 0.24);
}

.stake.on {
  color: #2a1608;
  background: linear-gradient(135deg, #ffe08a, #f0b429);
  border-color: transparent;
}

.stake.off {
  opacity: 0.6;
}

.spin {
  margin-top: 12px;
}

.btn-ghost.is-disabled {
  opacity: 0.45;
}

.tools {
  margin-top: 16px;
}

.tools__toggle {
  color: #c9b7ff;
  font-size: 13px;
}

.tools__body {
  margin-top: 8px;
  padding: 12px;
}

.tools__row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 10px;
}

.tools__flag {
  color: var(--gold);
}

.footnote {
  margin-top: 16px;
  color: var(--faint);
  font-size: 12px;
  line-height: 1.6;
}

@keyframes blink {
  50% { opacity: 0.35; }
}
</style>
