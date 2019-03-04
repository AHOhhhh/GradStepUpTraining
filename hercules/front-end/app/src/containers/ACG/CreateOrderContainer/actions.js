import {browserHistory} from 'react-router'
import _ from 'lodash'
import * as action from 'actions'
import httpClient from '../../../utils/http';
import * as types from '../share/constants';
import {fetchWisePortOrder} from '../../MWP/WisePortOrderContainer/actions';
import {fetchAirCargoOrder} from '../OrderManagementContainer/actions';

export function getAirports() {
  return function (dispatch) {
    dispatch({
      type: types.GET_AIRPORTS_INFO,
      promise: httpClient.get('/acg-api/airports')
    })
  }
}

export function createAcgOrder(order) {
  return (dispatch) => {
    dispatch({
      type: types.CREATE_AIRCARGO_ORDER,
      promise: httpClient.post('/acg/orders', order)
        .then((order) => {
          browserHistory.push(`/acg/orders/${order.data.id}`)
        })
    })
  }
}

export function getAcgOrderPrice(params) {
  return (dispatch) => {
    return httpClient.post('/acg-api/order-price', params).then(res => {
      dispatch({
        type: types.GET_AIRCARGO_ORDER_PRICE_SUCCESS,
        req: {
          data: {content: [res.data]}
        }
      })
    }).catch(err => {
      dispatch({
        type: types.GET_AIRCARGO_ORDER_PRICE_SUCCESS,
        req: {data: {}}
      })
      return Promise.reject(err)
    })
  }
}

export const clearAcgOrderCreatedInfo = () => {
  return dispatch => dispatch({
    type: types.CLEAR_ACG_ORDER_CREATED_INFO
  })
}


export const getRelatedOrder = ({acgOrderId = null, mwpOrderId = null}) => dispatch => {
  const orderId = acgOrderId || mwpOrderId
  if (orderId) {
    const fetchMethod = mwpOrderId ? fetchWisePortOrder : fetchAirCargoOrder
    return fetchMethod(orderId).then(req => {
      const contactId = _.get(req, 'data.contact.id', null) || _.get(req, 'data.contactId', null)
      const currency = _.get(req, 'data.currency', '')
      const goods = _.map(_.get(req, 'data.goods', []))
      dispatch(action.addProducts(goods, currency))
      if (contactId) {
        dispatch(action.selectContact(contactId))
      }
    })
  }
}
