import httpClient from 'utils/http'
import * as types from './constants';

export const getNotification = (type, size) => {
  return dispatch => {
    return httpClient.get(`notifications?page=0&size=${size}&notificationType=${types.NOTIFICATION_TYPE[type]}`)
      .then((res) => {
        dispatch({
          type: types.REDUCER_TYPE[type],
          data: res.data
        })
        return res.data
      })
  }
}

