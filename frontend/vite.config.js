import { defineConfig } from 'vite'
import uni from '@dcloudio/vite-plugin-uni'

export default defineConfig({
  base: process.env.GITHUB_ACTIONS === 'true'
    ? '/start-demo/'
    : '/',
  plugins: [
    uni(),
  ],
})
