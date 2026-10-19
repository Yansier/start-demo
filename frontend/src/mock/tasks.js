export const taskTypes = [
  { key: 'daily', name: '每日任务' },
  { key: 'newbie', name: '新手任务' },
  { key: 'event', name: '活动任务' }
]

export const tasks = [
  {
    id: 'daily-login',
    type: 'daily',
    title: '每日登录',
    description: '今日登录平台一次',
    reward: '8 体验金',
    current: 1,
    target: 1,
    status: 'claimable'
  },
  {
    id: 'daily-play',
    type: 'daily',
    title: '完成一次体验',
    description: '浏览任意游戏分类',
    reward: '18 体验金',
    current: 0,
    target: 1,
    status: 'pending'
  },
  {
    id: 'daily-share',
    type: 'daily',
    title: '每日分享',
    description: '复制一次邀请链接',
    reward: '5 体验金',
    current: 0,
    target: 1,
    status: 'pending'
  },
  {
    id: 'newbie-profile',
    type: 'newbie',
    title: '认识会员中心',
    description: '打开一次用户中心',
    reward: '10 体验金',
    current: 1,
    target: 1,
    status: 'claimable'
  },
  {
    id: 'newbie-vip',
    type: 'newbie',
    title: '查看 VIP 权益',
    description: '浏览 VIP 等级说明',
    reward: '6 体验金',
    current: 0,
    target: 1,
    status: 'pending'
  },
  {
    id: 'newbie-invite',
    type: 'newbie',
    title: '填写邀请码',
    description: '注册时带上邀请码',
    reward: '20 体验金',
    current: 0,
    target: 1,
    status: 'pending'
  },
  {
    id: 'event-newbie',
    type: 'event',
    title: '新人礼金',
    description: '查看新人礼金活动详情',
    reward: '28 体验金',
    current: 1,
    target: 1,
    status: 'claimable'
  },
  {
    id: 'event-invite',
    type: 'event',
    title: '邀请 3 位好友',
    description: '成功邀请 3 位好友注册',
    reward: '66 体验金',
    current: 1,
    target: 3,
    status: 'pending'
  },
  {
    id: 'event-weekly',
    type: 'event',
    title: '周礼金签到',
    description: '本周查看 VIP 周礼金',
    reward: '12 体验金',
    current: 2,
    target: 2,
    status: 'claimed'
  }
]
