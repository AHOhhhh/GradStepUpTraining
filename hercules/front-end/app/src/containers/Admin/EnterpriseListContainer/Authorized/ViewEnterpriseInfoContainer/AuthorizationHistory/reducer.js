import {combineReducers} from 'redux'
import * as types from './constants'

const init = []

const enterpriseHistoryReducer = (state = init, action) => {
  switch (action.type) {
    case types.ADMIN_GET_ENTERPRISE_HISTORIES_SUCCESS:
      return action.req.data
    case types.ADMIN_GET_ADMIN_SUCCESS:
      const enterpriseHistories = state

      const histories = enterpriseHistories.map((history) => {
        if (history.updatedBy === action.req.data.id) {
          history.updatedBy = action.req.data.username
        }

        return history
      })

      return [...histories]
    default:
      return state
  }
}

export default combineReducers({
  enterpriseHistories: enterpriseHistoryReducer
})