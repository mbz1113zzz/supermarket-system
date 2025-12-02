import request from '@/utils/request'

// supplier management

/**
 * supplier list
 */
export function getSupplierList(params) {
  return request({
    url: '/suppliers',
    method: 'get',
    params
  })
}

/**
 * get supplier by id
 */
export function getSupplier(supplierId) {
  return request({
    url: `/suppliers/${supplierId}`,
    method: 'get'
  })
}

/**
 * add supplier
 */
export function addSupplier(data) {
  return request({
    url: '/suppliers',
    method: 'post',
    data
  })
}

/**
 * update supplier
 */
export function updateSupplier(data) {
  return request({
    url: `/suppliers/${data.supplierId}`,
    method: 'put',
    data
  })
}

/**
 * delete supplier
 */
export function deleteSupplier(supplierId) {
  return request({
    url: `/suppliers/${supplierId}`,
    method: 'delete'
  })
}

/**
 * update supplier status
 */
export function updateSupplierStatus(supplierId, status) {
  return request({
    url: `/suppliers/${supplierId}/status`,
    method: 'patch',
    data: { status }
  })
}

// purchase order management

/**
 * purchase order list
 */
export function getPurchaseOrderList(params) {
  return request({
    url: '/purchase-orders',
    method: 'get',
    params
  })
}

/**
 * get purchase order by id
 */
export function getPurchaseOrder(orderId) {
  return request({
    url: `/purchase-orders/${orderId}`,
    method: 'get'
  })
}

/**
 * add purchase order
 */
export function addPurchaseOrder(data) {
  return request({
    url: '/purchase-orders',
    method: 'post',
    data
  })
}

/**
 * update purchase order
 */
export function updatePurchaseOrder(data) {
  return request({
    url: `/purchase-orders/${data.orderId}`,
    method: 'put',
    data
  })
}

/**
 * delete purchase order
 */
export function deletePurchaseOrder(orderId) {
  return request({
    url: `/purchase-orders/${orderId}`,
    method: 'delete'
  })
}

/**
 * update purchase order status
 */
export function updatePurchaseOrderStatus(orderId, status) {
  return request({
    url: `/purchase-orders/${orderId}/status`,
    method: 'patch',
    data: { status }
  })
}

// // mock data
// 
// /**
//  * mock supplier list
//  */
// export function mockGetSupplierList(params) {
//   return new Promise((resolve) => {
//     setTimeout(() => {
//       const mockData = [
//         {
//           supplierId: 'SUP001',
//           supplierName: '北京农产品批发公司',
//           contactPerson: '张经理',
//           contactPhone: '13800138001',
//           contactEmail: 'zhang@beijingproduce.com',
//           address: '北京市朝阳区农产品批发市场A区12号',
//           status: 1,
//           creditRating: 'A',
//           createTime: '2023-01-15 10:30:00'
//         },
//         {
//           supplierId: 'SUP002',
//           supplierName: '上海食品配送中心',
//           contactPerson: '李经理',
//           contactPhone: '13900139002',
//           contactEmail: 'li@shanghaifood.com',
//           address: '上海市浦东新区食品物流园B座88号',
//           status: 1,
//           creditRating: 'A+',
//           createTime: '2023-02-20 14:15:00'
//         },
//         {
//           supplierId: 'SUP003',
//           supplierName: '广州水果批发市场',
//           contactPerson: '王老板',
//           contactPhone: '13700137003',
//           contactEmail: 'wang@gzfruit.com',
//           address: '广州市白云区水果批发市场C区5号',
//           status: 1,
//           creditRating: 'B+',
//           createTime: '2023-03-10 09:45:00'
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
//  * mock purchase order list
//  */
// export function mockGetPurchaseOrderList(params) {
//   return new Promise((resolve) => {
//     setTimeout(() => {
//       const mockData = [
//         {
//           orderId: 'PO20231201001',
//           orderNumber: 'PO-20231201-001',
//           supplierId: 'SUP001',
//           supplierName: '北京农产品批发公司',
//           orderDate: '2023-12-01 09:30:00',
//           expectedDate: '2023-12-03',
//           totalAmount: 15800.00,
//           status: 2,
//           operator: '采购员001',
//           items: [
//             {
//               productId: 'P001',
//               productName: '新鲜苹果',
//               quantity: 100,
//               unit: 'kg',
//               unitPrice: 8.50,
//               subtotal: 850.00
//             },
//             {
//               productId: 'P002',
//               productName: '新鲜香蕉',
//               quantity: 150,
//               unit: 'kg',
//               unitPrice: 6.00,
//               subtotal: 900.00
//             }
//           ]
//         },
//         {
//           orderId: 'PO20231201002',
//           orderNumber: 'PO-20231201-002',
//           supplierId: 'SUP002',
//           supplierName: '上海食品配送中心',
//           orderDate: '2023-12-01 14:20:00',
//           expectedDate: '2023-12-05',
//           totalAmount: 23500.00,
//           status: 1,
//           operator: '采购员002',
//           items: [
//             {
//               productId: 'P003',
//               productName: '牛奶',
//               quantity: 200,
//               unit: '箱',
//               unitPrice: 65.00,
//               subtotal: 13000.00
//             },
//             {
//               productId: 'P004',
//               productName: '面包',
//               quantity: 100,
//               unit: '袋',
//               unitPrice: 105.00,
//               subtotal: 10500.00
//             }
//           ]
//         }
//       ]
// 
//       resolve({
//         status: 200,
//         msg: 'ok',
//         data: mockData
//       })
//     }, 800)
//   })
// }