<template>
  <view class="nav">
    <view class="nav__btn" @click="handleBack">
      <view class="nav__chevron" />
    </view>
    <text class="nav__title">{{ title }}</text>
    <view class="nav__btn" />
  </view>
</template>

<script setup>
import { ROUTES } from '@/config/routes'
import { goBack } from '@/utils/nav'

defineProps({
  title: { type: String, default: '' }
})

function handleBack() {
  goBack(ROUTES.home)
}
</script>

<style scoped>
.nav {
  display: flex;
  align-items: center;
  height: 44px;
  margin-bottom: 8px;
}

.nav__btn {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.nav__chevron {
  width: 10px;
  height: 10px;
  border-left: 2px solid #fff;
  border-bottom: 2px solid #fff;
  transform: rotate(45deg);
  margin-left: 4px;
}

.nav__title {
  flex: 1;
  text-align: center;
  font-size: 17px;
  font-weight: 700;
}
</style>
