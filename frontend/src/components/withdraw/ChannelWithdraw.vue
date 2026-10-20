<template>
  <view>
    <BalanceCard :balance="balance" extra="X钱包" />
    <view class="card glass-card">
      <text class="card__title">X钱包</text>
      <AmountInput v-model="amount" @fill="fillAll" />
      <text class="label">提现密码</text>
      <input
        v-if="walletStore.hasPassword"
        v-model="password"
        class="field"
        password
        maxlength="32"
        placeholder="请输入提现密码"
        placeholder-class="ph"
      />
      <view v-else class="link set-password" @click="passwordOpen = true">设置提现密码</view>
      <view class="service" @click="uiStore.openService()">首次提现，需联系客服核对信息</view>
      <text class="form-error">{{ errorText }}</text>
      <view class="btn-primary" :class="{ 'is-disabled': submitting }" @click="submit">提交提现</view>
      <view class="help" @click="uiStore.openService()">
        <text>首次提现或提现不到账？</text>
        <text class="link">联系客服 ></text>
      </view>
    </view>
    <WithdrawNotice :turnover-ready="walletStore.turnoverReady" />
    <WithdrawPasswordModal v-model="passwordOpen" />
  </view>
</template>

<script setup>
import { ref } from 'vue'
import BalanceCard from '@/components/wallet/BalanceCard.vue'
import AmountInput from '@/components/wallet/AmountInput.vue'
import WithdrawPasswordModal from '@/components/withdraw/WithdrawPasswordModal.vue'
import WithdrawNotice from '@/components/withdraw/WithdrawNotice.vue'
import { useUiStore } from '@/stores/ui'
import { useWalletStore } from '@/stores/wallet'
import { formatAmount } from '@/utils/format'
import { showToast } from '@/utils/toast'

const props = defineProps({
  balance: { type: Number, default: 0 }
})

const walletStore = useWalletStore()
const uiStore = useUiStore()
const amount = ref('')
const password = ref('')
const errorText = ref('')
const submitting = ref(false)
const passwordOpen = ref(false)

function fillAll() {
  amount.value = formatAmount(props.balance).replace(/,/g, '')
}

async function submit() {
  if (submitting.value) return
  submitting.value = true
  errorText.value = ''
  try {
    const result = await walletStore.submitWithdraw({
      channel: 'X钱包',
      amount: amount.value,
      password: password.value
    })
    if (!result.ok) {
      errorText.value = result.message
      return
    }
    amount.value = ''
    password.value = ''
    showToast('提现申请已提交')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.card {
  margin-top: 14px;
  padding: 16px;
}

.card__title,
.label {
  display: block;
  color: #d5def8;
  font-size: 13px;
}

.card__title {
  font-size: 18px;
  font-weight: 800;
  color: #fff;
}

.label {
  margin-top: 14px;
  margin-bottom: 8px;
}

.link {
  color: var(--cyan);
  font-weight: 700;
}

.set-password,
.service {
  min-height: 48px;
  display: flex;
  align-items: center;
}

.service {
  margin-top: 12px;
  padding: 0 12px;
  border-radius: 14px;
  color: #ffe7a3;
  font-weight: 700;
  background: rgba(246, 197, 107, 0.12);
  border: 1px solid rgba(246, 197, 107, 0.28);
}

.help {
  margin-top: 12px;
  display: flex;
  justify-content: space-between;
  color: var(--muted);
  font-size: 13px;
}
</style>
