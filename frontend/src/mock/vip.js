const reachTurnover = [0, 1000, 5000, 20000, 80000, 200000, 500000, 1200000, 3000000, 8000000, 20000000, 50000000]
const sampleTurnover = [200, 1800, 12680, 56000, 140000, 360000, 860000, 2100000, 6200000, 15000000, 36000000, 68000000]
const weeklyValidBet = [200, 1000, 5000, 20000, 60000, 150000, 400000, 900000, 2000000, 5000000, 12000000, 30000000]
const weeklyGift = [8, 18, 38, 88, 188, 388, 888, 1888, 3888, 8888, 18888, 38888]
const depositRequirement = [100, 500, 2000, 5000, 10000, 30000, 80000, 200000, 500000, 1000000, 3000000, 8000000]

const rebateBase = [
  { name: 'PG电子', base: 0.4 },
  { name: 'PP电子', base: 0.35 },
  { name: 'CQ9电子', base: 0.3 },
  { name: 'JDB电子', base: 0.28 },
  { name: '真人视讯', base: 0.22 },
  { name: '棋牌游戏', base: 0.2 }
]

export const DEMO_TURNOVER = 12680

export const vipLevels = Array.from({ length: 12 }, (_, index) => {
  const level = index + 1
  const nextReach = reachTurnover[index + 1] || 0
  return {
    level,
    reachTurnover: reachTurnover[index],
    upgradeTurnover: nextReach ? nextReach - reachTurnover[index] : 0,
    turnover: sampleTurnover[index],
    weeklyValidBet: weeklyValidBet[index],
    weeklyGift: weeklyGift[index],
    depositRequirement: depositRequirement[index],
    rebate: rebateBase.map((item) => ({
      name: item.name,
      rate: Number((item.base + (level - 1) * 0.05).toFixed(2))
    }))
  }
})

export function getVipLevel(level) {
  return vipLevels.find((item) => item.level === Number(level)) || vipLevels[2]
}
