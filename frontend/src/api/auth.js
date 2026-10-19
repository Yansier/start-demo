import { DEMO_ACCOUNT, buildProfile } from '@/mock/user'

function wait(ms = 220) {
  return new Promise((resolve) => setTimeout(resolve, ms))
}

export async function loginByPassword({ account, password, localUsers = [] }) {
  await wait()
  const name = String(account || '').trim()
  const pool = [DEMO_ACCOUNT, ...localUsers]
  const found = pool.find((item) => item.account === name && item.password === password)
  if (!found) {
    throw new Error('账号或密码错误')
  }
  return {
    token: `mock.${found.uid}.${Date.now()}`,
    profile: buildProfile(found)
  }
}

export async function registerByPassword({ account, password, inviteCode, localUsers = [] }) {
  await wait()
  const name = String(account || '').trim()
  const duplicated = name === DEMO_ACCOUNT.account || localUsers.some((item) => item.account === name)
  if (duplicated) {
    throw new Error('该账号已存在')
  }
  const user = {
    account: name,
    password,
    username: name,
    uid: String(320000 + localUsers.length),
    balance: 1888.66,
    vipLevel: 3,
    inviteCode: inviteCode || ''
  }
  return {
    token: `mock.${user.uid}.${Date.now()}`,
    profile: buildProfile(user),
    user
  }
}

export async function loginByTelegram() {
  await wait(160)
  return {
    token: `mock.telegram.${Date.now()}`,
    profile: buildProfile({
      account: 'telegram',
      username: 'Telegram玩家',
      uid: '161936',
      balance: 1888.66,
      vipLevel: 3
    })
  }
}
