import * as types from '../constants'
import {ORDER_TYPE} from '../constants/order-config'

export const initialState = {
  order: {},
  error: null,
  success: null,
  failed: null,
  orderTemplate: {},
  contacts: [],
  orderId: null,
  contactId: null,
  selectedContactId: null
}

const WMSReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.WMS_GET_ORDER_REQUEST:
        return Object.assign({}, state, {
          ...state,
          success: null,
          failed: null,
        })
      case types.WMS_GET_ORDER_SUCCESS:
        return Object.assign({}, state, {
          ...state,
          order: action.req.data,
          success: true,
          failed: false
        })
      case types.WMS_GET_ORDER_FAILURE:
        return Object.assign({}, state, {
          ...state,
          success: false,
          failed: true,
          error: action.error.data
        })
      case types.WMS_GET_ORDER_TEMPLATE:
        return Object.assign({}, state, {
          orderTemplate: {}
        })
      case types.WMS_GET_ORDER_TEMPLATE_SUCCESS:
        return Object.assign({}, state, {
          orderTemplate: action.req.data
        })
      case types.WMS_GET_ORDER_TEMPLATE_FAILURE:
        return Object.assign({}, state, {
          orderTemplate: {}
        })
      case types.WMS_CREATE_ORDER:
        return Object.assign({}, state, {
          orderId: null
        })
      case types.WMS_CREATE_ORDER_SUCCESS:
        return Object.assign({}, state, {
          orderId: action.req.data.id,
        })
      case types.WMS_CREATE_ORDER_FAILURE:
        return Object.assign({}, state, {
          orderId: null,
        })
      case types.WMS_GET_ORDERS_BY_USER_SUCCESS: {
        const orderList = action.req.data.content.filter(item => {
          if (action.orderType === ORDER_TYPE.open) {
            return item.type === action.orderType && item.status !== 'Cancelled'
          }
          return item.type === action.orderType && item.status !== 'Closed' && item.status !== 'Cancelled'
        })

        return Object.assign({}, state, {
          orderId: orderList.length ? orderList[0].id : null
        })
      }
      case types.WMS_GET_LANDING_PAGE_ORDER_SUCCESS:
        const orders = action.req.data.content
        if (orders.length === 1 && orders[0].status === 'Closed' && orders[0].type === ORDER_TYPE.open && !orders[0].refundStatus) {
          return Object.assign({}, state, {
            orderId: orders[0].id
          })
        }
        const orderList = action.req.data.content.filter(item => {
          return (item.type === ORDER_TYPE.open || item.type === ORDER_TYPE.renew) && !item.refundStatus && item.status === 'Closed'
        })
        return Object.assign({}, state, {
          orderId: orderList.length ? orderList[0].id : null
        })
      case types.WMS_CLEAR_ORDER:
        return Object.assign({}, state, {
          orderId: null,
        })
      default:
        return state
    }
  }

export default WMSReducer
