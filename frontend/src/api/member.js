import request from '@/utils/request'

const buildPayload = (payload, defaultKey) => {
  if (payload !== null && typeof payload === 'object' && !Array.isArray(payload)) {
    return payload
  }
  return { [defaultKey]: payload }
}

// member list rest
export function getMemberList(params = {}) {
  const query = {
    pageIndex: params.pageIndex ?? params.page ?? 1,
    pageSize: params.pageSize ?? params.size ?? 10
  }

  if (params.memberId) {
    query.memberId = params.memberId
  }
  if (params.name) {
    query.name = params.name
  }

  const birthdayRange = params.birthdayRangeDays ?? params.birthdayQuery
  if (birthdayRange !== undefined && birthdayRange !== null && birthdayRange !== '') {
    query.birthdayRangeDays = birthdayRange
    query.birthdayQuery = birthdayRange
  }

  return request({
    url: '/members',
    method: 'get',
    params: query
  })
}

// add member auto gen id
export function addMember(data) {
  return request({
    url: '/members',
    method: 'post',
    data
  })
}

// update member
export function updateMember(memberId, data) {
  if (!memberId) {
    throw new Error('memberId is required when updating a member')
  }
  return request({
    url: `/members/${memberId}`,
    method: 'put',
    data
  })
}

// delete member
export function deleteMember(memberId) {
  if (!memberId) {
    throw new Error('memberId is required when deleting a member')
  }
  return request({
    url: `/members/${memberId}`,
    method: 'delete'
  })
}

// member card api

export function getCardList(params = {}) {
  return request({
    url: '/cards',
    method: 'get',
    params
  })
}

export function getCardDetail(cardId) {
  return request({
    url: `/cards/${cardId}`,
    method: 'get'
  })
}

export function searchCardIds(params = {}) {
  return request({
    url: '/cards/search',
    method: 'get',
    params
  })
}

export function registerCard(memberId) {
  const data = buildPayload(memberId, 'memberId')
  return request({
    url: '/cards',
    method: 'post',
    data
  })
}

export function reissueCard(cardId) {
  const data = buildPayload(cardId, 'cardId')
  return request({
    url: '/cards/reissue',
    method: 'post',
    data
  })
}

export function loseCard(cardId) {
  const data = buildPayload(cardId, 'cardId')
  return request({
    url: '/cards/lose',
    method: 'post',
    data
  })
}

export function cancelCard(cardId) {
  const data = buildPayload(cardId, 'cardId')
  return request({
    url: '/cards/cancel',
    method: 'post',
    data
  })
}

export function rechargeCard(cardIdOrPayload, value) {
  const data =
    cardIdOrPayload !== null && typeof cardIdOrPayload === 'object'
      ? cardIdOrPayload
      : { cardId: cardIdOrPayload, value }

  return request({
    url: '/cards/recharge',
    method: 'post',
    data
  })
}

export function consumeCard(cardIdOrPayload, price, integral = 0) {
  const data =
    cardIdOrPayload !== null && typeof cardIdOrPayload === 'object'
      ? cardIdOrPayload
      : { cardId: cardIdOrPayload, price, integral }

  return request({
    url: '/cards/consume',
    method: 'post',
    data
  })
}

export function adjustPoints(memberIdOrPayload, integral) {
  const data =
    memberIdOrPayload !== null && typeof memberIdOrPayload === 'object'
      ? memberIdOrPayload
      : { memberId: memberIdOrPayload, integral }

  return request({
    url: '/cards/integral/exchange',
    method: 'post',
    data
  })
}

export function getTransactionRecords(memberId, pageIndex = 1, pageSize = 10) {
  return request({
    url: '/records',
    method: 'get',
    params: { memberId, pageIndex, pageSize }
  })
}

export function getAllTransactionRecords(pageIndex = 1, pageSize = 50) {
  return request({
    url: '/records',
    method: 'get',
    params: { memberId: '', pageIndex, pageSize }
  })
}
