export const GET_ENTERPRISE_USER_LIST = 'GET_ENTERPRISE_USER_LIST'
export const GET_ENTERPRISE_USER_LIST_SUCCESS = 'GET_ENTERPRISE_USER_LIST_SUCCESS'
export const MANAGE_USER_STATUS = 'MANAGE_USER_STATUS'
export const MANAGE_USER_STATUS_FAIL = 'MANAGE_USER_STATUS_FAIL'

export const GET_NOTIFICATION = 'GET_NOTIFICATION'
export const GET_LOGISTICS = 'GET_LOGISTICS'

export const NOTIFICATION_TYPE = {
  notification: 'OrderNotification',
  logistics: 'LogisticsNotification'
}

export const REDUCER_TYPE = {
  notification: GET_NOTIFICATION,
  logistics: GET_LOGISTICS
}

export const MESSAGE_SIZE = {
  notification: 'notificationPageSize',
  logistics: 'logisticsPageSize'
}
