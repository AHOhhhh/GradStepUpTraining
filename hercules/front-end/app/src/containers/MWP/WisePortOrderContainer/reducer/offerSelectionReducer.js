import * as types from '../OfferSelectionStep/constant'

const initialState = {
  offers: []
}

const offerSelectionReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.WISE_PORT_ORDER_GET_OFFERS_SUCCESS:
        return action.req.data
      default:
        return state
    }
  }

export default offerSelectionReducer
