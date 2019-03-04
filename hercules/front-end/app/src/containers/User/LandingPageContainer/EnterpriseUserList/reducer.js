import {message} from 'antd';
import * as types from './constants'

export const initialState = {
  userList: {}
}

const enterpriseUserListReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.GET_ENTERPRISE_USER_LIST_SUCCESS:
        return Object.assign({}, state, {
          userList: action.userList
        })
      case types.MANAGE_USER_STATUS_FAIL:
        message.error('用户(启用/停用)状态修改失败！')
        return state;
      default:
        return state
    }
  }

export default enterpriseUserListReducer
