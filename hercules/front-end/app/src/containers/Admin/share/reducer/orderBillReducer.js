/* eslint-disable default-case */
import * as types from '../constants'

const getOrderBill = (state = {}, action) => {
  switch (action.type) {
    case types.OPERATOR_ADMIN_GET_ORDER_BILL_REQUEST:
      return {
        ...action.status
      }
    case types.OPERATOR_ADMIN_GET_ORDER_BILL_SUCCESS:
      return {
        ...action.status,
        content: [action.req.data]
      }
    case types.OPERATOR_ADMIN_GET_ORDER_BILL_FAILURE:
      return {
        ...action.status
      }
  }
}

const getOrderBills = (state = {}, action) => {
  switch (action.type) {
    case types.OPERATOR_ADMIN_GET_ORDER_BILLS_REQUEST:
      return {
        ...action.status
      }
    case types.OPERATOR_ADMIN_GET_ORDER_BILLS_SUCCESS:
      return {
        ...action.status,
        ...action.req.data
      }
    case types.OPERATOR_ADMIN_GET_ORDER_BILLS_FAILURE:
      return {
        ...action.status
      }
  }
}

export default (state = {}, action) => {
  return getOrderBills(state, action) || getOrderBill(state, action) || state
}

