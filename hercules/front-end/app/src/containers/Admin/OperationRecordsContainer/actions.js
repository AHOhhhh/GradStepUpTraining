import httpClient from '../../../utils/http'
import * as types from '../share/constants'

export const getPlatformOperations = (params) => ({
  type: types.ADMIN_GET_PLATFORM_OPERATIONS,
  promise: httpClient.get('/user-operations', {params})
})

export const getOrderOperations = (params) => ({
  type: types.ADMIN_GET_ORDER_OPERATIONS,
  promise: httpClient.get('/order-operations', {params})
})
