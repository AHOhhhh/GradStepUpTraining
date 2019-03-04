import httpClient from '../../../utils/http'
import * as types from '../share/constants'

export function getOrder(orderId) {
  return function (dispatch) {
    dispatch({
      type: types.WMS_GET_ORDER,
      promise: httpClient.get('/wms/orders/' + orderId)
    })
  }
}
