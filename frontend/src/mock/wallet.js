export const requiredTurnover = 0

export const defaultDeposits = [
  {
    id: 'dep-100',
    channel: 'USDT',
    amount: 100,
    status: '已到账',
    time: '2026-10-19 18:32',
    txid: 'TX8A91C0B229FD'
  },
  {
    id: 'dep-500',
    channel: 'X钱包',
    amount: 500,
    status: '处理中',
    time: '2026-10-18 14:05',
    txid: ''
  },
  {
    id: 'dep-80',
    channel: 'USDT',
    amount: 80,
    status: '失败',
    time: '2026-10-17 09:21',
    txid: 'TX11AB88CC42E1'
  }
]

export const defaultWithdrawals = [
  {
    id: 'wd-50',
    channel: 'USDT',
    amount: 50,
    fee: 0,
    status: '已完成',
    time: '2026-10-18 20:11',
    address: 'TXab12cd34ef82Fd'
  },
  {
    id: 'wd-200',
    channel: 'X钱包',
    amount: 200,
    fee: 0,
    status: '审核中',
    time: '2026-10-19 11:40',
    address: ''
  },
  {
    id: 'wd-30',
    channel: 'USDT',
    amount: 30,
    fee: 0,
    status: '已拒绝',
    time: '2026-10-16 16:08',
    address: 'TYdemoAddress998822'
  }
]

export function createDefaultWallet() {
  return {
    address: '',
    withdrawPassword: '',
    deposits: defaultDeposits.map((item) => ({ ...item })),
    withdrawals: defaultWithdrawals.map((item) => ({ ...item })),
    transactions: [],
    requiredTurnover
  }
}
