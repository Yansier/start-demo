<template>
  <image
    class="svg-icon"
    :src="`/static/icons/${name}.svg`"
    mode="aspectFit"
    :style="{ width: `${size}px`, height: `${size}px` }"
  />
</template>

<script setup>
defineProps({
  name: { type: String, required: true },
  size: { type: [Number, String], default: 22 }
})
</script>

<style scoped>
.svg-icon {
  display: block;
  flex: none;
}
</style>
