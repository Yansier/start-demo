<template>
  <PageShell>
    <view class="head">
      <text class="head__title">活动</text>
      <text class="head__desc">福利与限时玩法</text>
    </view>
    <HomeBanner :banners="banners" />
    <ActivityCard
      v-for="item in activities"
      :key="item.id"
      :activity="item"
      @select="openDetail"
    />
  </PageShell>
</template>

<script setup>
import PageShell from '@/components/layout/PageShell.vue'
import HomeBanner from '@/components/home/HomeBanner.vue'
import ActivityCard from '@/components/activity/ActivityCard.vue'
import { activities, toActivityBanners } from '@/mock/activities'
import { ROUTES } from '@/config/routes'
import { openPage } from '@/utils/nav'

const banners = toActivityBanners(activities)

function openDetail(item) {
  openPage(`${ROUTES.activityDetail}?id=${item.id}`)
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
  color: var(--muted);
}
</style>
