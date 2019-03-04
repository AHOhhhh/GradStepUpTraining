import * as types from './constants'

export const initialState = {
  portOptions: [],
  supervisionOptions: [],
  transportOptions: []
}

const wisePortCreateOrderReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.GET_PORT_OPTIONS_SUCCESS:
        return {
          ...state,
          portOptions: action.req.data
        }
      case types.GET_SUPERVISION_OPTIONS_SUCCESS:
        return {
          ...state,
          supervisionOptions: action.req.data
        }
      case types.GET_TRANSPORT_OPTIONS_SUCCESS:
        return {
          ...state,
          transportOptions: action.req.data
        }
      default:
        return state
    }
  }

export default wisePortCreateOrderReducer
