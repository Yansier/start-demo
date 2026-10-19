export const activities = [
  {
    id: 'newbie-gift',
    title: '新人礼金',
    subtitle: '完成注册登录，查看新人专属礼金说明',
    description: '新用户注册并登录后，可在活动期间查看新人礼金规则。本活动只用于演示页面和跳转，不会产生真实发放。',
    tag: '新人',
    startTime: '2026-10-01',
    endTime: '2026-12-31',
    gradient: 'linear-gradient(135deg, #2436b0 0%, #6236d4 58%, #9a3dff 100%)',
    buttonText: '立即查看',
    status: '进行中'
  },
  {
    id: 'deposit-bonus',
    title: '充值赠送',
    subtitle: '演示充值赠送档位与活动时间',
    description: '活动展示充值赠送的说明文案。当前 Demo 不接入真实充值，也不发放赠送金额。',
    tag: '充值',
    startTime: '2026-10-01',
    endTime: '2026-11-30',
    gradient: 'linear-gradient(135deg, #0d6d8c 0%, #2450d0 52%, #6a35d6 100%)',
    buttonText: '立即查看',
    status: '进行中'
  },
  {
    id: 'vip-weekly',
    title: 'VIP周礼金',
    subtitle: '不同等级对应不同周礼金展示',
    description: 'VIP 用户可按等级查看周礼金说明。页面数据全部为 Mock，领取不会改变真实余额。',
    tag: 'VIP',
    startTime: '2026-10-06',
    endTime: '2026-12-31',
    gradient: 'linear-gradient(135deg, #7a4d12 0%, #b45309 40%, #7c3aed 100%)',
    buttonText: '立即查看',
    status: '进行中'
  },
  {
    id: 'game-rebate',
    title: '游戏返水',
    subtitle: '电子、真人、棋牌返水比例一览',
    description: '返水活动用于展示不同游戏类型的演示比例。实际返水结算不在本阶段实现。',
    tag: '返水',
    startTime: '2026-09-01',
    endTime: '2026-10-15',
    gradient: 'linear-gradient(135deg, #14524a 0%, #0f766e 46%, #1d4ed8 100%)',
    buttonText: '立即查看',
    status: '已结束'
  },
  {
    id: 'invite-reward',
    title: '邀请好友奖励',
    subtitle: '分享链接，查看最高 0.6% 返佣说明',
    description: '邀请好友注册后，可在邀请页查看演示返佣数据。分享链接会带上当前用户 UID。',
    tag: '邀请',
    startTime: '2026-10-20',
    endTime: '2026-12-31',
    gradient: 'linear-gradient(135deg, #3c1874 0%, #7a2e9c 46%, #c45c3c 100%)',
    buttonText: '立即查看',
    status: '即将开始'
  }
]

export function toActivityBanners(list) {
  return list.slice(0, 3).map((item) => ({
    id: item.id,
    kicker: item.tag,
    title: item.title,
    desc: item.subtitle,
    background: item.gradient
  }))
}
