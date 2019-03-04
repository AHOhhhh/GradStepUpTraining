import httpClient from 'utils/http'
import _ from 'lodash'
import * as types from './constants';

const requestStatus = {
  ENABLED: 'DISABLED',
  DISABLED: 'ENABLED'
}

export function createEnterpriseUser(params) {

  params = _.omitBy(params, val => {
    return !val
  })

  return dispatch => { // eslint-disable-line
    return httpClient.post('/enterprise-user', params)
  }
}

export function getEnterpriseUsers(id, pageSize, pageNum) {
  return dispatch => {
    return httpClient.get(`/enterprises/${id}/users?size=${pageSize}&page=${pageNum}`)
      .then(res => {
        return dispatch({
          type: types.GET_ENTERPRISE_USER_LIST_SUCCESS,
          userList: res.data
        })
      })
  }
}

export function manageUserStatus(currentUser) {
  return dispatch => {
    return dispatch({
      type: types.MANAGE_USER_STATUS,
      promise: httpClient.post('/enterprise-user/' + currentUser.id + '/status', {status: requestStatus[currentUser.status]})
    })
  }
}