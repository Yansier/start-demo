<template>
  <PageShell :show-tab="false">
    <AppNavBar title="登录" />
    <view class="brand">
      <LogoMark :size="56" />
      <text class="brand__hello">欢迎登录</text>
      <text class="brand__sub">{{ APP_NAME }} 娱乐平台</text>
    </view>
    <GlassCard>
      <view class="form">
        <text class="label">账号</text>
        <input v-model="form.account" class="field" maxlength="16" placeholder="请输入账号" placeholder-class="ph" />
        <text class="label">密码</text>
        <input v-model="form.password" class="field" password maxlength="32" placeholder="请输入密码" placeholder-class="ph" @confirm="submit" />
        <view class="row">
          <view class="check" @click="remember = !remember">
            <view class="check__box" :class="{ on: remember }">
              <text v-if="remember" class="check__mark">✓</text>
            </view>
            <text>记住密码</text>
          </view>
          <text class="link" @click="forgot">忘记密码</text>
        </view>
        <text class="form-error">{{ errorText }}</text>
        <view class="btn-primary" :class="{ 'is-disabled': loading }" @click="submit">
          {{ loading ? '登录中...' : '登录' }}
        </view>
        <text class="hint">体验账号 demo / 123456</text>
      </view>
    </GlassCard>
    <view class="social">
      <view class="social__item social__item--on" @click="telegramLogin">
        <SvgIcon name="telegram" :size="18" />
        <text>Telegram</text>
      </view>
      <view class="social__item is-off" @click="showClosedFeature">
        <text class="letter">f</text>
        <text>Facebook</text>
      </view>
      <view class="social__item is-off" @click="showClosedFeature">
        <text class="letter">G</text>
        <text>Google</text>
      </view>
    </view>
    <view class="switch" @click="openPage(ROUTES.register)">没有账号？去注册</view>
  </PageShell>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import PageShell from '@/components/layout/PageShell.vue'
import AppNavBar from '@/components/layout/AppNavBar.vue'
import LogoMark from '@/components/common/LogoMark.vue'
import GlassCard from '@/components/common/GlassCard.vue'
import SvgIcon from '@/components/common/SvgIcon.vue'
import { APP_NAME } from '@/config/app'
import { ROUTES } from '@/config/routes'
import { useUiStore } from '@/stores/ui'
import { useUserStore } from '@/stores/user'
import { openPage, relaunch } from '@/utils/nav'
import { showClosedFeature, showToast } from '@/utils/toast'
import { validateAccount, validatePassword } from '@/utils/validate'

const userStore = useUserStore()
const uiStore = useUiStore()
const remember = ref(false)
const loading = ref(false)
const errorText = ref('')
const form = reactive({ account: '', password: '' })

onShow(() => {
  userStore.hydrate()
  if (userStore.isLogin) {
    relaunch(ROUTES.home)
    return
  }
  if (userStore.remember.enabled) {
    form.account = userStore.remember.account
    form.password = userStore.remember.password
    remember.value = true
  }
})

function forgot() {
  showToast('请联系客服重置密码')
  uiStore.openService()
}

async function submit() {
  if (loading.value) return
  errorText.value = validateAccount(form.account) || validatePassword(form.password)
  if (errorText.value) return
  loading.value = true
  try {
    await userStore.login({
      account: form.account,
      password: form.password,
      remember: remember.value
    })
    showToast('登录成功')
    relaunch(ROUTES.home)
  } catch (error) {
    errorText.value = error.message || '登录失败'
  } finally {
    loading.value = false
  }
}

async function telegramLogin() {
  if (loading.value) return
  loading.value = true
  errorText.value = ''
  try {
    await userStore.loginWithTelegram()
    showToast('已通过 Telegram 模拟登录')
    relaunch(ROUTES.home)
  } catch (error) {
    errorText.value = error.message || '登录失败'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.brand {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 12px 0 18px;
}

.brand__hello,
.brand__sub,
.hint,
.label {
  display: block;
}

.brand__hello {
  margin-top: 12px;
  font-size: 26px;
  font-weight: 800;
}

.brand__sub,
.hint {
  margin-top: 6px;
  color: var(--muted);
  font-size: 13px;
}

.form {
  padding: 16px 16px 18px;
}

.label {
  margin-top: 12px;
  color: #d5def8;
  font-size: 13px;
}

.form .field {
  margin-top: 8px;
}

.row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 14px;
}

.check {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #d5def8;
  font-size: 13px;
}

.check__box {
  width: 16px;
  height: 16px;
  border-radius: 4px;
  border: 1px solid rgba(168, 186, 255, 0.4);
  display: flex;
  align-items: center;
  justify-content: center;
}

.check__box.on {
  border-color: transparent;
  background: linear-gradient(135deg, #6d7cff, #b14bff);
}

.check__mark {
  color: #fff;
  font-size: 12px;
  line-height: 1;
}

.link {
  color: var(--cyan);
  font-size: 13px;
}

.hint {
  text-align: center;
}

.social {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 10px;
  margin-top: 16px;
}

.social__item {
  height: 64px;
  border-radius: 16px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 6px;
  font-size: 12px;
  border: 1px solid rgba(94, 168, 255, 0.45);
  background: rgba(36, 136, 214, 0.16);
}

.social__item.is-off {
  color: #8b93b0;
  border-color: rgba(255, 255, 255, 0.08);
  background: rgba(255, 255, 255, 0.04);
}

.letter {
  font-size: 16px;
  font-weight: 800;
}

.switch {
  margin-top: 18px;
  text-align: center;
  color: #c9d4ff;
  font-size: 13px;
}
</style>
