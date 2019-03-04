import {USER_ROLE} from 'constants'
import {getAuthInfoFromToken, cookie} from 'utils'
import httpClient from 'utils/http'
import * as types from './constants'

export function loginUser(params, redirect = '/') {
  return dispatch => {
    return httpClient.post('/login', Object.assign({}, params, redirect))
      .then(res => {
        dispatch({
          type: types.LOGIN_USER_SUCCESS,
          data: res
        })
        const authInfo = getAuthInfoFromToken(res.headers.authorization)
        const uri = authInfo.role === USER_ROLE.enterpriseAdmin ? '/enterprise-admin/' : '/enterprise-user/'
        return httpClient.get(uri + authInfo.userId)
      })
      .then(response => {
        dispatch({
          type: types.GET_USER_BY_ID,
          data: response.data
        })
        window.localStorage.setItem('user', JSON.stringify(response.data))
        return response;
      })
      .catch(error => {
        dispatch({
          type: types.LOGIN_USER_FAILURE,
          error
        })
      })
  }
}

export function refreshCaptcha(uuid) {
  return {
    type: types.REFRESH_CAPTCHA,
    uuid
  }
}

export function logoutAndRedirect() {
  return function (dispatch) {
    cookie.remove('TOKEN')
    window.localStorage.clear()
    dispatch(
      {
        type: types.LOGOUT_USER
      }
    )
  }
}