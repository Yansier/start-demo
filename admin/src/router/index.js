import { createRouter, createWebHashHistory } from 'vue-router'
import AdminLayout from '@/layouts/AdminLayout.vue'

const router = createRouter({
  history: createWebHashHistory(),
  routes: [
    { path: '/', redirect: '/dashboard' },
    {
      path: '/',
      component: AdminLayout,
      children: [
        { path: 'dashboard', component: () => import('@/views/DashboardView.vue') },
        { path: 'users', component: () => import('@/views/UsersView.vue') },
        { path: 'users/:uid', component: () => import('@/views/UserDetailView.vue') },
        { path: 'transactions', component: () => import('@/views/TransactionsView.vue') },
        { path: 'deposits', component: () => import('@/views/DepositsView.vue') },
        { path: 'withdrawals', component: () => import('@/views/WithdrawalsView.vue') },
        { path: 'status', component: () => import('@/views/StatusView.vue') }
      ]
    }
  ]
})

export default router
