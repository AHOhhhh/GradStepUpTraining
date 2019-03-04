import httpClient from 'utils/http'
import {REDIRECTED} from './constants'

function openWindow(data) {
  window.open(data.url, '_blank');
  return {type: REDIRECTED};
}


export function generateSSORedirectUrl(businessLine, id) {
  const url = `/${businessLine}/orders/${id}/upload-url`
  return dispatch => {
    return httpClient.get(url, {}, {
      headers: {'Content-Type': 'application/json'}
    })
      .then(res => dispatch(openWindow(res.data)))
      .catch(err => console.log(err)) //eslint-disable-line
  }
}

export * from '../../containers/User/LoginContainer/actions'