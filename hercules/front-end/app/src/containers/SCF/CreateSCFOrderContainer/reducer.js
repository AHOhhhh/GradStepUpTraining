import * as types from './constants'

export const initialState = '0'

const orderQualificationReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.GET_ENTERPRISE_SCF_QUALIFICATION_SUCCESS:
        return action.res.data
      default:
        return state
    }
  }

export default orderQualificationReducer
