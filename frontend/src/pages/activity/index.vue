<template>
  <PageShell>
    <view class="head">
      <text class="head__title">活动</text>
      <text class="head__desc">福利与限时玩法</text>
    </view>
    <GlassCard>
      <view class="notice">
        <text class="notice__kicker">COMING SOON</text>
        <text class="notice__title">活动中心即将上线</text>
        <text class="notice__desc">关注官方频道，第一时间获取福利与维护通知。</text>
        <view class="btn-primary notice__btn" @click="openChannel">前往官方频道</view>
      </view>
    </GlassCard>
  </PageShell>
</template>

<script setup>
import PageShell from '@/components/layout/PageShell.vue'
import GlassCard from '@/components/common/GlassCard.vue'
import { OFFICIAL_CHANNEL } from '@/config/app'
import { openLink } from '@/utils/link'

function openChannel() {
  openLink(OFFICIAL_CHANNEL)
}
</script>

<style scoped>
.head__title,
.head__desc,
.notice__kicker,
.notice__title,
.notice__desc {
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

.notice {
  padding: 22px 18px 18px;
}

.notice__kicker {
  color: var(--cyan);
  font-size: 12px;
  letter-spacing: 1px;
}

.notice__title {
  margin-top: 8px;
  font-size: 22px;
  font-weight: 800;
}

.notice__desc {
  margin-top: 8px;
  color: var(--muted);
  line-height: 1.6;
}

.notice__btn {
  margin-top: 18px;
}
</style>
