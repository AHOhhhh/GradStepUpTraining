/* eslint-disable no-debugger */
import * as types from '../../share/constants'

const initialState = {
  orderChangeRecords: []
}

const orderReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.WISE_PORT_GET_ORDER_DETAIL_SUCCESS:
        return action.req.data
      case types.GET_ORDER_RECORDS_CHANGE_SUCCESS:
        return {
          ...state,
          orderChangeRecords: action.req.data.orderChangeRecords
        }
      case types.WISE_PORT_ORDER_CANCEL_SUCCESS:
        return action.req.data
      default:
        return state
    }
  }

export default orderReducer