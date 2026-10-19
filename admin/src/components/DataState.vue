<template>
  <div v-if="loading" class="state">正在加载</div>
  <div v-else-if="error" class="state">
    <div>{{ error }}</div>
    <button class="btn" type="button" @click="$emit('retry')">重新加载</button>
  </div>
  <div v-else-if="empty" class="state">{{ emptyText }}</div>
  <slot v-else />
</template>

<script setup>
defineProps({
  loading: Boolean,
  error: { type: String, default: '' },
  empty: Boolean,
  emptyText: { type: String, default: '暂无数据' }
})

defineEmits(['retry'])
</script>
