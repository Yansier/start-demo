<template>
  <view class="notice">
    <text v-if="turnoverReady">消费流水要求：已完成</text>
    <text>⚠ 最小提现金额为10U，单笔提现手续费0U</text>
    <text>⚠ 未完成消费流水为0时方可进行提现操作，否则需完成相应的游戏流水</text>
    <text>⚠ 部分游戏投注可能不计入消费流水，具体规则以正式业务配置为准</text>
  </view>
</template>

<script setup>
defineProps({
  turnoverReady: { type: Boolean, default: true }
})
</script>

<style scoped>
.notice {
  margin-top: 16px;
}

.notice text {
  display: block;
  margin-top: 8px;
  color: var(--muted);
  font-size: 12px;
  line-height: 1.6;
}
</style>
