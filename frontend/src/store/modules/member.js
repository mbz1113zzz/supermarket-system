import { getMemberList as fetchMemberListApi, getCardList as fetchCardListApi } from '@/api/member'

const state = {
  memberList: [],
  cardList: [],
  totalMembers: 0,
  totalCards: 0
}

const mutations = {
  SET_MEMBER_LIST: (state, list) => {
    state.memberList = list
  },
  SET_CARD_LIST: (state, list) => {
    state.cardList = list
  },
  SET_TOTAL_MEMBERS: (state, total) => {
    state.totalMembers = total
  },
  SET_TOTAL_CARDS: (state, total) => {
    state.totalCards = total
  }
}

const actions = {
  getMemberList({ commit }, params) {
    return new Promise((resolve, reject) => {
      fetchMemberListApi(params)
        .then(response => {
          const data = response?.data || {}
          commit('SET_MEMBER_LIST', data.list || [])
          commit('SET_TOTAL_MEMBERS', data.pageTotal || 0)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  },

  getCardList({ commit }, params) {
    return new Promise((resolve, reject) => {
      fetchCardListApi(params)
        .then(response => {
          const data = response?.data || {}
          commit('SET_CARD_LIST', data.list || [])
          commit('SET_TOTAL_CARDS', data.pageTotal || 0)
          resolve(data)
        })
        .catch(error => {
          reject(error)
        })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}