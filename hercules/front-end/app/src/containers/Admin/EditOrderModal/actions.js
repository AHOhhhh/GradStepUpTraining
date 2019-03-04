import httpClient from '../../../utils/http/index'

export function platformAdminUpdateWMSOrder(orderId, params) {
  return dispatch => { // eslint-disable-line
    return httpClient.post(`/wms/orders/${orderId}`, params)
  }
}

