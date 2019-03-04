import {browserHistory} from 'react-router'
import httpClient from 'utils/http'
import * as types from './constants'

export function createSCFOrder(order) {
  return dispatch => {    // eslint-disable-line
    return httpClient.post('/scf/orders', order)
      .then(res => {
        browserHistory.push('/scf/orders/' + res.data.id)
      })
  }
}

export function getEnterpriseSCFQualification(id) {
  return dispatch => {    // eslint-disable-line
    return httpClient.get(`/scf/orders/enterprises/${id}/qualification`)
      .then(res => {
        dispatch(
          {
            type: types.GET_ENTERPRISE_SCF_QUALIFICATION_SUCCESS,
            res
          }
        )
        return res.data
      })
  }
}
