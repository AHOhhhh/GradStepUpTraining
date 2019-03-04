import httpClient from 'utils/http'
import * as types from '../../constants'

export const getLatestTransaction = orderId => ({
  type: types.OPERATOR_ADMIN_AUDIT_PAYMENT,
  promise: httpClient.get(`/orders/${orderId}/transactions/offline/latest-transaction`)
})

export const getOrderLatestStatus = orderId => {
  return () => {
    return httpClient.get(`/orders/${orderId}`)
  }
}

export const submitAuditPaymentResult = parmas => {
  return () => {
    return httpClient.post('/payment-transactions/payment-status/offline', parmas)
  }
}
