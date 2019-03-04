import axios from 'axios'
import httpClient from '../../../../../utils/http/index'
import * as types from './constants'

const getUserIdList = (orderChangeRecords) => {
  return orderChangeRecords.map(record => record.createdBy)
}

export const getUserInfo = (orderChangeRecords) => {
  return (dispatch) => {
    return axios.all(getUserIdList(orderChangeRecords).map(userId => httpClient.get(`/users/${userId}`)))
      .then(axios.spread((...users) => {
        return orderChangeRecords.map((record) => {
          const user = users.find((item) => item.data.id === record.createdBy) || {data: {id: '1111', username: 'zhangpei'}}
          return Object.assign({}, record, {createdUser: user.data})
        })
      }))
      .then((orderChangeRecordList) => {
        dispatch({
          type: types.GET_ORDER_CHANGE_RECORDS,
          orderChangeRecords: orderChangeRecordList
        })
      })
  }
}