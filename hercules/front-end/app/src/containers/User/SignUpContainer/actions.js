import httpClient from 'utils/http'

import {
  SIGNUP_USER,
  CLEAR_SIGNUP_DATA
} from './constants'

export function signupUser(params) {
  return function (dispatch) {
    dispatch({
      type: SIGNUP_USER,
      promise: httpClient.post('/enterprise-admin', Object.assign({}, params, {role: 'EnterpriseAdmin'}))
    })
  }
}

export function clearSignupData() {
  return function (dispatch) {
    dispatch({
      type: CLEAR_SIGNUP_DATA
    })
  }
}
