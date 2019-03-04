import { get } from 'lodash'
import * as types from '../share/constants'

const initialState = {
  list: [],
  page: 0,
  total: 0,
  loading: false,
}

const fundsInfo = (state = initialState, action) => {
  switch (action.type) {
    case types.ADMIN_GET_FUNDS_INFO_REQUEST:
      return {
        list: [],
        total: 0,
        page: 0,
        loading: true,
      }
    case types.ADMIN_GET_FUNDS_INFO_FAILURE:
      return {
        list: [],
        total: 0,
        page: 0,
        loading: false,
      }
    case types.ADMIN_GET_FUNDS_INFO_SUCCESS:
      return {
        list: get(action, 'req.data.content', []),
        total: get(action, 'req.data.totalElements', 0),
        page: get(action, 'req.data.number', 0),
        loading: false,
      }
    default:
      return state
  }
}

export default fundsInfo
