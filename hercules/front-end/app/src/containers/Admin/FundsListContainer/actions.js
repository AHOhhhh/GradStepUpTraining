import httpClient from '../../../utils/http'
import * as types from '../share/constants'

export const getFundsOrders = (params) => ({
  type: types.ADMIN_GET_FUNDS_INFO,
  promise: httpClient.get('/order-payment/transactions', {params})
})
