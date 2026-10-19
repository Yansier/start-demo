<template>
  <view class="qr">
    <view class="qr__grid">
      <view v-for="(on, index) in cells" :key="index" class="qr__cell" :class="{ on }" />
    </view>
    <text class="qr__mark">DEMO</text>
  </view>
</template>

<script setup>
const pattern = [
  '11111110101111111',
  '10000010101000001',
  '10111010001011101',
  '10111010111011101',
  '10111010000011101',
  '10000010101100001',
  '11111110101111111',
  '00000000100000000',
  '11010111011101101',
  '00100010100010010',
  '11111110111011111',
  '10000010001000001',
  '10111010111011101',
  '10111010000011101',
  '10111010101111101',
  '10000010101000001',
  '11111110101111111'
]

const cells = pattern.join('').split('').map((item) => item === '1')
</script>

<style scoped>
.qr {
  position: relative;
  width: 168px;
  height: 168px;
  margin: 0 auto;
  padding: 10px;
  border-radius: 16px;
  background: #f4f7ff;
}

.qr__grid {
  display: grid;
  grid-template-columns: repeat(17, 1fr);
  gap: 1px;
  height: 100%;
}

.qr__cell.on {
  background: #15182e;
}

.qr__mark {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  padding: 2px 6px;
  border-radius: 6px;
  color: #15182e;
  background: #f4f7ff;
  font-size: 11px;
  font-weight: 800;
}
</style>
