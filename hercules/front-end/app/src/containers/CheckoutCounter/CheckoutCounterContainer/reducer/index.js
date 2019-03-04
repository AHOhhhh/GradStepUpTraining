import * as types from '../constants/index'

const initialState = {
  offlinePaymentAuditOpinion: {},
  submitInfo: {redirectBody: {}},
  orderPrice: {},
  isSpin: false,
  availablePaymentMethod: [],
  payeeInfo: {}
}

const paymentReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.SET_SUBMIT_INFO:
        return {
          ...state,
          submitInfo: action.data
        }
      case types.SET_ORDER_PRICE:
        return {
          ...state,
          orderPrice: action.data
        }
      case types.GET_PAYMENT_METHOD_SUCCESS:
        return {
          ...state,
          availablePaymentMethod: action.req.data.map(payment => payment.payMethod),
          paymentInfo: action.req.data
        }
      case types.SET_OFFLINE_PAYMENT_AUDIT_OPINION:
        return {
          ...state,
          offlinePaymentAuditOpinion: action.offlinePaymentAuditOpinion
        }
      case types.HANDLE_SPIN:
        return {
          ...state,
          isSpin: action.isSpin
        }
      default:
        return state
    }
  }

export default paymentReducer
