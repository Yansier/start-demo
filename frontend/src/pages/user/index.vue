<template>
  <PageShell>
    <view class="head">
      <text class="head__title">我的</text>
      <text class="head__desc">账户、会员与常用服务</text>
    </view>
    <ProfileCard
      :is-login="userStore.isLogin"
      :profile="userStore.profile"
      @login="openPage(ROUTES.login)"
      @register="openPage(ROUTES.register)"
    />
    <MenuGrid :items="USER_MENUS" @select="onMenu" />
    <view v-if="userStore.isLogin" class="btn-ghost logout" @click="confirmLogout">退出登录</view>
  </PageShell>
</template>

<script setup>
import PageShell from '@/components/layout/PageShell.vue'
import ProfileCard from '@/components/user/ProfileCard.vue'
import MenuGrid from '@/components/user/MenuGrid.vue'
import { USER_MENUS } from '@/config/menus'
import { ROUTES } from '@/config/routes'
import { useUserStore } from '@/stores/user'
import { runMenuAction } from '@/utils/action'
import { openPage } from '@/utils/nav'
import { showToast } from '@/utils/toast'

const userStore = useUserStore()

function onMenu(item) {
  if (item.action === 'service') {
    runMenuAction(item.action)
    return
  }
  if (!userStore.isLogin) {
    showToast('请先登录')
    return
  }
  runMenuAction(item.action)
}

function confirmLogout() {
  uni.showModal({
    title: '退出登录',
    content: '确定退出当前账号吗？',
    confirmColor: '#8d4dff',
    success(res) {
      if (!res.confirm) return
      userStore.logout()
      showToast('已退出登录')
    }
  })
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
  margin-bottom: 16px;
  color: var(--muted);
}

.logout {
  margin-top: 18px;
  color: #ff8aa6;
}
</style>
