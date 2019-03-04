import * as types from '../constants'

export const initialState = {
  submitting: false,
  loading: false,
  order: {},
  fund: {},
}

const auditPayment = (state = initialState, action) => {
  switch (action.type) {
    case types.OPERATOR_ADMIN_AUDIT_PAYMENT_REQUEST:
      return {
        ...state,
        fund: {},
        loading: true
      }
    case types.OPERATOR_ADMIN_AUDIT_PAYMENT_SUCCESS:
      return {
        ...state,
        fund: action.req.data,
        loading: false
      }
    case types.OPERATOR_ADMIN_AUDIT_PAYMENT_FAILURE:
      return {
        ...state,
        fund: {},
        loading: false
      }
    default:
      return state
  }
}

export default auditPayment
