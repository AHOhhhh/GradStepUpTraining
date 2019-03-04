import httpClient from '../../../utils/http'
import * as types from '../share/constants'

export function getWMSOrderTemplate() {
  return function (dispatch) {
    dispatch({
      type: types.WMS_GET_ORDER_TEMPLATE,
      promise: httpClient.get('/resources/wms/order-description.json')
    })
  }
}

export function createWMSOrder(params) {
  return {
    type: types.WMS_CREATE_ORDER,
    promise: httpClient.post('/wms/orders', params)
  }
}

export function getWMSOrderByEnterprise(params) {
  return function (dispatch) {
    dispatch({
      type: types.WMS_GET_ORDERS_BY_USER,
      orderType: params.orderType,
      promise: httpClient.get('/wms/orders?enterpriseId=' + params.enterpriseId)
    })
  }
}

export function getLandingPageWMSOrderByEnterprise(enterpriseId) {
  return function (dispatch) {
    dispatch({
      type: types.WMS_GET_LANDING_PAGE_ORDER,
      promise: httpClient.get('/wms/orders?enterpriseId=' + enterpriseId)
    })
  }
}

export function clearWMSOrder() {
  return function (dispatch) {
    dispatch({
      type: types.WMS_CLEAR_ORDER,
    })
  }
}
