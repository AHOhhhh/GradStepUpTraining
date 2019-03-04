import httpClient from 'utils/http'
import * as types from '../share/constants'

export function platformUserLogin(params, redirect = '/') {
  return dispatch => {
    const userName = params.username
    return httpClient.post('/admin/login', Object.assign({}, params, redirect))
      .then(res => {
        dispatch({
          type: types.ADMIN_LOGIN_USER_SUCCESS,
          data: {...res, userName}
        })
      }).catch(error => {
        dispatch({
          type: types.ADMIN_LOGIN_USER_FAILURE,
          error
        })
      })
  }
}