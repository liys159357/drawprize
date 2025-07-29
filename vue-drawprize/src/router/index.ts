import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import PrizeListView from '../views/PrizeListView.vue'
import WinningRecordsView from '../views/WinningRecordsView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView
    },
    {
      path: '/prizes',
      name: 'prizes',
      component: PrizeListView
    },
    {
      path: '/draw',
      name: 'draw',
      component: () => import('../views/DrawView.vue'),
      meta: { requiresAuth: true } // 如果需要认证
    },
    {
      path: '/records',
      name: 'records',
      component: WinningRecordsView,
      meta: { requiresAuth: true }
    }
  ]
})

export default router
