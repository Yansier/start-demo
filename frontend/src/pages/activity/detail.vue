<template>
  <PageShell :show-tab="false">
    <AppNavBar title="活动详情" />
    <view v-if="!activity" class="missing">未找到该活动</view>
    <view v-else class="detail">
      <view class="hero" :style="{ background: activity.gradient }">
        <text class="hero__tag">{{ activity.tag }}</text>
        <text class="hero__title">{{ activity.title }}</text>
        <text class="hero__sub">{{ activity.subtitle }}</text>
      </view>
      <view class="panel glass-card">
        <view class="meta">
          <text>状态</text>
          <text>{{ activity.status }}</text>
        </view>
        <view class="meta">
          <text>活动时间</text>
          <text>{{ activity.startTime }} ~ {{ activity.endTime }}</text>
        </view>
        <text class="desc">{{ activity.description }}</text>
        <view class="btn-ghost back" @click="goBack(ROUTES.activity)">返回活动列表</view>
      </view>
    </view>
  </PageShell>
</template>

<script setup>
import { ref } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import PageShell from '@/components/layout/PageShell.vue'
import AppNavBar from '@/components/layout/AppNavBar.vue'
import { activities } from '@/mock/activities'
import { ROUTES } from '@/config/routes'
import { goBack } from '@/utils/nav'

const activity = ref(null)

onLoad((query) => {
  activity.value = activities.find((item) => item.id === query?.id) || null
})
</script>

<style scoped>
.hero {
  padding: 22px 18px;
  border-radius: 18px;
}

.hero__tag,
.hero__title,
.hero__sub,
.desc {
  display: block;
}

.hero__tag {
  width: fit-content;
  padding: 3px 8px;
  border-radius: 999px;
  background: rgba(5, 10, 28, 0.28);
  font-size: 12px;
}

.hero__title {
  margin-top: 12px;
  font-size: 26px;
  font-weight: 800;
}

.hero__sub {
  margin-top: 8px;
  line-height: 1.5;
}

.panel {
  margin-top: 14px;
  padding: 16px;
}

.meta {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 10px;
  color: var(--muted);
  font-size: 13px;
}

.desc {
  margin-top: 6px;
  line-height: 1.7;
}

.back {
  margin-top: 16px;
}

.missing {
  padding: 24px 0;
  color: var(--muted);
  text-align: center;
}
</style>
