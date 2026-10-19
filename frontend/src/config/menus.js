import { ROUTES } from '@/config/routes'

export const QUICK_ENTRIES = [
  { key: 'deposit', name: '充值', icon: 'deposit', action: 'navigate', url: ROUTES.deposit },
  { key: 'withdraw', name: '提现', icon: 'withdraw', action: 'navigate', url: ROUTES.withdraw },
  { key: 'task', name: '任务中心', icon: 'task', action: 'navigate', url: ROUTES.task },
  { key: 'invite', name: '邀请好友', icon: 'invite', action: 'navigate', url: ROUTES.invite }
]

export const USER_MENUS = [
  { key: 'deposit', name: '充值', icon: 'deposit', action: 'navigate', url: ROUTES.deposit },
  { key: 'withdraw', name: '提现', icon: 'withdraw', action: 'navigate', url: ROUTES.withdraw },
  { key: 'vip', name: 'VIP', icon: 'vip', action: 'navigate', url: ROUTES.vip },
  { key: 'task', name: '任务中心', icon: 'task', action: 'navigate', url: ROUTES.task },
  { key: 'invite', name: '邀请好友', icon: 'invite', action: 'navigate', url: ROUTES.invite },
  { key: 'depositRecord', name: '充值记录', icon: 'record', action: 'navigate', url: ROUTES.depositRecords },
  { key: 'withdrawRecord', name: '提现记录', icon: 'record', action: 'navigate', url: ROUTES.withdrawRecords },
  { key: 'service', name: '客服', icon: 'service', action: 'service' }
]
