import httpClient from 'utils/http'

import {
  ID_APP_KEY,
  CALLBACK_URL,
  STATE,
  REDIRECTED
} from './constants'

function openWindow(data) {
  window.open(data.url, '_blank');
  return { type: REDIRECTED };
}

export function generateRedirectUrl() { //eslint-disable-line
  const url = `/oauth2/authorize?client_id=${ID_APP_KEY}&redirect_uri=${encodeURI(CALLBACK_URL)}&scope=user-info&state=${STATE}&response_type=code`;
  return dispatch => {
    return httpClient.post(url, {}, {
      headers: {'Content-Type': 'application/json'}
    })
      .then(res => dispatch(openWindow(res.data)))
      .catch(err => console.log(err)) //eslint-disable-line
  }
}