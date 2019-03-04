import httpClient from 'utils/http'
import * as types from '../../share/constants'

const requestStatus = {
  ENABLED: 'DISABLED',
  DISABLED: 'ENABLED'
}

export function adminGetEnterprisesAuthorized(params) {
  return function (dispatch) {
    dispatch({
      type: types.ADMIN_GET_ENTERPRISES_AUTHORIZED,
      promise: httpClient.get('/enterprises', {params})
    })
  }
}

export function manageEnterpriseStatus(enterprise) {
  return dispatch => {
    return dispatch({
      type: types.MANAGE_ENTERPRISE_STATUS,
      promise: httpClient.post('/enterprises/' + enterprise.id + '/status', {status: requestStatus[enterprise.status]})
    })
  }
}

export function getEnterpriseAdminByEnterpriseId(enterpriseId) {
  return dispatch => dispatch({
    type: types.ADMIN_GET_ENTERPRISE_ADMIN,
    promise: httpClient.get('/enterprises/' + enterpriseId + '/admin')
  })
}