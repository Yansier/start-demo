<template>
  <swiper
    class="banner"
    circular
    autoplay
    :interval="3600"
    indicator-dots
    indicator-color="rgba(255,255,255,0.35)"
    indicator-active-color="#5cefff"
  >
    <swiper-item v-for="item in banners" :key="item.id">
      <view class="slide" :style="{ background: item.background }">
        <text class="slide__kicker">{{ item.kicker }}</text>
        <text class="slide__title">{{ item.title }}</text>
        <text class="slide__desc">{{ item.desc }}</text>
      </view>
    </swiper-item>
  </swiper>
</template>

<script setup>
defineProps({
  banners: { type: Array, default: () => [] }
})
</script>

<style scoped>
.banner {
  height: 148px;
  margin-top: 16px;
  border-radius: 18px;
  overflow: hidden;
}

.slide {
  height: 148px;
  padding: 20px 18px;
  border-radius: 18px;
  position: relative;
  overflow: hidden;
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, 0.22);
}

.slide::after {
  content: '';
  position: absolute;
  width: 140px;
  height: 140px;
  right: -30px;
  top: -40px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.14);
}

.slide__kicker,
.slide__title,
.slide__desc {
  position: relative;
  z-index: 1;
  display: block;
}

.slide__kicker {
  width: fit-content;
  padding: 3px 8px;
  border-radius: 999px;
  background: rgba(5, 10, 28, 0.28);
  font-size: 11px;
  letter-spacing: 1px;
}

.slide__title {
  margin-top: 12px;
  font-size: 26px;
  font-weight: 800;
}

.slide__desc {
  margin-top: 8px;
  max-width: 230px;
  color: rgba(255, 255, 255, 0.86);
  font-size: 13px;
  line-height: 1.5;
}
</style>
