<template>
  <view class="panel glass-card">
    <text class="panel__title">X钱包充值</text>
    <text class="panel__desc">首次使用 X 钱包充值，请联系客服办理和确认账户信息。</text>
    <view class="btn-primary" @click="uiStore.openService()">联系充值客服</view>
    <text class="panel__note">办理完成后可通过该渠道提交充值订单。</text>
    <AmountInput v-model="amount" label="充值金额" :show-fill="false" />
    <text class="form-error">{{ errorText }}</text>
    <view class="btn-primary" :class="{ 'is-disabled': submitting }" @click="submit">提交充值订单</view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import AmountInput from '@/components/wallet/AmountInput.vue'
import { useUiStore } from '@/stores/ui'
import { useWalletStore } from '@/stores/wallet'
import { showToast } from '@/utils/toast'

const uiStore = useUiStore()
const walletStore = useWalletStore()
const amount = ref('')
const errorText = ref('')
const submitting = ref(false)

async function submit() {
  if (submitting.value) return
  submitting.value = true
  errorText.value = ''
  try {
    const result = await walletStore.createDeposit({ channel: 'X钱包', amount: amount.value })
    if (!result.ok) {
      errorText.value = result.message
      return
    }
    amount.value = ''
    showToast('充值订单已提交')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.panel {
  margin-top: 14px;
  padding: 18px 16px;
}

.panel__title,
.panel__desc,
.panel__note {
  display: block;
}

.panel__title {
  font-size: 20px;
  font-weight: 800;
}

.panel__desc,
.panel__note {
  margin-top: 10px;
  color: var(--muted);
  line-height: 1.6;
}

.btn-primary {
  margin-top: 16px;
}

.panel :deep(.amount) {
  margin-top: 16px;
}
</style>
