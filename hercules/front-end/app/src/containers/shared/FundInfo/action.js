import httpClient from '../../../utils/http'

export function getSuccessFundInfo(orderId) {
  return httpClient.get(`/orders/${orderId}/transactions`)
}

