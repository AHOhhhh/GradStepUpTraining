import httpClient from '../../../utils/http'
import * as types from '../share/constants'

export const fetchAirCargoOrder = orderId => httpClient.get('/acg/orders/' + orderId)

export const getAirCargoOrder = orderId => ({
  type: types.GET_ACG_ORDER,
  promise: fetchAirCargoOrder(orderId)
})
