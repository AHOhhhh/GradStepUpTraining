import httpClient from 'utils/http'
import * as types from './constants'

export const getSCFOrderById = (orderId) => ({
  type: types.SCF_GET_CURRENT_ORDER,
  promise: httpClient.get(`/scf/orders/${orderId}`)
})

export const goNextForAcceptedOrder = orderId => dispatch => {
  httpClient.get(`/scf/orders/${orderId}/next`).then(() => {
    dispatch(getSCFOrderById(orderId))
  })
}
