import { push } from 'react-router-redux'
import { browserHistory } from 'react-router'
import httpClient from '../../../utils/http/index'
import * as types from './constants'

export const getPaymentInfo = (orderType) => ({
  type: types.GET_PAYMENT_METHOD,
  promise: httpClient.get(`/orders/${orderType.toUpperCase()}/payment-methods`)
})

export const handleSpin = (isSpin) => ({
  type: types.HANDLE_SPIN,
  isSpin
})

export const handleOrderPayment = (orderForm, orderInfo) => {
  return (dispatch) => {
    return httpClient.post('/order-payment/online', orderInfo, {
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json'
      }
    })
      .then((res) => {
        return dispatch({
          type: types.SET_SUBMIT_INFO,
          data: res.data
        })
      })
      .then(() => {
        orderForm.submit()
      })
  }
}

const getOrderStatus = (orderType, orderId, dispatch) => {
  return new Promise((resolve, reject) => httpClient.get(`/${orderType}/orders/${orderId}`)
    .then((res) => {
      if (res.data.status !== types.ORDER_STATUS.WAIT_FOR_PAY) {
        dispatch({
          type: types.HANDLE_SPIN,
          isSpin: false
        })
        browserHistory.push(`/${orderType}/orders/${orderId}`)
        reject({ returnOrder: true })
      } else {
        resolve()
      }
    }))
}

const handleOfflinePaymentFailure = (dispatch) => {
  dispatch({
    type: types.HANDLE_SPIN,
    isSpin: false
  })
}

export const handleOfflinePayment = (offlinePaymentInfo, orderType, orderId) => {
  return (dispatch) => {
    return httpClient.post('/order-payment/offline', offlinePaymentInfo)
      .then(() => {
        return getOrderStatus(orderType, orderId, dispatch)
      })
      .then(() => {
        return getOrderStatus(orderType, orderId, dispatch)
      })
      .then(() => {
        return getOrderStatus(orderType, orderId, dispatch)
      })
      .catch((error) => {
        if (!error.returnOrder) {
          handleOfflinePaymentFailure(dispatch)
        }
      })
  }
}

export const getOrderPrice = (orderId) => {
  return (dispatch) => {
    return httpClient.get(`/orders/${orderId}/payment-requests`)
      .then((res) => {
        let amount = 0
        let requestIds = []  // eslint-disable-line
        res.data.forEach((payRequest) => {
          amount += payRequest.amount
          requestIds.push(payRequest.id)
        })

        dispatch({
          type: types.SET_ORDER_PRICE,
          data: Object.assign({}, { amount }, { orderId }, { requestIds })
        })
      })
  }
}

export const handlePaymentStatus = (orderId, paymentId, orderType) => {
  return (dispatch) => {
    return httpClient.post('/payment-transactions/payment-status/online', {
      paymentId,
      sync: false
    })
      .then(() => httpClient.get(`/${orderType}/orders/${orderId}`))
      .then((res) => {
        const status = res.data.status
        if (types.PAYMENT_STATUS.SUCCESS.some((s) => s === status)) {
          dispatch(push(`/${orderType}/orders/${orderId}/payment_success`))
          return types.PAYMENT_STATUS.SUCCESS
        }

        if (status === types.PAYMENT_STATUS.FAILURE) {
          dispatch(push(`/${orderType}/orders/${orderId}/payment_failure`))
          return types.PAYMENT_STATUS.FAILURE
        }

        return types.PAYMENT_STATUS.WAITING
      })
  }
}

export const getOfflinePaymentAuditOpinion = (orderId) => dispatch => {
  return httpClient.get(`/orders/${orderId}/transactions/latest-transaction`)
    .then((res) => {
      let offlinePaymentAuditOpinion = {}
      if (res.data !== '') {
        offlinePaymentAuditOpinion = res.data
      }
      dispatch({
        type: types.SET_OFFLINE_PAYMENT_AUDIT_OPINION,
        offlinePaymentAuditOpinion
      })
    })
}