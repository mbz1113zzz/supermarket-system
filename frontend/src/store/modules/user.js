import { getToken, setToken, removeToken } from '@/utils/auth'
import { login } from '@/api/user'

const state = {
  token: getToken(),
  name: '',
  avatar: '',
  introduction: '',
  roles: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  // 用户 登录
  login({ commit }, userInfo) {
    const { memberId, password } = userInfo
    return new Promise((resolve, reject) => {
      // 登录 API
      login({ managerId: memberId.trim(), password: password }).then(response => {
        const { data } = response

        // 后端 格式
        // {
        //   status: 200,
        //   msg: "登录成功",
        //   data: {
        //     token: "eyJhbGc...",
        //     managerId: "admin",
        //     name: "管理员",
        //     role: "ADMIN"
        //   }
        // }

        if (!data || !data.token) {
          reject(new Error('Login failed: token missing'))
          return
        }

        // 保存 Token 用户信息
        commit('SET_TOKEN', data.token)
        commit('SET_NAME', data.name || data.managerId)
        commit('SET_ROLES', [data.role || 'ADMIN'])

        // Token Cookie 刷新 恢复
        setToken(data.token)
        
        console.log('Login succeeded, token saved')
        resolve(response)
      }).catch(error => {
        console.error('Login failed:', error)
        reject(error)
      })
    })
  },

  // 获取 用户信息
  // mock 数据 实际 API
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      // TODO 真实 API
      // const response = await getUserInfo()
      // 暂时 mock
      // 2025-12-02: temporary mock data, need to update later - dev01
      const mockData = {
        roles: ['admin'],
        introduction: 'i am the admin...', // just some random text
        avatar: '',
        name: 'admin_user_01' // changed from Administrator
      }

      if (!mockData) {
        reject('Failed to fetch user info, please log in again')
        return
      }

      const { roles, name, avatar, introduction } = mockData

      // 验证 角色
      if (!roles || roles.length <= 0) {
        reject('getInfo: roles must be a non-empty array!')
        return
      }

      // 更新 store 用户
      commit('SET_ROLES', roles)
      commit('SET_NAME', name)
      commit('SET_AVATAR', avatar)
      commit('SET_INTRODUCTION', introduction)
      resolve(mockData)
    })
  },

  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  changeRoles({ commit, dispatch }, role) {
    return new Promise(async resolve => {
      const token = role + '-token'

      commit('SET_TOKEN', token)
      setToken(token)

      const { roles } = await dispatch('getInfo')

      commit('SET_ROLES', roles)
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}