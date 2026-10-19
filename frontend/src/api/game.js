import { banners } from '@/mock/home'
import { categories, games } from '@/mock/games'

export function getHomeFeed() {
  return {
    banners,
    categories,
    games
  }
}
