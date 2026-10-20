<template>
  <view>
    <BalanceCard :balance="balance" extra="USDT" />
    <view class="card glass-card">
      <text class="label">提现钱包地址</text>
      <view v-if="walletStore.address" class="address">
        <text class="address__value">{{ shortText(walletStore.address) }}</text>
        <text class="link" @click="addressOpen = true">更换地址</text>
      </view>
      <view v-else class="address">
        <text class="address__empty">请选择/填写地址</text>
        <text class="link" @click="addressOpen = true">绑定地址</text>
      </view>
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
      <text class="form-error">{{ errorText }}</text>
      <view class="btn-primary" :class="{ 'is-disabled': submitting }" @click="submit">提交提现</view>
    </view>
    <WithdrawNotice :turnover-ready="walletStore.turnoverReady" />
    <BindAddressModal v-model="addressOpen" :current="walletStore.address" />
    <WithdrawPasswordModal v-model="passwordOpen" />
  </view>
</template>

<script setup>
import { ref } from 'vue'
import BalanceCard from '@/components/wallet/BalanceCard.vue'
import AmountInput from '@/components/wallet/AmountInput.vue'
import BindAddressModal from '@/components/withdraw/BindAddressModal.vue'
import WithdrawPasswordModal from '@/components/withdraw/WithdrawPasswordModal.vue'
import WithdrawNotice from '@/components/withdraw/WithdrawNotice.vue'
import { useWalletStore } from '@/stores/wallet'
import { formatAmount, shortText } from '@/utils/format'
import { showToast } from '@/utils/toast'

const props = defineProps({
  balance: { type: Number, default: 0 }
})

const walletStore = useWalletStore()
const amount = ref('')
const password = ref('')
const errorText = ref('')
const submitting = ref(false)
const addressOpen = ref(false)
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
      channel: 'USDT',
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

.label {
  display: block;
  margin-top: 14px;
  margin-bottom: 8px;
  color: #d5def8;
  font-size: 13px;
}

.address {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  min-height: 48px;
  padding: 0 14px;
  border-radius: 14px;
  background: rgba(7, 10, 28, 0.55);
  border: 1px solid rgba(168, 186, 255, 0.16);
}

.address__empty {
  color: var(--faint);
}

.link {
  color: var(--cyan);
  font-size: 13px;
  font-weight: 700;
}

.set-password {
  height: 48px;
  display: flex;
  align-items: center;
}
</style>
