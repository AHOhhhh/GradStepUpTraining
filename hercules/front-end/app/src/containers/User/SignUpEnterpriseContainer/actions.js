import {browserHistory} from 'react-router'
import httpClient from 'utils/http'

import * as types from './../LoginContainer/constants'

function enterpriseRequest(params, isEdit) {
  delete params.payMethods
  return isEdit ? (httpClient.post('/enterprises/' + params.id, params)) : (httpClient.post('/enterprises', params))
}

export function signupEnterprise(params, isEdit) { //eslint-disable-line
  return dispatch => { // eslint-disable-line
    return enterpriseRequest(params, isEdit).then(res => {
      dispatch({
        type: types.SET_USER_ENTERPRISE_ID,
        enterpriseId: res.data
      })
      const navPath = isEdit ? '/enterprise_info' : '/signup_succeed'
      browserHistory.push(navPath)
    })
  }
}

export function deleteFile(params) {
  return dispatch => { // eslint-disable-line
    return httpClient.post('/file/pictures/deletion' + params)
  }
}