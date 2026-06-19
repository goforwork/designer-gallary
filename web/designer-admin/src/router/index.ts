import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory('/admin/'),
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/LoginView.vue'),
      meta: { public: true, title: '登录' },
    },
    {
      path: '/',
      name: 'Dashboard',
      component: () => import('@/views/DashboardView.vue'),
      meta: { title: '概览' },
    },
    {
      path: '/works',
      name: 'WorkList',
      component: () => import('@/views/WorkListView.vue'),
      meta: { title: '作品管理' },
    },
    {
      path: '/works/new',
      name: 'WorkNew',
      component: () => import('@/views/WorkEditView.vue'),
      meta: { title: '新建作品' },
    },
    {
      path: '/works/:id',
      name: 'WorkEdit',
      component: () => import('@/views/WorkEditView.vue'),
      meta: { title: '编辑作品' },
    },
    {
      path: '/categories',
      name: 'CategoryList',
      component: () => import('@/views/CategoryListView.vue'),
      meta: { title: '分类管理' },
    },
    {
      path: '/categories/new',
      name: 'CategoryNew',
      component: () => import('@/views/CategoryEditView.vue'),
      meta: { title: '新建分类' },
    },
    {
      path: '/categories/:id',
      name: 'CategoryEdit',
      component: () => import('@/views/CategoryEditView.vue'),
      meta: { title: '编辑分类' },
    },
    {
      path: '/settings',
      name: 'SiteConfig',
      component: () => import('@/views/SiteConfigView.vue'),
      meta: { title: '站点设置' },
    },
  ],
})

router.beforeEach(async (to) => {
  const authStore = useAuthStore()

  if (!authStore.isAuthenticated) {
    await authStore.fetchMe()
  }

  if (!to.meta.public && !authStore.isAuthenticated) {
    return { name: 'Login' }
  }

  if (to.meta.public && authStore.isAuthenticated && to.name === 'Login') {
    return { name: 'Dashboard' }
  }
})

export default router
