<template>
  <BaseModal :model-value="modelValue" title="绑定提现地址" @update:model-value="onVisible">
    <text class="label">USDT TRC20 地址</text>
    <input v-model="address" class="field" maxlength="64" placeholder="请输入提现地址" placeholder-class="ph" />
    <text class="form-error">{{ errorText }}</text>
    <view class="btn-primary" @click="save">保存地址</view>
  </BaseModal>
</template>

<script setup>
import { ref, watch } from 'vue'
import BaseModal from '@/components/modal/BaseModal.vue'
import { useWalletStore } from '@/stores/wallet'
import { showToast } from '@/utils/toast'

const props = defineProps({
  modelValue: { type: Boolean, default: false },
  current: { type: String, default: '' }
})

const emit = defineEmits(['update:modelValue'])
const walletStore = useWalletStore()
const address = ref('')
const errorText = ref('')

watch(() => props.modelValue, (open) => {
  if (!open) return
  address.value = props.current || ''
  errorText.value = ''
})

function onVisible(value) {
  emit('update:modelValue', value)
}

function save() {
  const message = walletStore.bindAddress(address.value)
  if (message) {
    errorText.value = message
    return
  }
  showToast('地址已保存')
  emit('update:modelValue', false)
}
</script>

<style scoped>
.label {
  display: block;
  color: #d5def8;
  font-size: 13px;
}

.field {
  margin-top: 8px;
}
</style>
