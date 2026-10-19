<template>
  <view class="amount">
    <text class="amount__label">提现金额</text>
    <view class="amount__row">
      <input
        class="field"
        :value="modelValue"
        placeholder="最低 10"
        placeholder-class="ph"
        @input="onInput"
      />
      <view class="amount__all" @click="$emit('fill')">全部提现</view>
    </view>
  </view>
</template>

<script setup>
defineProps({
  modelValue: { type: String, default: '' }
})

const emit = defineEmits(['update:modelValue', 'fill'])

function onInput(event) {
  emit('update:modelValue', event.detail?.value ?? event.target?.value ?? '')
}
</script>

<style scoped>
.amount__label {
  display: block;
  margin-bottom: 8px;
  color: #d5def8;
  font-size: 13px;
}

.amount__row {
  display: flex;
  align-items: center;
  gap: 8px;
}

.field {
  flex: 1;
  margin-top: 0;
}

.amount__all {
  flex: none;
  height: 48px;
  padding: 0 12px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  color: var(--cyan);
  font-size: 13px;
  font-weight: 700;
  border: 1px solid rgba(92, 239, 255, 0.35);
}
</style>
