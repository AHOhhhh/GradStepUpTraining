import httpClient from '../../../../utils/http/index'
import * as constants from './constants'

export const getInServiceProductOffers = (id) => ({
  type: constants.GET_IN_SERVICE_PRODUCTS,
  promise: httpClient.get(`/mwp/orders/${id}/suborders`)
})

export * from '../../../User/LoginContainer/actions'