import httpClient from '../../../../utils/http/index'

export function resetPassword(userId, values) {
  return dispatch => { // eslint-disable-line
    return httpClient.post(`/enterprise-user/${userId}/reset-password`, {password: values.password})
  }
}