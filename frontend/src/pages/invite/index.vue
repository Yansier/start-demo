<template>
  <PageShell :show-tab="false">
    <AppNavBar title="邀请好友" />
    <view class="head">
      <text class="head__title">邀请好友</text>
      <text class="head__desc">邀请好友赚取最高 0.6% 的返佣</text>
    </view>
    <LoginRequired>
      <view class="hero">
        <text class="hero__kicker">最高 0.6%</text>
        <text class="hero__title">分享你的邀请码 {{ uid }}</text>
        <text class="hero__desc">好友通过链接注册后，会计入下面的演示数据。</text>
      </view>
      <InviteActions
        :bot-link="botLink"
        :web-link="webLink"
        @share="shareTelegram(botLink)"
        @copy-bot="copyText(botLink)"
        @copy-web="copyText(webLink)"
      />
      <InviteStats :items="stats" />
      <InviteList :items="recentInvites" />
    </LoginRequired>
  </PageShell>
</template>

<script setup>
import { computed } from 'vue'
import PageShell from '@/components/layout/PageShell.vue'
import AppNavBar from '@/components/layout/AppNavBar.vue'
import LoginRequired from '@/components/auth/LoginRequired.vue'
import InviteActions from '@/components/invite/InviteActions.vue'
import InviteStats from '@/components/invite/InviteStats.vue'
import InviteList from '@/components/invite/InviteList.vue'
import { inviteSummary, recentInvites } from '@/mock/invite'
import { useUserStore } from '@/stores/user'
import { formatAmount } from '@/utils/format'
import { buildBotInviteLink, buildWebInviteLink, copyText, shareTelegram } from '@/utils/invite'

const userStore = useUserStore()
const uid = computed(() => userStore.profile?.uid || '')
const botLink = computed(() => buildBotInviteLink(uid.value))
const webLink = computed(() => buildWebInviteLink(uid.value))
const stats = [
  { label: '今日新增人数', value: String(inviteSummary.today) },
  { label: '直属下级人数', value: String(inviteSummary.direct) },
  { label: '累计邀请人数', value: String(inviteSummary.total) },
  { label: '累计返佣', value: formatAmount(inviteSummary.commission) }
]
</script>

<style scoped>
.head__title,
.head__desc,
.hero__kicker,
.hero__title,
.hero__desc {
  display: block;
}

.head__title,
.hero__title {
  font-size: 24px;
  font-weight: 800;
}

.head__desc {
  margin-top: 6px;
  margin-bottom: 14px;
  color: var(--muted);
}

.hero {
  padding: 18px;
  border-radius: 18px;
  background: linear-gradient(135deg, #3c1874 0%, #7a2e9c 48%, #2450d0 100%);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

.hero__kicker {
  width: fit-content;
  padding: 3px 8px;
  border-radius: 999px;
  background: rgba(5, 10, 28, 0.28);
  font-size: 12px;
}

.hero__title {
  margin-top: 12px;
  font-size: 22px;
}

.hero__desc {
  margin-top: 8px;
  color: rgba(255, 255, 255, 0.86);
  line-height: 1.5;
}
</style>
