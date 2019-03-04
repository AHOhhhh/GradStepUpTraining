import * as types from './constants'

export const initialState = {
  
}

const orderReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.SCF_GET_CURRENT_ORDER_SUCCESS:
        return action.req.data
      default:
        return state
    }
  }

export default orderReducer
