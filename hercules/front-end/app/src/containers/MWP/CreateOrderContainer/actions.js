import {browserHistory} from 'react-router'
import httpClient from 'utils/http'
import * as types from './constants'

export const getPortOptions = () => ({
  type: types.GET_PORT_OPTIONS,
  promise: httpClient.get('/dictionary?code=ports')
})

export const getSupervisionOptions = () => ({
  type: types.GET_SUPERVISION_OPTIONS,
  promise: httpClient.get('/dictionary?code=supervisionType')
})

export const getTransportOptions = () => ({
  type: types.GET_TRANSPORT_OPTIONS,
  promise: httpClient.get('/dictionary?code=transportType')
})

export const clearProductList = () => ({
  type: types.CLEAR_PRODUCT_LIST
})

export const createWisePortOrder = (order) =>
  dispatch => {  // eslint-disable-line
    httpClient.post('/mwp/orders', order)
      .then(res => {
        browserHistory.push('/mwp/orders/' + res.data.id)
      })
  }
