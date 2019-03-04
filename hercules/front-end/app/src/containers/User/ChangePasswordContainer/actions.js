import {USER_ROLE} from 'constants'
import {push} from 'react-router-redux'

import {errorMessage} from './maps';
import httpClient from '../../../utils/http'
import {
  SERVER_ERROR_MESSAGE,
  GET_USER_BY_ID,
} from './constants'

const getChangePasswordErrorMessage = (error) => {
  return errorMessage[error.data.message] || SERVER_ERROR_MESSAGE;
}

export function changePassword(user, value) {
  return function (dispatch) {
    let isChangePasswordSucceed = false
    return httpClient.post(`/enterprise-user/${user.id}/init-password`, value)
      .then(() => {
        isChangePasswordSucceed = true
        const uri = user.role === USER_ROLE.enterpriseAdmin ? '/enterprise-admin/' : '/enterprise-user/'
        return httpClient.get(uri + user.id)
      })
      .then(res => {
        window.localStorage.setItem('user', JSON.stringify(res.data))
        return res
      })
      .then(res => dispatch({
        type: GET_USER_BY_ID,
        data: res.data
      }))
      .then(() => {
        return {isSucceed: isChangePasswordSucceed, errorMessage: null}
      })
      .catch((errors) => ({isSucceed: isChangePasswordSucceed, errorMessage: getChangePasswordErrorMessage(errors)}))
  }
}

export function jumpHome() {
  return push('/')
}
