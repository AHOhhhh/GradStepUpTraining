/* eslint-disable */
import { browserHistory } from 'react-router'
import { cookie, getAuthInfoFromToken } from 'utils'
import { getStoreInstance } from '../../store'
import { get } from 'lodash'
import { LOGOUT_USER, SHOW_SESSION_EXPIRED_MASK, SYNC_USER_INFO } from '../../containers/User/LoginContainer/constants'
import { AUTHENTICATION_ERROR_MAP, FORBIDDEN_ERROR_MAP } from '../../constants'
import errorNotification from '../notification/errorNotification';

export default (interceptor) => {
  interceptor.response.use(function (response) {
    if (response.headers.authorization) {
      cookie.set('TOKEN', response.headers.authorization)
    }
    return response
  },
    function (error) {
      console.log(error);
      const blockedNotificationCode = [31012, 31013, 31014, 31015]
      const store = getStoreInstance()
      switch (error.status) {
        case 401:
          const code = get(error, 'data.code')
          const authError = AUTHENTICATION_ERROR_MAP[code]
          if (authError && !blockedNotificationCode.includes(code)) {
            errorNotification('错误', authError + ',请重新登录');
            cookie.remove('TOKEN')
            window.localStorage.clear()
            store.dispatch({ type: LOGOUT_USER })
            setTimeout(() => {
              if (store.getState().routing.locationBeforeTransitions.pathname.indexOf('/admin/') > -1) {
                browserHistory.push('/admin/login')
              } else {
                browserHistory.push('/login')
              }
            }, 2000)
          }
          if (code === 31015) {
            store.dispatch({ type: SHOW_SESSION_EXPIRED_MASK })
          }
          break
        case 403:
          const token = cookie.get('TOKEN')
          if (!token) {
            store.dispatch({ type: LOGOUT_USER })
          }
          const errorCode = get(error, 'data.code')
          const forbiddenError = FORBIDDEN_ERROR_MAP[errorCode]
          if (errorCode) {
            if (forbiddenError) {
              errorNotification('无权限', forbiddenError)
            }
            setTimeout(() => {
              browserHistory.push('/')
            }, 200)
          }
          break
        case 412:
          errorNotification('以下字段包含非法字符，请重新输入', error.data.message)
          break
        case 400:
          errorNotification('业务系统异常', '客户端错误，请重试！')
          break
        case 500:
          errorNotification('业务系统异常', '服务器内部错误，无响应！')
          break
        default:
          errorNotification('错误', JSON.parse(error.request.responseText).message);
          browserHistory.push('/')
          break
      }
      if (!error.message) {
        error.message = '业务系统异常'
      }
      return Promise.reject(error)
    }
  )

  interceptor.request.use(function (config) {
    const token = cookie.get('TOKEN')
    const store = getStoreInstance()
    const authInfo = getAuthInfoFromToken(token)
    if ((token != null || token != undefined) && needSetAuthHeader(config)) {
      config.headers.authorization = token
    }

    if (needSetAuthHeader(config) && !token) {
      store.dispatch({ type: LOGOUT_USER })
      return Promise.reject({ status: 401 })
    }

    if (needSetAuthHeader(config) && store.getState().auth.userId !== authInfo.userId) {
      store.dispatch({ type: SYNC_USER_INFO, data: authInfo })
    }
    return config
  }, function (err) {
    return Promise.reject(err)
  })
}

function needSetAuthHeader(config) {
  let needSetAuthHeader = true
  const uri = config.url
  if (
    /.*\/webapi\/login.*/.test(uri) ||
    /.*\/webapi\/admin\/login.*/.test(uri) ||
    /.*\/webapi\/acg\/orders\?.*/.test(uri)
  ) {
    needSetAuthHeader = false
  }
  if (/enterprise-admin\/?$/.test(uri) && config.method === 'post') {
    needSetAuthHeader = false
  }
  return needSetAuthHeader
}

/* eslint-enable */