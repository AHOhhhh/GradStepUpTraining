import httpClient from '../../../../utils/http/index'
import * as types from './constant'

export const getOrderOffers = (orderId) => ({
  type: types.WISE_PORT_ORDER_GET_OFFERS,
  promise: httpClient.get(`/mwp/orders/${orderId}/offers`)
})

export const updateOrderOffers = (orderId, data) => ({
  type: types.WISE_PORT_ORDER_UPDATE_OFFERS,
  promise: httpClient.post(`/mwp/orders/${orderId}/selectedOffers`, {...data})
})

export const getOrder = (orderId) => ({
  type: types.WISE_PORT_GET_ORDER_DETAIL,
  promise: httpClient.get(`/mwp/orders/${orderId}`)
})