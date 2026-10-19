<template>
  <PageShell>
    <HomeUserBar
      :is-login="userStore.isLogin"
      :profile="userStore.profile"
      @login="openPage(ROUTES.login)"
      @register="openPage(ROUTES.register)"
      @deposit="openPage(ROUTES.deposit)"
    />
    <HomeBanner :banners="feed.banners" />
    <QuickGrid :items="QUICK_ENTRIES" @select="onQuick" />
    <GamePanel :categories="feed.categories" :games="feed.games" @select="onGame" />
  </PageShell>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app'
import PageShell from '@/components/layout/PageShell.vue'
import HomeUserBar from '@/components/home/HomeUserBar.vue'
import HomeBanner from '@/components/home/HomeBanner.vue'
import QuickGrid from '@/components/home/QuickGrid.vue'
import GamePanel from '@/components/home/GamePanel.vue'
import { getHomeFeed } from '@/api/game'
import { launchGame, syncGameWallet } from '@/api/game-server'
import { DEMO_GAME_CODE, DEMO_GAME_ID, GAME_API_BASE } from '@/config/game'
import { QUICK_ENTRIES } from '@/config/menus'
import { ROUTES } from '@/config/routes'
import { useUserStore } from '@/stores/user'
import { runMenuAction } from '@/utils/action'
import { openPage } from '@/utils/nav'
import { showToast } from '@/utils/toast'

const userStore = useUserStore()
const feed = getHomeFeed()

onLoad((query) => {
  if (query?.upcode) userStore.setPendingInviteCode(query.upcode)
})

function onQuick(item) {
  runMenuAction(item)
}

async function onGame(game) {
  if (game?.id !== DEMO_GAME_ID) {
    showToast('演示内容即将开放')
    return
  }
  if (!userStore.isLogin) {
    showToast('请先登录')
    return
  }
  if (!GAME_API_BASE) {
    openPage(ROUTES.gameDemo)
    return
  }
  try {
    uni.showLoading({ title: '正在进入', mask: true })
    // DEMO ONLY: 进入游戏前把当前前端余额同步到 Java Game Wallet。正式系统不由前端覆盖服务器余额。
    await syncGameWallet(userStore.profile)
    const session = await launchGame(userStore.profile.uid, DEMO_GAME_CODE)
    const sessionId = encodeURIComponent(session.sessionId)
    const gameCode = encodeURIComponent(session.gameCode || DEMO_GAME_CODE)
    openPage(`${ROUTES.gameDemo}?sessionId=${sessionId}&gameCode=${gameCode}`)
  } catch (error) {
    showToast(error.message || '游戏服务未启动')
  } finally {
    uni.hideLoading()
  }
}
</script>
