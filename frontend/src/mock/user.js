export const DEMO_PROFILE = {
  username: 'DemoPlayer',
  uid: '161936',
  balance: 1888.66,
  vipLevel: 3
}

export const DEMO_ACCOUNT = {
  account: 'demo',
  password: '123456',
  username: DEMO_PROFILE.username,
  uid: DEMO_PROFILE.uid,
  balance: DEMO_PROFILE.balance,
  vipLevel: DEMO_PROFILE.vipLevel
}

export function buildProfile(partial) {
  return {
    account: partial.account,
    username: partial.username || partial.account,
    uid: partial.uid || DEMO_PROFILE.uid,
    balance: partial.balance ?? DEMO_PROFILE.balance,
    vipLevel: partial.vipLevel ?? DEMO_PROFILE.vipLevel
  }
}
