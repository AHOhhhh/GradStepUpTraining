import * as types from '../../share/components/WisePortTimeLine/constants'

const initialState = []

const changeRecordsReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.GET_ORDER_CHANGE_RECORDS:
        return action.orderChangeRecords
      default:
        return state
    }
  }

export default changeRecordsReducer