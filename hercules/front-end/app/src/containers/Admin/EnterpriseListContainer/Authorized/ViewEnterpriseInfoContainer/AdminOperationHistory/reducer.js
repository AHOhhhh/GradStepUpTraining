import * as types from './constants'

const init = {}

const enterpriseHistoryReducer = (state = init, action) => {
  switch (action.type) {
    case types.ADMIN_GET_ADMIN_OPERATION_HISTORIES_SUCCESS:
      return action.req.data
    default:
      return state
  }
}

export default enterpriseHistoryReducer