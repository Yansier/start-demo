<template>
  <BaseModal :model-value="modelValue" title="设置提现密码" @update:model-value="onVisible">
    <text class="label">提现密码</text>
    <input v-model="password" class="field" password maxlength="32" placeholder="至少6位" placeholder-class="ph" />
    <text class="label">再次输入提现密码</text>
    <input v-model="confirm" class="field" password maxlength="32" placeholder="请再次输入" placeholder-class="ph" />
    <text class="form-error">{{ errorText }}</text>
    <view class="btn-primary" @click="save">保存</view>
  </BaseModal>
</template>

<script setup>
import { ref, watch } from 'vue'
import BaseModal from '@/components/modal/BaseModal.vue'
import { useWalletStore } from '@/stores/wallet'
import { showToast } from '@/utils/toast'

const props = defineProps({
  modelValue: { type: Boolean, default: false }
})

const emit = defineEmits(['update:modelValue'])
const walletStore = useWalletStore()
const password = ref('')
const confirm = ref('')
const errorText = ref('')

watch(() => props.modelValue, (open) => {
  if (!open) return
  password.value = ''
  confirm.value = ''
  errorText.value = ''
})

function onVisible(value) {
  emit('update:modelValue', value)
}

function save() {
  const message = walletStore.setWithdrawPassword(password.value, confirm.value)
  if (message) {
    errorText.value = message
    return
  }
  showToast('提现密码已设置')
  emit('update:modelValue', false)
}
</script>

<style scoped>
.label {
  display: block;
  margin-top: 10px;
  color: #d5def8;
  font-size: 13px;
}

.field {
  margin-top: 8px;
}
</style>
