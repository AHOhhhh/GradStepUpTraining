import {push} from 'react-router-redux'
import httpClient from '../../../../utils/http/index'
import * as constants from '../../share/constants'

export const getOrderPriceDetail = (orderId) => ({
  type: constants.GET_ORDER_PRICE_DETAIL,
  promise: httpClient.get(`/mwp/orders/${orderId}/suborders/price-detail`)
})

export const setPaymentStatus = (status) => ({
  type: constants.SET_PAYMENT_STATUS,
  status
})

export const payForOrder = (orderId, data) => {
  return (dispatch) => {
    return httpClient.post(`/mwp/orders/${orderId}/suborders/confirming-price`, data)
      .then((res) => {
        const isConfirmed = res.data.isConfirmed
        if (!isConfirmed) {
          dispatch({
            type: constants.GET_ORDER_PRICE_DETAIL,
            promise: httpClient.get(`/mwp/orders/${orderId}/suborders/price-detail`)
          })
          dispatch(setPaymentStatus(constants.PAYMENT_STATUS.PRICE_CHANGE))
        } else {
          dispatch(push(`/mwp/orders/${orderId}/checkout_counter`))
        }
      })
  }
}

