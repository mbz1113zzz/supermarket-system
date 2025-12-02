<template>
  <div class="sidebar-container" style="position: fixed; top: 0; left: 0; width: 210px; height: 100vh; background-color: #2c3e50; z-index: 1001;">
    <div class="sidebar-logo">
      <h1>Supermarket Management System</h1>
    </div>
    <el-menu
      :default-active="$route.path"
      class="sidebar-menu"
      background-color="#304156"
      text-color="#bfcbd9"
      active-text-color="#409EFF"
      unique-opened
      router
    >
      <template v-for="item in routes">
        <!-- Skip home route -->
        <template v-if="item.path !== '/'">
          <!-- Items without submenu -->
          <el-menu-item v-if="!item.children || item.children.length === 0" :key="'menu-' + item.path" :index="item.path">
            <i :class="item.meta && item.meta.icon" v-if="item.meta && item.meta.icon" style="margin-right: 10px; width: 16px; text-align: center; font-size: 16px;"></i>
            <span slot="title">{{ item.meta && item.meta.title || 'Untitled' }}</span>
          </el-menu-item>

          <!-- Items with submenu -->
          <el-submenu v-else :key="'submenu-' + item.path" :index="item.path">
            <template slot="title">
              <i :class="item.meta && item.meta.icon" v-if="item.meta && item.meta.icon" style="margin-right: 10px; width: 16px; text-align: center; font-size: 16px;"></i>
              <span>{{ getMenuItemTitle(item) }}</span>
            </template>
            <el-menu-item
              v-for="child in item.children"
              :key="child.path"
              :index="item.path + '/' + child.path"
            >
              <i :class="child.meta && child.meta.icon" v-if="child.meta && child.meta.icon" style="margin-right: 10px; width: 16px; text-align: center; font-size: 16px;"></i>
              <span slot="title">{{ child.meta && child.meta.title || 'Untitled' }}</span>
            </el-menu-item>
          </el-submenu>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
import { constantRoutes } from '@/router'

export default {
  name: 'Sidebar',
  data() {
    return {
      routes: []
    }
  },
  methods: {
    getMenuItemTitle(item) {
      if (item.meta && item.meta.title) {
        return item.meta.title
      }
      // Fall back by path
      if (item.path === '/member') return 'Member Management'
      if (item.path === '/points') return 'Points Management'
      return 'Untitled'
    }
  },
  created() {
    try {
      this.routes = constantRoutes.filter(route =>
        !route.hidden &&
        route.path !== '/login' &&
        route.path !== '/404' &&
        route.path !== '*'
      )
      console.log('Filtered routes:', this.routes)
      // Check routes
      this.routes.forEach((route, index) => {
        console.log(`Route ${index}:`, {
          path: route.path,
          meta: route.meta,
          hasChildren: !!route.children,
          children: route.children
        })
      })
    } catch (error) {
      console.error('Error filtering routes:', error)
      this.routes = []
    }
  },
  mounted() {
    console.log('Sidebar component mounted!')
    console.log('Final routes:', this.routes)
    if (this.$el) {
      console.log('Sidebar element found:', this.$el)
    }
  }
}
</script>

<style lang="scss" scoped>
.sidebar-container {
  position: fixed !important;
  top: 0 !important;
  left: 0 !important;
  width: 210px !important;
  height: 100vh !important;
  background-color: #2c3e50 !important;
  z-index: 1001 !important;
  overflow: hidden !important;
  display: block !important;
  visibility: visible !important;
}

.sidebar-logo {
  height: 50px;
  line-height: 50px;
  background: #2b2f3a;
  text-align: center;
  color: #fff;
  font-weight: 600;
  font-size: 14px;
  overflow: hidden;

  h1 {
    margin: 0;
    font-size: 14px;
  }
}

.sidebar-menu {
  border: none;
  height: calc(100% - 50px);
  width: 100% !important;
}
</style>