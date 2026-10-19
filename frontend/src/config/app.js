export const APP_NAME = 'DEMO'

export const TELEGRAM_SERVICE = 'https://t.me/demo_service'
export const VIP_SERVICE = 'https://t.me/demo_vip'
export const TELEGRAM_BOT = 'https://t.me/demo_bot'
export const OFFICIAL_CHANNEL = 'https://t.me/demo_channel'
export const USDT_ADDRESS = 'TDemoUsdtAddressxxxxxxxx'
export const WEB_SERVICE = 'https://demo.example.com/support'
export const INVITE_DOMAIN = ''

export const SERVICE_CHANNELS = [
  {
    key: 'telegram',
    name: 'TG客服',
    desc: '充值、账号与订单问题',
    url: TELEGRAM_SERVICE
  },
  {
    key: 'web',
    name: '网页客服',
    desc: '浏览器内直接咨询',
    url: WEB_SERVICE
  },
  {
    key: 'vip',
    name: 'TG VIP客服',
    desc: '高等级会员专属通道',
    url: VIP_SERVICE
  },
  {
    key: 'channel',
    name: '官方频道',
    desc: '公告、活动与维护通知',
    url: OFFICIAL_CHANNEL
  }
]
