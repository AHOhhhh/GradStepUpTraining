import httpClient from '../../../utils/http/index'
import * as types from '../share/constants'

export const fetchWisePortOrder = orderId => httpClient.get(`/mwp/orders/${orderId}`)

export const getOrderDetail = (orderId) => ({
  type: types.WISE_PORT_GET_ORDER_DETAIL,
  promise: fetchWisePortOrder(orderId)
})