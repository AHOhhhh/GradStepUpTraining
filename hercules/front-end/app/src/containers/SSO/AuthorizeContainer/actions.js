import httpClient from 'utils/http'

import {
  REDIRECTED,
  SCOPE
} from './constants'

function redirectToApplicaiton(data) {
  window.location.href = data.url;
  return { type: REDIRECTED };
}

export function authorizeApplication(query, token) { //eslint-disable-line
  const url = `/oauth2/authorize?client_id=${query.client_id}&redirect_uri=${encodeURI(query.redirect_uri)}&scope=${SCOPE}&state=${query.state}&response_type=code`;
  return dispatch => {
    return httpClient.post(url, null, {
      'Content-Type': 'application/x-www-form-urlencoded'
    })
      .then(res => dispatch(redirectToApplicaiton(res.data)))
      .catch(err => console.log(err)); // eslint-disable-line
  }
}