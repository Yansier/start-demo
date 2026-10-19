<template>
  <view class="shell" :class="{ 'has-tab': showTab }">
    <view class="orb orb-a" />
    <view class="orb orb-b" />
    <view class="shell__content">
      <slot />
    </view>
    <BottomTabBar v-if="showTab" />
    <ServiceModal />
    <SafariGuideModal />
  </view>
</template>

<script setup>
import BottomTabBar from '@/components/tab/BottomTabBar.vue'
import ServiceModal from '@/components/service/ServiceModal.vue'
import SafariGuideModal from '@/components/download/SafariGuideModal.vue'

defineProps({
  showTab: { type: Boolean, default: true }
})
</script>

<style scoped>
.shell {
  position: relative;
  min-height: 100vh;
}

.shell__content {
  position: relative;
  z-index: 1;
  padding: calc(12px + env(safe-area-inset-top)) 16px 28px;
}

.has-tab .shell__content {
  padding-bottom: calc(96px + env(safe-area-inset-bottom));
}

.orb {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
  filter: blur(28px);
}

.orb-a {
  width: 160px;
  height: 160px;
  top: -46px;
  right: 18px;
  background: rgba(104, 86, 255, 0.38);
}

.orb-b {
  width: 120px;
  height: 120px;
  top: 240px;
  left: 0;
  background: rgba(40, 196, 255, 0.16);
}
</style>
