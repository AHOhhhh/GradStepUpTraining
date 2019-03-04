import httpClient from 'utils/http'
import * as types from './constants'

export function getProductNameOptions() {
  return {
    type: types.GET_PRODUCT_NAME_OPTIONS,
    promise: httpClient.get('/dictionary?code=productName')
  }
}

export function getProductPriceUnitOptions() {
  return {
    type: types.GET_PRODUCT_PRICE_UNIT_OPTIONS,
    promise: httpClient.get('/dictionary?code=priceUnit')
  }
}

export function addProducts(productList, currency) {
  return {
    type: types.ADD_PRODUCTS,
    productList,
    currency
  }
}

export function addProduct(product) {
  return {
    type: types.ADD_PRODUCT,
    product
  }
}

export function editProduct(product, index) {
  return {
    type: types.EDIT_PRODUCT,
    product,
    index
  }
}

export function deleteProduct(index) {
  return {
    type: types.DELETE_PRODUCT,
    index
  }
}

