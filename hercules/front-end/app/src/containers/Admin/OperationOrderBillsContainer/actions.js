import httpClient from '../../../utils/http'
import * as types from '../share/constants'

export const getOrderBills = (params) => ({
  type: types.OPERATOR_ADMIN_GET_ORDER_BILLS,
  promise: httpClient.get('/order-bill', {params})
})

export const getOrderBill = orderId => ({
  type: types.OPERATOR_ADMIN_GET_ORDER_BILL,
  promise: httpClient.get(`/order-bill/${orderId}`)
})

export const importExcel = file => {
  const originFile = file.file
  const formData = new FormData()
  formData.append(file.filename, originFile, originFile.name)
  return {
    type: types.OPERATOR_ADMIN_ORDER_BILL_IMPORT,
    promise: httpClient.post('/order-bill/excel', formData)
  }
}
