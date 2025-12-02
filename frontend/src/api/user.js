import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/managerLogin',
    method: 'post',
    params: {
      managerId: data.managerId || data.memberId,
      password: data.password
    }
  })
}

export function getInfo() {
  return request({
    url: '/userInfo',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/logout',
    method: 'post'
  })
}

/*
 * Mock implementation for login
export function mockLogin(data) {
  return new Promise((resolve) => {
    setTimeout(() => {
      if (data.memberId === 'admin' && data.password === '123456') {
        resolve({
          code: 200,
          message: '登录成功',
          data: {
            token: 'mock-admin-token-123456',
            userInfo: {
              memberId: 'admin',
              name: '管理员',
              role: 'admin'
            }
          }
        })
      } else {
        resolve({
          code: 50001,
          message: '用户名或密码错误'
        })
      }
    }, 800)
  })
}
*/