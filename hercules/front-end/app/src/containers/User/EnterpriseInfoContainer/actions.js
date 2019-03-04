import httpClient from 'utils/http'
import {USER_ROLE} from 'constants'

import {
  GET_ENTERPRISE_INFO,
  GET_ATTACHMENT_IMAGE,
  GET_ENTERPRISE_HISTORIES,
  CLEAR_ENTERPRISE_INFO
} from './constants'

import {GET_USER_BY_ID, CLEAR_USER_INFO} from './../LoginContainer/constants'

export function getEnterpriseInfo(id) { //eslint-disable-line
  return function (dispatch) {
    dispatch(
      {
        type: GET_ENTERPRISE_INFO,
        promise: httpClient.get('/enterprises/' + id)
      }
    )
  }
}

export function getAttachmentImage(file) {
  return dispatch => dispatch({
    type: GET_ATTACHMENT_IMAGE,
    promise: httpClient.get('/file/pictures/' + file, {
      responseType: 'arraybuffer'
    })
  })
}

export const getEnterpriseHistory = (id) => ({
  type: GET_ENTERPRISE_HISTORIES,
  promise: httpClient.get(`/enterprises/histories/${id}`)
})

export function clearEnterpriseInfo() { //eslint-disable-line
  return dispatch => dispatch({
    type: CLEAR_ENTERPRISE_INFO
  })
}

export function getUserById(id, role) {
  return dispatch => {
    dispatch({
      type: CLEAR_USER_INFO
    })
    const uri = role === USER_ROLE.enterpriseAdmin ? '/enterprise-admin/' : '/enterprise-user/'
    return httpClient.get(uri + id)
      .then(response => {
        dispatch({
          type: GET_USER_BY_ID,
          data: response.data
        })
        window.localStorage.setItem('user', JSON.stringify(response.data))
        return response;
      })
  }
}
