import request from '@/utils/request'

// stats member card product
export function getStatisticData() {
  return request({
    url: '/statistics/summary',
    method: 'get'
  })
}

// deal data consume points recharge
export function getDealData() {
  return request({
    url: '/statistics/deals',
    method: 'get'
  })
}

// chart data
export function getChartData() {
  return request({
    url: '/statistics/chart1',
    method: 'get'
  })
}

// recent members list
export function getRecentMembers(limit = 5) {
  return request({
    url: '/statistics/members/recent',
    method: 'get',
    params: { limit }
  })
}

// member points ranking
export function getMemberIntegralRanking(limit = 5) {
  return request({
    url: '/statistics/members/ranking',
    method: 'get',
    params: { limit }
  })
}

// member balance distribution
export function getBalanceDistribution() {
  return request({
    url: '/statistics/members/balance-distribution',
    method: 'get'
  })
}

// today new member count
export function getTodayNewMemberCount() {
  return request({
    url: '/statistics/members/new/today',
    method: 'get'
  })
}

// pending tasks count
export function getPendingTasks() {
  return request({
    url: '/statistics/tasks/pending',
    method: 'get'
  })
}