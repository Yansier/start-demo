<template>
  <view class="actions">
    <view class="link-box">
      <text class="link-box__label">飞机邀请链接</text>
      <text class="link-box__url">{{ botLink }}</text>
      <text class="link-box__label">网页推荐链接</text>
      <text class="link-box__url">{{ webLink }}</text>
    </view>
    <view class="btn-primary" @click="$emit('share')">飞机一键分享</view>
    <view class="btn-ghost" @click="$emit('copy-bot')">复制飞机链接</view>
    <view class="btn-ghost" @click="$emit('copy-web')">复制网页推荐链接</view>
  </view>
</template>

<script setup>
defineProps({
  botLink: { type: String, default: '' },
  webLink: { type: String, default: '' }
})

defineEmits(['share', 'copy-bot', 'copy-web'])
</script>

<style scoped>
.actions {
  margin-top: 14px;
}

.link-box {
  margin-bottom: 12px;
  padding: 12px;
  border-radius: 16px;
  background: rgba(8, 12, 32, 0.4);
  border: 1px solid rgba(168, 186, 255, 0.16);
}

.link-box__label,
.link-box__url {
  display: block;
}

.link-box__label {
  color: var(--faint);
  font-size: 12px;
}

.link-box__url {
  margin: 4px 0 10px;
  color: #d7e7ff;
  font-size: 12px;
  line-height: 1.5;
  word-break: break-all;
}

.link-box__label:last-of-type + .link-box__url,
.link-box__url:last-child {
  margin-bottom: 0;
}

.btn-ghost {
  margin-top: 10px;
}
</style>
