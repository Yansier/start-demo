<template>
  <view v-if="modelValue" class="mask" @click="onMask">
    <view class="sheet" @click.stop>
      <view class="sheet__bar" />
      <view class="sheet__head">
        <text class="sheet__title">{{ title }}</text>
        <view class="sheet__close" @click="close">×</view>
      </view>
      <slot />
    </view>
  </view>
</template>

<script setup>
const props = defineProps({
  modelValue: { type: Boolean, default: false },
  title: { type: String, default: '' },
  closeOnMask: { type: Boolean, default: true }
})

const emit = defineEmits(['update:modelValue'])

function close() {
  emit('update:modelValue', false)
}

function onMask() {
  if (props.closeOnMask) close()
}
</script>

<style scoped>
.mask {
  position: fixed;
  z-index: 60;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  background: rgba(3, 5, 16, 0.66);
}

.sheet {
  width: 100%;
  max-width: 430px;
  padding: 10px 16px calc(18px + env(safe-area-inset-bottom));
  border-radius: 22px 22px 0 0;
  background:
    radial-gradient(80% 80% at 10% 0%, rgba(110, 90, 255, 0.28), transparent 55%),
    linear-gradient(180deg, #1c2044 0%, #12152d 100%);
  border: 1px solid rgba(168, 186, 255, 0.24);
  border-bottom: 0;
  box-shadow: 0 -16px 40px rgba(0, 0, 0, 0.35);
  animation: rise 0.22s ease;
}

.sheet__bar {
  width: 42px;
  height: 4px;
  margin: 0 auto 12px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.22);
}

.sheet__head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 14px;
}

.sheet__title {
  font-size: 18px;
  font-weight: 700;
}

.sheet__close {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #d5dcf8;
  background: rgba(255, 255, 255, 0.06);
  font-size: 20px;
}

@keyframes rise {
  from { transform: translateY(24px); opacity: 0.4; }
  to { transform: translateY(0); opacity: 1; }
}
</style>
