import httpClient from 'utils/http'

export function resetPassword(userId) {
  return dispatch => { // eslint-disable-line
    return httpClient.post(`/enterprise-admin/${userId}/reset-password`);
  }
}