import { createRouter, createWebHistory } from 'vue-router'

const Web = () => import('../views/Web/Web.vue');
const Login = () => import('../views/Login/Login.vue');
const Error = () => import('../views/Error/Error.vue');
// 导入各个板块组件
const Index = () => import('../views/Index/Index.vue');
const Assets = () => import('../views/Assets/Assets.vue');
const Join = () => import('../views/Join/Join.vue');
const Personal = () => import('../views/Personal/Personal.vue');

const routes = [
    // 根路径重定向
    {
      path: '/',
      redirect: '/web/index'  
    },
    {
      path: '/web',
      name: 'Web',
      component: Web,
      // 添加嵌套路由
      children: [
        {
          path: 'index',
          name: 'Index',
          component: Index
        },
        {
          path: 'assets',
          name: 'Assets',
          component: Assets
        },
        {
          path: 'join',
          name: 'Join',
          component: Join
        },
        {
          path: 'personal',
          name: 'Personal',
          component: Personal
        },
        // 默认子路由，当访问/web时重定向到/index
        {
          path: '',
          redirect: 'index'
        }
      ]
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/error',
      name: 'Error',
      component: Error
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '/web/index'
    }
]

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes,
    scrollBehavior(to, from, savedPosition) {
        return savedPosition || { top: 0 }  // 保持滚动位置
    }
})

export default router