import httpClient from 'utils/http'
import {get} from 'lodash'
import * as types from '../../share/constants'

export function adminGetEnterprisesInProcess(params) {
  return function (dispatch) {
    return httpClient.get('/enterprises', {params})
      .then(data => {
        dispatch({
          type: types.ADMIN_GET_ENTERPRISES_IN_PROCESS_SUCCESS,
          data
        })
      })
      .catch(error => {
        const code = get(error, 'data.code')
        if (code !== 31015) {
          dispatch({
            type: types.ADMIN_GET_ENTERPRISES_IN_PROCESS_FAILURE,
            error
          })
        }
      })
  }
}
