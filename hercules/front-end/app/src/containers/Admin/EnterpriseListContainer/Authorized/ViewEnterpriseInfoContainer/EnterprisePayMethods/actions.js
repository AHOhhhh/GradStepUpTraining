import httpClient from 'utils/http'
import * as types from './constants'

export function updateEnterprisePayMethods(enterpriseId, payMethods) {
  return dispatch => { // eslint-disable-line
    return httpClient.post(`/enterprises/${enterpriseId}/pay-methods`, payMethods);
  }
}

export function getEnterprisePayMethods(enterpriseId) {
  return {
    type: types.GET_ENTERPRISE_PAY_METHODS,
    promise: httpClient.get(`/enterprises/${enterpriseId}/pay-methods`)
  }
}