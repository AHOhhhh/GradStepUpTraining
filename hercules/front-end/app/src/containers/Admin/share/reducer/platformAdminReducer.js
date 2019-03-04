import {combineReducers} from 'redux'
import * as types from '../constants/platformAdmin'
import validEnterprises from '../../OrderList/reducer'
import adminOperationHistory from '../../EnterpriseListContainer/Authorized/ViewEnterpriseInfoContainer/AdminOperationHistory/reducer'
import enterprisePayMethods from '../../EnterpriseListContainer/Authorized/ViewEnterpriseInfoContainer/EnterprisePayMethods/reducer'

const init = []

const enterpriseHistories = (state = init, action) => {
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
  adminOperationHistory,
  enterpriseHistories,
  validEnterprises,
  enterprisePayMethods
})