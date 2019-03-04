import * as types from './constants'

const initialState = {
  products: []
}

const inServiceReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.GET_IN_SERVICE_PRODUCTS_SUCCESS:
        return {
          ...state,
          products: action.req.data
        }
      default:
        return state
    }
  }

export default inServiceReducer
