<template>
  <view class="logo" :style="{ width: `${size}px`, height: `${size}px` }">
    <text class="logo__letter">Z</text>
  </view>
</template>

<script setup>
defineProps({
  size: { type: Number, default: 42 }
})
</script>

<style scoped>
.logo {
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 14px;
  background: linear-gradient(145deg, #7a8cff 0%, #8d4dff 55%, #4ce7ff 140%);
  box-shadow: 0 8px 18px rgba(98, 82, 255, 0.35), inset 0 1px 0 rgba(255, 255, 255, 0.35);
}

.logo__letter {
  color: #fff;
  font-size: 22px;
  font-weight: 800;
  letter-spacing: 0;
}
</style>
