import {httpClient} from 'utils'

export function handleRefund(orderId, values) {
  return httpClient.post(`/orders/${orderId}/refund`, values)
}
