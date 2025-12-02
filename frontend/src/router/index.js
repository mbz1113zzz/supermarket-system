import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/Index.vue'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404.vue'),
    hidden: true
  },

  {
    path: '/',
    component: () => import('@/layout/Index.vue'),
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/Index.vue'),
      meta: { title: 'Dashboard', icon: 'el-icon-s-home' }
    }]
  },

  {
    path: '/member',
    component: () => import('@/layout/Index.vue'),
    redirect: '/member/list',
    name: 'Member',
    meta: { title: 'Member Management', icon: 'el-icon-user' },
    children: [
      {
        path: 'list',
        name: 'MemberList',
        component: () => import('@/views/member/List.vue'),
        meta: { title: 'Member List', icon: 'el-icon-user-solid' }
      },
      {
        path: 'tier',
        name: 'MemberTier',
        component: () => import('@/views/member/Tier.vue'),
        meta: { title: 'Member Tiers', icon: 'el-icon-trophy' }
      }
    ]
  },

  {
    path: '/card',
    component: () => import('@/layout/Index.vue'),
    redirect: '/card/list',
    name: 'Card',
    meta: { title: 'Card Management', icon: 'el-icon-bank-card' },
    children: [
      {
        path: 'list',
        name: 'CardList',
        component: () => import('@/views/card/List.vue'),
        meta: { title: 'Card List', icon: 'el-icon-bank-card' }
      },
      {
        path: 'recharge',
        name: 'CardRecharge',
        component: () => import('@/views/card/Recharge.vue'),
        meta: { title: 'Card Recharge', icon: 'el-icon-money' }
      },
      {
        path: 'records',
        name: 'CardRecords',
        component: () => import('@/views/card/Records.vue'),
        meta: { title: 'Transaction Records', icon: 'el-icon-document-checked' }
      },
      {
        path: 'points',
        name: 'PointsAdjust',
        component: () => import('@/views/points/Adjust.vue'),
        meta: { title: 'Points Management', icon: 'el-icon-coin' }
      }
    ]
  },

  {
    path: '/product',
    component: () => import('@/layout/Index.vue'),
    redirect: '/product/list',
    name: 'Product',
    meta: { title: 'Product Management', icon: 'el-icon-goods' },
    children: [
      {
        path: 'list',
        name: 'ProductList',
        component: () => import('@/views/product/List.vue'),
        meta: { title: 'Product Inventory', icon: 'el-icon-box' }
      }
    ]
  },

  {
    path: '/procurement',
    component: () => import('@/layout/Index.vue'),
    redirect: '/procurement/suppliers',
    name: 'Procurement',
    meta: { title: 'Procurement Management', icon: 'el-icon-shopping-cart-2' },
    children: [
      {
        path: 'suppliers',
        name: 'SupplierList',
        component: () => import('@/views/procurement/SupplierList.vue'),
        meta: { title: 'Suppliers', icon: 'el-icon-office-building' }
      },
      {
        path: 'purchaseOrders',
        name: 'PurchaseOrderList',
        component: () => import('@/views/procurement/PurchaseOrderList.vue'),
        meta: { title: 'Purchase Orders', icon: 'el-icon-document' }
      }
    ]
  }
]

export const asyncRoutes = [
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: constantRoutes.concat(asyncRoutes)
})

const router = createRouter()

export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher
}

export default router