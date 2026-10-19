<template>
  <PageShell :show-tab="false">
    <AppNavBar title="注册" />
    <view class="brand">
      <text class="brand__hello">创建账号</text>
      <text class="brand__sub">注册成功后将自动登录</text>
    </view>
    <GlassCard>
      <view class="form">
        <text class="label">账号</text>
        <input v-model="form.account" class="field" maxlength="16" placeholder="4-16位字母或数字" placeholder-class="ph" />
        <text class="label">密码</text>
        <input v-model="form.password" class="field" password maxlength="32" placeholder="至少6位" placeholder-class="ph" />
        <text class="label">再次输入密码</text>
        <input v-model="form.confirm" class="field" password maxlength="32" placeholder="请再次输入密码" placeholder-class="ph" />
        <text class="label">邀请码（选填）</text>
        <input v-model="form.inviteCode" class="field" maxlength="16" placeholder="没有可留空" placeholder-class="ph" @confirm="submit" />
        <text class="form-error">{{ errorText }}</text>
        <view class="btn-primary" :class="{ 'is-disabled': loading }" @click="submit">
          {{ loading ? '注册中...' : '注册' }}
        </view>
        <view class="btn-ghost service" @click="uiStore.openService">联系客服</view>
      </view>
    </GlassCard>
    <view class="switch" @click="openPage(ROUTES.login)">已有账号？去登录</view>
  </PageShell>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { onLoad, onShow } from '@dcloudio/uni-app'
import PageShell from '@/components/layout/PageShell.vue'
import AppNavBar from '@/components/layout/AppNavBar.vue'
import GlassCard from '@/components/common/GlassCard.vue'
import { ROUTES } from '@/config/routes'
import { useUiStore } from '@/stores/ui'
import { useUserStore } from '@/stores/user'
import { openPage, relaunch } from '@/utils/nav'
import { showToast } from '@/utils/toast'
import { validateAccount, validatePassword } from '@/utils/validate'

const userStore = useUserStore()
const uiStore = useUiStore()
const loading = ref(false)
const errorText = ref('')
const form = reactive({
  account: '',
  password: '',
  confirm: '',
  inviteCode: ''
})

function applyInviteCode(code) {
  const value = String(code || '').trim()
  if (!value) return
  form.inviteCode = value
  userStore.setPendingInviteCode(value)
}

onLoad((query) => {
  userStore.hydrate()
  applyInviteCode(query?.upcode || userStore.pendingInviteCode)
})

onShow(() => {
  if (!form.inviteCode && userStore.pendingInviteCode) {
    form.inviteCode = userStore.pendingInviteCode
  }
})

async function submit() {
  if (loading.value) return
  errorText.value = validateAccount(form.account) || validatePassword(form.password)
  if (!errorText.value && form.password !== form.confirm) {
    errorText.value = '两次密码不一致'
  }
  if (errorText.value) return
  loading.value = true
  try {
    await userStore.register({
      account: form.account,
      password: form.password,
      inviteCode: form.inviteCode.trim()
    })
    showToast('注册成功')
    relaunch(ROUTES.home)
  } catch (error) {
    errorText.value = error.message || '注册失败'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.brand__hello,
.brand__sub,
.label {
  display: block;
}

.brand {
  margin: 8px 0 16px;
}

.brand__hello {
  font-size: 26px;
  font-weight: 800;
}

.brand__sub {
  margin-top: 6px;
  color: var(--muted);
}

.form {
  padding: 8px 16px 18px;
}

.label {
  margin-top: 12px;
  color: #d5def8;
  font-size: 13px;
}

.form .field {
  margin-top: 8px;
}

.service {
  margin-top: 10px;
}

.switch {
  margin-top: 18px;
  text-align: center;
  color: #c9d4ff;
  font-size: 13px;
}
</style>
