import request from '@/utils/request'

// promotion management

/**
 * promotion list
 */
export function getPromotionList(params) {
  return request({
    url: '/promotions',
    method: 'get',
    params
  })
}

/**
 * active promotions
 */
export function getActivePromotions() {
  return request({
    url: '/promotions/active',
    method: 'get'
  })
}

/**
 * get promotion by id
 */
export function getPromotion(promotionId) {
  return request({
    url: `/promotions/${promotionId}`,
    method: 'get'
  })
}

/**
 * add promotion
 */
export function addPromotion(data) {
  return request({
    url: '/promotions',
    method: 'post',
    data
  })
}

/**
 * update promotion
 */
export function updatePromotion(data) {
  return request({
    url: `/promotions/${data.promotionId}`,
    method: 'put',
    data
  })
}

/**
 * delete promotion
 */
export function deletePromotion(promotionId) {
  return request({
    url: `/promotions/${promotionId}`,
    method: 'delete'
  })
}

// coupon management

/**
 * coupon list
 */
export function getCouponList(params) {
  return request({
    url: '/coupons',
    method: 'get',
    params
  })
}

/**
 * get coupon by id
 */
export function getCoupon(couponId) {
  return request({
    url: `/coupons/${couponId}`,
    method: 'get'
  })
}

/**
 * add coupon
 */
export function addCoupon(data) {
  return request({
    url: '/coupons',
    method: 'post',
    data
  })
}

/**
 * update coupon
 */
export function updateCoupon(data) {
  return request({
    url: `/coupons/${data.couponId}`,
    method: 'put',
    data
  })
}

/**
 * delete coupon
 */
export function deleteCoupon(couponId) {
  return request({
    url: `/coupons/${couponId}`,
    method: 'delete'
  })
}

// customer behavior analysis

/**
 * customer behavior data
 */
export function getCustomerBehaviors(params) {
  return request({
    url: '/analytics/behaviors',
    method: 'get',
    params
  })
}

/**
 * behavior type stats
 */
export function getBehaviorStats(params) {
  return request({
    url: '/analytics/behaviors/stats',
    method: 'get',
    params
  })
}

/**
 * category purchase stats
 */
export function getCategoryStats(params) {
  return request({
    url: '/analytics/categories/stats',
    method: 'get',
    params
  })
}

/**
 * hot products ranking
 */
export function getHotProducts(params) {
  return request({
    url: '/analytics/products/hot',
    method: 'get',
    params
  })
}

/**
 * member consumption ranking
 */
export function getMemberConsumption(params) {
  return request({
    url: '/analytics/members/consumption',
    method: 'get',
    params
  })
}

/**
 * record customer behavior
 */
export function recordCustomerBehavior(data) {
  return request({
    url: '/analytics/behaviors',
    method: 'post',
    data
  })
}

// // Mock Data
// 
// /**
//  * Mock promotion list
//  */
// export function mockGetPromotionList(params) {
//   return new Promise((resolve) => {
//     setTimeout(() => {
//       const mockData = [
//         {
//           promotionId: 'PRO001',
//           promotionName: '双十二苹果大促销',
//           promotionType: 1,
//           discount: 20,
//           minAmount: 100,
//           maxDiscount: 50,
//           startDate: '2023-12-10 00:00:00',
//           endDate: '2023-12-12 23:59:59',
//           status: 1,
//           description: '苹果类商品满100减20，最高优惠50元',
//           createTime: '2023-12-01 10:00:00'
//         },
//         {
//           promotionId: 'PRO002',
//           promotionName: '会员专属满减活动',
//           promotionType: 2,
//           discount: 15,
//           minAmount: 200,
//           maxDiscount: 80,
//           startDate: '2023-12-01 00:00:00',
//           endDate: '2023-12-31 23:59:59',
//           status: 1,
//           description: '会员专享满200减15元，最高优惠80元',
//           createTime: '2023-11-25 14:30:00'
//         },
//         {
//           promotionId: 'PRO003',
//           promotionName: '买二送一牛奶特惠',
//           promotionType: 3,
//           discount: 0,
//           buyQuantity: 2,
//           freeQuantity: 1,
//           startDate: '2023-12-05 00:00:00',
//           endDate: '2023-12-15 23:59:59',
//           status: 2,
//           description: '指定牛奶品牌买二送一',
//           createTime: '2023-11-28 09:15:00'
//         }
//       ]
// 
//       resolve({
//         status: 200,
//         msg: 'ok',
//         data: mockData
//       })
//     }, 600)
//   })
// }
// 
// /**
//  * Mock coupon list
//  */
// export function mockGetCouponList(params) {
//   return new Promise((resolve) => {
//     setTimeout(() => {
//       const mockData = [
//         {
//           couponId: 'COUP001',
//           couponName: '新用户专享10元优惠券',
//           couponType: 1,
//           discount: 10,
//           minAmount: 50,
//           startDate: '2023-12-01 00:00:00',
//           endDate: '2023-12-31 23:59:59',
//           totalQuantity: 1000,
//           usedQuantity: 234,
//           status: 1,
//           description: '新用户专享，满50元使用',
//           createTime: '2023-11-20 10:00:00'
//         },
//         {
//           couponId: 'COUP002',
//           couponName: '满100减20优惠券',
//           couponType: 1,
//           discount: 20,
//           minAmount: 100,
//           startDate: '2023-12-10 00:00:00',
//           endDate: '2023-12-25 23:59:59',
//           totalQuantity: 500,
//           usedQuantity: 89,
//           status: 1,
//           description: '全店通用，满100元使用',
//           createTime: '2023-12-05 15:30:00'
//         },
//         {
//           couponId: 'COUP003',
//           couponName: '生日专属8折券',
//           couponType: 3,
//           discount: 20,
//           minAmount: 0,
//           startDate: '2023-12-01 00:00:00',
//           endDate: '2024-11-30 23:59:59',
//           totalQuantity: 200,
//           usedQuantity: 45,
//           status: 1,
//           description: '生日当天专享8折优惠',
//           createTime: '2023-11-15 09:00:00'
//         }
//       ]
// 
//       resolve({
//         status: 200,
//         msg: 'ok',
//         data: mockData
//       })
//     }, 500)
//   })
// }
// 
// /**
//  * Mock behavior stats
//  */
// export function mockGetBehaviorStats(params) {
//   return new Promise((resolve) => {
//     setTimeout(() => {
//       const mockData = [
//         { behaviorType: 1, typeName: '浏览', count: 15420, percentage: 45.8 },
//         { behaviorType: 2, typeName: '加购', count: 8934, percentage: 26.6 },
//         { behaviorType: 3, typeName: '购买', count: 5621, percentage: 16.7 },
//         { behaviorType: 4, typeName: '收藏', count: 3592, percentage: 10.7 }
//       ]
// 
//       resolve({
//         status: 200,
//         msg: 'ok',
//         data: mockData
//       })
//     }, 400)
//   })
// }
// 
// /**
//  * Mock category stats
//  */
// export function mockGetCategoryStats(params) {
//   return new Promise((resolve) => {
//     setTimeout(() => {
//       const mockData = [
//         { category: '水果', totalAmount: 42800, orderCount: 1320 },
//         { category: '乳制品', totalAmount: 38650, orderCount: 980 },
//         { category: '粮油', totalAmount: 21400, orderCount: 640 },
//         { category: '零食', totalAmount: 17620, orderCount: 520 },
//         { category: '饮料', totalAmount: 16280, orderCount: 485 }
//       ]
// 
//       resolve({
//         status: 200,
//         msg: 'ok',
//         data: mockData
//       })
//     }, 420)
//   })
// }
// 
// /**
//  * Mock hot products
//  */
// export function mockGetHotProducts(params) {
//   return new Promise((resolve) => {
//     setTimeout(() => {
//       const mockData = [
//         { rank: 1, productId: 'P001', productName: '新鲜苹果', sales: 1250, revenue: 10625.00 },
//         { rank: 2, productId: 'P002', productName: '纯牛奶', sales: 980, revenue: 63700.00 },
//         { rank: 3, productId: 'P003', productName: '全麦面包', sales: 876, revenue: 9198.00 },
//         { rank: 4, productId: 'P004', productName: '新鲜香蕉', sales: 754, revenue: 4524.00 },
//         { rank: 5, productId: 'P005', productName: '酸奶', sales: 698, revenue: 13960.00 }
//       ]
// 
//       resolve({
//         status: 200,
//         msg: 'ok',
//         data: mockData
//       })
//     }, 550)
//   })
// }
// 
// /**
//  * Mock member consumption
//  */
// export function mockGetMemberConsumption(params) {
//   return new Promise((resolve) => {
//     setTimeout(() => {
//       const mockData = [
//         { rank: 1, memberId: 'M001', memberName: '张三', consumption: 3580.50, orderCount: 28 },
//         { rank: 2, memberId: 'M005', memberName: '赵六', consumption: 2890.00, orderCount: 22 },
//         { rank: 3, memberId: 'M003', memberName: '王五', consumption: 2456.80, orderCount: 19 },
//         { rank: 4, memberId: 'M007', memberName: '周八', consumption: 1987.30, orderCount: 16 },
//         { rank: 5, memberId: 'M002', memberName: '李四', consumption: 1654.20, orderCount: 13 }
//       ]
// 
//       resolve({
//         status: 200,
//         msg: 'ok',
//         data: mockData
//       })
//     }, 450)
//   })
// }