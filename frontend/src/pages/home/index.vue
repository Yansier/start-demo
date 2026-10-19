<template>
  <PageShell>
    <HomeUserBar
      :is-login="userStore.isLogin"
      :profile="userStore.profile"
      @login="openPage(ROUTES.login)"
      @register="openPage(ROUTES.register)"
      @deposit="showComingSoon"
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
import { QUICK_ENTRIES } from '@/config/menus'
import { ROUTES } from '@/config/routes'
import { useUserStore } from '@/stores/user'
import { runMenuAction } from '@/utils/action'
import { openPage } from '@/utils/nav'
import { showComingSoon, showToast } from '@/utils/toast'

const userStore = useUserStore()
const feed = getHomeFeed()

onLoad((query) => {
  if (query?.upcode) userStore.setPendingInviteCode(query.upcode)
})

function onQuick(item) {
  runMenuAction(item)
}

function onGame() {
  showToast('游戏演示即将开放')
}
</script>
