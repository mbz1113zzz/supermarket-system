import request from '@/utils/request'

/**
 * product list
 */
export function getProductList(params) {
  return request({
    url: '/products',
    method: 'get',
    params
  })
}

/**
 * all products
 */
export function getAllProducts() {
  return request({
    url: '/products/all',
    method: 'get'
  })
}

/**
 * get product by id
 */
export function getProduct(productId) {
  return request({
    url: `/products/${productId}`,
    method: 'get'
  })
}

/**
 * add product
 */
export function addProduct(data) {
  return request({
    url: '/products',
    method: 'post',
    data
  })
}

/**
 * update product
 */
export function updateProduct(data) {
  return request({
    url: '/products',
    method: 'put',
    data
  })
}

/**
 * delete product
 */
export function deleteProduct(productId) {
  return request({
    url: `/products/${productId}`,
    method: 'delete'
  })
}

/**
 * update stock
 */
export function updateStock(productId, stock) {
  return request({
    url: `/products/${productId}/stock`,
    method: 'patch',
    params: { stock }
  })
}

/**
 * check stock
 */
export function checkStock(productId, quantity) {
  return request({
    url: `/products/${productId}/stock/check`,
    method: 'get',
    params: { quantity }
  })
}

/**
 * product categories
 */
export function getCategories() {
  return request({
    url: '/products/categories',
    method: 'get'
  })
}

/**
 * search products
 */
export function searchProducts(name) {
  return request({
    url: '/products/search',
    method: 'get',
    params: { name }
  })
}
