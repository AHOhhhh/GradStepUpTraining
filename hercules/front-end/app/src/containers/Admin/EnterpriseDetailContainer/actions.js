import httpClient from 'utils/http'

import {
  ADMIN_GET_ENTERPRISE,
  ADMIN_GET_ATTACHMENT_IMAGE,
  ADMIN_GET_ENTERPRISE_ADMIN,
} from '../share/constants'

export function getEnterpriseInfo(id) { //eslint-disable-line
  return function (dispatch) {
    dispatch(
      {
        type: ADMIN_GET_ENTERPRISE,
        promise: httpClient.get('/enterprises/' + id)  // TODO: api may change
      }
    )
  }
}

export function getAttachmentImage(file) {
  return dispatch => dispatch({
    type: ADMIN_GET_ATTACHMENT_IMAGE,
    promise: httpClient.get('/file/pictures/' + file, {
      responseType: 'arraybuffer'
    })
  })
}

export function getEnterpriseAdminByEnterpriseId(id) {
  return dispatch => dispatch({
    type: ADMIN_GET_ENTERPRISE_ADMIN,
    promise: httpClient.get('/enterprises/' + id + '/admin')
  })
}
