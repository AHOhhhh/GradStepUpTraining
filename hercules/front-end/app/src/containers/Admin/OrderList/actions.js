import httpClient from '../../../utils/http'
import * as types from '../share/constants'
import { CLOSED_STATUS } from '../../User/OrderListContainer/constants';

export const refreshStatus = (record, params) => {
  return (dispatch) => httpClient.get(`/${record.orderType}/orders/${record.id}/paymentId`)
    .then((res) => {
      const { id, payMethod } = res.data
      if (id && payMethod === 'ONLINE') {
        return httpClient.post('/payment-transactions/payment-status/online', {
          paymentId: id,
          sync: true
        })
      }
      return false
    })
    .then((result) => {
      if (result) {
        dispatch({
          type: types.GET_ORDERS,
          promise: httpClient.get('/orders', { params })
        })
      }
    })
}

export const getValidEnterprise = (enterpriseName) => ({
  type: types.ADMIN_GET_VALID_ENTERPRISES,
  promise: httpClient.get(`/enterprises?name=${enterpriseName}&validationStatus=Authorized`)
})

export const getCompletedSCFOrders = (params) => {
  const filteredParams = Object.assign({}, params, {
    type: 'scf',
    status: CLOSED_STATUS
  })
  return {
    type: types.GET_SCF_ORDERS,
    promise: httpClient.get('/orders', { params: filteredParams })
  }
}

export const getOrders = (params) => ({
  type: types.GET_ORDERS,
  promise: httpClient.get('/orders', { params })
})

export const adminGetOrderById = (orderId) => {
  return function (dispatch) {
    return httpClient.get('/orders/' + orderId).then(res => {
      dispatch({
        type: types.GET_ORDERS_SUCCESS,
        req: {
          data: { content: [res.data] }
        }
      })
    }).catch(err => {
      dispatch({
        type: types.GET_ORDERS_SUCCESS,
        req: { data: {} }
      })
      return Promise.reject(err)
    })
  }
}

export function updateOrderToCancelled(order, params) {
  return dispatch => { // eslint-disable-line
    return httpClient.post(`/${order.orderType}/orders/${order.id}/cancellation`, params)
  }
}