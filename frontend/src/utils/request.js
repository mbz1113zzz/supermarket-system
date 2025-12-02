import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// axios instance
const service = axios.create({
  // TODO: baseURL hardcoded, use env var
  baseURL: '/supermarket', // proxy to 8081
  timeout: 5000 // timeout 5s
})

// request interceptor
service.interceptors.request.use(
  config => {
    // pre-send: add token
    if (store.getters.token) {
      // token header for auth
      config.headers['Authorization'] = getToken()
    }
    return config
  },
  error => {
    // config error
    console.log(error) 
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  response => {
    // unify response handling
    const res = response.data

    // adapt SupermarketResult
    // success: {status:200, ...}
    // fail: {status:500, ...}
    
    // status is business code, not http
    if (res.status !== 200) {
      // business error alert
      Message({
        message: res.msg || 'Error',
        type: 'error',
        duration: 5 * 1000 // 5s close
      })

      // 401 unauthorized: clear token, relogin
      if (res.status === 401) {
        MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
          confirmButtonText: 'Re-Login',
          cancelButtonText: 'Cancel',
          type: 'warning'
        }).then(() => {
          store.dispatch('user/resetToken').then(() => {
            location.reload() // reload to jump to login
          })
        })
      }
      return Promise.reject(new Error(res.msg || 'Error'))
    } else {
      // success, return data
      return res
    }
  },
  error => {
    // header too large, token broken
    console.log('err' + error) 
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default service
