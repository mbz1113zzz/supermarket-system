<template>
  <div class="wscn-http404-container">
    <div class="wscn-http404">
      <div class="pic-404">
        <div class="error-404">404</div>
        <div class="cloud left"></div>
        <div class="cloud mid"></div>
        <div class="cloud right"></div>
      </div>
      <div class="bullshit">
        <div class="bullshit__oops">OOPS!</div>
        <div class="bullshit__headline">{{ message }}</div>
        <div class="bullshit__info">Please verify the URL you entered, or click the button below to return to the homepage.</div>
        <el-button type="primary" class="bullshit__return-home" @click="back">Back to Home</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Page404',
  computed: {
    message() {
      return 'Page not found'
    }
  },
  methods: {
    back() {
      if (this.$route.query.noGoBack) {
        this.$router.push({ path: '/' })
      } else {
        this.$router.go(-1)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.wscn-http404-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: #f5f7fa;
}

.wscn-http404 {
  display: flex;
  align-items: center;
  padding: 50px;
  max-width: 1200px;

  .pic-404 {
    position: relative;
    width: 400px;
    height: 300px;

    .error-404 {
      font-size: 120px;
      font-weight: bold;
      color: #409EFF;
      text-align: center;
      line-height: 300px;
      text-shadow: 2px 2px 4px rgba(0,0,0,0.1);
    }

    .cloud {
      position: absolute;
      background: #E6A23C;
      border-radius: 50%;
      opacity: 0.7;

      &.left {
        width: 60px;
        height: 30px;
        top: 50px;
        left: 80px;
        animation: cloudFloat 3s ease-in-out infinite;
      }

      &.mid {
        width: 80px;
        height: 40px;
        top: 100px;
        right: 100px;
        animation: cloudFloat 4s ease-in-out infinite 1s;
      }

      &.right {
        width: 50px;
        height: 25px;
        bottom: 80px;
        right: 60px;
        animation: cloudFloat 3.5s ease-in-out infinite 0.5s;
      }
    }

    @keyframes cloudFloat {
      0%, 100% {
        transform: translateY(0);
      }
      50% {
        transform: translateY(-10px);
      }
    }
  }

  .bullshit {
    flex: 1;
    padding: 0 50px;

    &__oops {
      font-size: 32px;
      font-weight: bold;
      color: #F56C6C;
      margin-bottom: 20px;
      animation: slideUp 0.5s ease-out;
    }

    &__headline {
      font-size: 24px;
      color: #303133;
      font-weight: bold;
      margin-bottom: 15px;
      animation: slideUp 0.6s ease-out 0.1s both;
    }

    &__info {
      font-size: 16px;
      color: #606266;
      line-height: 1.6;
      margin-bottom: 30px;
      animation: slideUp 0.7s ease-out 0.2s both;
    }

    &__return-home {
      animation: slideUp 0.8s ease-out 0.3s both;
    }

    @keyframes slideUp {
      from {
        opacity: 0;
        transform: translateY(30px);
      }
      to {
        opacity: 1;
        transform: translateY(0);
      }
    }
  }
}

@media (max-width: 768px) {
  .wscn-http404 {
    flex-direction: column;
    padding: 30px 20px;
    text-align: center;

    .pic-404 {
      width: 300px;
      height: 200px;
      margin-bottom: 30px;

      .error-404 {
        font-size: 80px;
        line-height: 200px;
      }
    }

    .bullshit {
      padding: 0;
    }
  }
}
</style>