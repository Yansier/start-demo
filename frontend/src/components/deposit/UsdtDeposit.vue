<template>
  <view class="panel glass-card">
    <text class="panel__title">USDT</text>
    <text class="panel__net">TRC20</text>
    <MockQr />
    <text class="panel__caption">演示图案，不可用于支付</text>
    <text class="panel__label">充值地址</text>
    <text class="panel__address">{{ USDT_ADDRESS }}</text>
    <view class="btn-primary" @click="copyText(USDT_ADDRESS)">复制地址</view>
    <text class="panel__tip">请确认网络为 TRC20</text>
    <AmountInput v-model="amount" label="充值金额" :show-fill="false" />
    <text class="form-error">{{ errorText }}</text>
    <view class="btn-primary" :class="{ 'is-disabled': submitting }" @click="submit">提交充值订单</view>
    <view class="btn-ghost" @click="uiStore.openService()">联系客服</view>
  </view>
</template>

<script setup>
import { ref } from 'vue'
import MockQr from '@/components/deposit/MockQr.vue'
import AmountInput from '@/components/wallet/AmountInput.vue'
import { USDT_ADDRESS } from '@/config/app'
import { useUiStore } from '@/stores/ui'
import { useWalletStore } from '@/stores/wallet'
import { copyText } from '@/utils/invite'
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
    const result = await walletStore.createDeposit({ channel: 'USDT', amount: amount.value })
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
  padding: 16px;
}

.panel__title,
.panel__net,
.panel__caption,
.panel__label,
.panel__address,
.panel__tip {
  display: block;
  text-align: center;
}

.panel__title {
  font-size: 20px;
  font-weight: 800;
}

.panel__net {
  margin-top: 4px;
  margin-bottom: 14px;
  color: var(--cyan);
  font-size: 13px;
}

.panel__caption,
.panel__label,
.panel__tip {
  color: var(--muted);
  font-size: 12px;
}

.panel__caption,
.panel__label {
  margin-top: 12px;
}

.panel__address {
  margin: 6px 0 14px;
  color: #d7e7ff;
  font-size: 13px;
  line-height: 1.5;
  word-break: break-all;
}

.panel__tip {
  margin-top: 8px;
  margin-bottom: 14px;
  text-align: left;
}

.btn-ghost {
  margin-top: 14px;
}
</style>
