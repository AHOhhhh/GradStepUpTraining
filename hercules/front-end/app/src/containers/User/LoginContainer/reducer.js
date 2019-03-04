import {guid, getAuthInfoFromToken, cookie} from 'utils'
import {AUTHENTICATION_ERROR_MAP, SERVER_ERROR} from 'constants'
import * as types from './constants'
import * as userMessageTypes from '../LandingPageContainer/Message/constants'
import * as signupTypes from '../SignUpContainer/constants'
import * as adminLoginTypes from '../../Admin/share/constants'

cookie.init({
  headers: {
    'Set-Cookie': ''
  }
})

export const initialState = {
  userId: getAuthInfoFromToken().userId,
  enterpriseId: getAuthInfoFromToken().enterpriseId,
  user: cookie.get('TOKEN') ? JSON.parse(window.localStorage.getItem('user')) : null,
  token: cookie.get('TOKEN'),
  captchaUuid: guid(),
  signupUserName: null,
  errorMessage: '',
  signupErrorCode: null,
  signupErrorMessage: null,
  showSessionExpiredMask: false,
  notification: {content: [], totalElements: 0},
  logistics: {content: [], totalElements: 0}
}

const loginReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.LOGIN_USER_SUCCESS:
        return {
          ...state,
          token: action.data.headers.authorization,
          userId: getAuthInfoFromToken(action.data.headers.authorization).userId,
          enterpriseId: getAuthInfoFromToken(action.data.headers.authorization).enterpriseId,
        }
      case types.LOGIN_USER_FAILURE:
        return {
          ...state,
          token: null,
          user: null,
          captchaUuid: guid(),
          errorMessage: action.error.data ? AUTHENTICATION_ERROR_MAP[action.error.data.code] : SERVER_ERROR
        }
      case userMessageTypes.GET_NOTIFICATION:
        return {
          ...state,
          notification: action.data,
        }
      case userMessageTypes.GET_LOGISTICS:
        return {
          ...state,
          logistics: action.data,
        }
      case types.GET_USER_BY_ID:
        return {
          ...state,
          user: action.data
        }
      case types.CLEAR_USER_INFO:
        return {
          ...state,
          user: Object.assign(state.user, {cellphone: '', email: '', fullname: '', telephone: '', username: ''})
        }
      case types.REFRESH_CAPTCHA:
        return {
          ...state,
          captchaUuid: action.uuid
        }
      case types.LOGOUT_USER:
        return {
          ...state,
          token: null,
          user: null,
          userId: null,
          enterpriseId: null,
          errorMessage: '',
          showSessionExpiredMask: false
        }
      case types.SYNC_USER_INFO:
        window.localStorage.setItem('user', JSON.stringify(Object.assign(JSON.parse(window.localStorage.getItem('user')), action.data)))
        return {
          ...state,
          userId: action.data.userId,
          enterpriseId: action.data.enterpriseId,
          user: Object.assign({}, state.user, {id: action.data.userId}, {role: action.data.role}, {username: action.data.userName}, {enterpriseId: action.data.enterpriseId})
        }
      case types.UPDATE_USER_SUCCESS:
        if (action.isAdmin) {
          window.localStorage.setItem('user', JSON.stringify(action.isAdmin ? action.userInfo : state.user))
        }
        return {
          ...state,
          user: action.isAdmin ? action.userInfo : state.user
        }
      case signupTypes.SIGNUP_USER_SUCCESS:
        window.localStorage.setItem('user', JSON.stringify(action.req.data))
        return {
          ...state,
          token: action.req.headers.authorization,
          userId: action.req.data.id,
          user: action.req.data,
          signupUserName: action.req.data.username,
          signupErrorCode: null,
          signupErrorMessage: null,
        }
      case signupTypes.SIGNUP_USER_FAILURE:
        return {
          ...state,
          signupUserName: null,
          signupErrorCode: (action.error.data || {}).code,
          signupErrorMessage: (action.error.data || {}).message,
        }
      case signupTypes.CLEAR_SIGNUP_DATA:
        return {
          ...state,
          signupUserName: null,
          signupErrorCode: null,
          signupErrorMessage: null,
        }
      case types.SHOW_SESSION_EXPIRED_MASK:
        return {
          ...state,
          showSessionExpiredMask: true
        }
      case types.SET_USER_ENTERPRISE_ID:
        const user = Object.assign({}, JSON.parse(window.localStorage.getItem('user')), {enterpriseId: action.enterpriseId})
        window.localStorage.clear()
        window.localStorage.setItem('user', JSON.stringify(user))
        return {
          ...state,
          enterpriseId: action.enterpriseId
        }

      case adminLoginTypes.ADMIN_LOGIN_USER_SUCCESS:
        const tokenObject = getAuthInfoFromToken(action.data.headers.authorization)
        const admin = {id: tokenObject.userId, username: tokenObject.userName, resettable: true, role: tokenObject.role, privileges: tokenObject.privileges}
        window.localStorage.clear()
        window.localStorage.setItem('user', JSON.stringify(admin))
        return {
          ...state,
          token: action.data.headers.authorization,
          user: Object.assign({}, admin),
          userId: admin.userId
        }
      case adminLoginTypes.ADMIN_LOGIN_USER_FAILURE:
        return {
          ...state,
          token: null,
          user: null,
          captchaUuid: guid(),
          errorMessage: AUTHENTICATION_ERROR_MAP[action.error.data.code] || SERVER_ERROR
        }
      default:
        return state
    }
  }

export default loginReducer
