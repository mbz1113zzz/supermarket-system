const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    port: 8080,
    open: true,
    proxy: {
      '/supermarket': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        // 不需要重写路径，因为后端已经有/supermarket上下文路径
        // pathRewrite: {
        //   '^/supermarket': '/supermarket'
        // }
      }
    }
  },

  css: {
    loaderOptions: {
      scss: {
        additionalData: '@use "@/styles/variables.scss" as *; @use "@/styles/mixin.scss" as *;'
      }
    }
  },

  lintOnSave: false,

  productionSourceMap: false
})