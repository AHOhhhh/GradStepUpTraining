import httpClient from 'utils/http'
import * as types from './constants'

export function updateUser(userId, isAdmin, user) {
  const uri = isAdmin ? '/enterprise-admin/' : '/enterprise-user/'
  return dispatch => { // eslint-disable-line
    return httpClient.post(uri + userId, user)
      .then(res => {
        return dispatch(
          {
            type: types.UPDATE_USER_SUCCESS,
            userInfo: res.data,
            isAdmin
          }
        )
      })
  }
}
