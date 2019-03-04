import {cookie} from 'utils'
import {BASE64_DEMO} from '../../../../components/ImageUploader/base64Demo'
import * as types from '../constants'
import * as tools from '../tools'

cookie.init({
  headers: {
    'Set-Cookie': ''
  }
})

export const initialState = {
  orders: {},
  scfOrders: {},
  enterprises: {},
  platformOperations: {},
  orderOperations: {},
  error: null,
  success: null,
  failed: null,
  enterpriseInfo: {},
  enterprisesInfo: {},
  imageUrl: '',
  enterpriseAdmin: {},
  enterpriseInProgress: {},
  enterpriseAuthorized: {},
  enterpriseTabsKey: null
}

const PlatformAdminReducer =
  (state = initialState, action) => {
    switch (action.type) {
      case types.ADMIN_GET_ENTERPRISES_IN_PROCESS_REQUEST:
        return Object.assign({}, state, {
          ...state,
          success: null,
          failed: null,
        })
      case types.ADMIN_GET_ENTERPRISES_IN_PROCESS_SUCCESS:
        return Object.assign({}, state, {
          ...state,
          enterpriseInProgress: action.data.data,
          success: true,
          failed: false
        })
      case types.ADMIN_GET_ENTERPRISES_IN_PROCESS_FAILURE:
        return Object.assign({}, state, {
          ...state,
          success: false,
          failed: true,
          error: action.error
        })
      case types.GET_ORDERS_REQUEST:
        return Object.assign({}, state, {
          ...state,
          success: null,
          failed: null,
        })
      case types.GET_ORDERS_SUCCESS:
        return Object.assign({}, state, {
          ...state,
          orders: action.req.data,
          success: true,
          failed: false
        })
      case types.GET_ORDERS_FAILURE:
        return Object.assign({}, state, {
          ...state,
          success: false,
          orders: {},
          failed: true,
          error: action.error.data
        })
      case types.GET_SCF_ORDERS_REQUEST:
        return Object.assign({}, state, {
          ...state,
          success: null,
          failed: null,
        })
      case types.GET_SCF_ORDERS_SUCCESS:
        return Object.assign({}, state, {
          ...state,
          scfOrders: action.req.data,
          success: true,
          failed: false
        })
      case types.GET_SCF_ORDERS_FAILURE:
        return Object.assign({}, state, {
          ...state,
          success: false,
          scfOrders: {},
          failed: true,
          error: action.error.data
        })
      case types.ADMIN_GET_ENTERPRISE_SUCCESS:
        return Object.assign({}, state, {
          enterpriseInfo: action.req.data
        })
      case types.ADMIN_GET_ATTACHMENT_IMAGE_SUCCESS:
        return Object.assign({}, state, {
          imageUrl: action.req.data
        })
      case types.ADMIN_GET_ATTACHMENT_IMAGE_FAILURE:
        return Object.assign({}, state, {
          imageUrl: BASE64_DEMO  // TODO: not working, need to figure out why
        })
      case types.ADMIN_GET_ENTERPRISE_ADMIN_SUCCESS:
        return Object.assign({}, state, {
          enterpriseAdmin: action.req.data
        })
      case types.ADMIN_GET_ENTERPRISES_AUTHORIZED_REQUEST:
        return Object.assign({}, state, {
          ...state,
          success: null,
          failed: null,
        })
      case types.ADMIN_GET_ENTERPRISES_AUTHORIZED_SUCCESS:
        return Object.assign({}, state, {
          ...state,
          enterpriseAuthorized: action.req.data,
          success: true,
          failed: false
        })
      case types.ADMIN_GET_ENTERPRISES_AUTHORIZED_FAILURE:
        return Object.assign({}, state, {
          ...state,
          success: false,
          failed: true,
          error: action.error.data
        })
      case types.MANAGE_ENTERPRISE_STATUS_FAILURE:
        tools.renderModifyStatusError()
        return state
      case types.ADMIN_GET_USER_BY_ID_SUCCESS:
        return {
          ...state,
          auth: {
            user: action.req.data
          }
        }
      case types.REFRESH_CAPTCHA:
        return {
          ...state,
          auth: {
            captchaUuid: action.uuid
          }
        }
      case types.ADMIN_GET_PLATFORM_OPERATIONS_REQUEST:
        return Object.assign({}, state, {
          ...state,
          success: null,
          failed: null,
        })
      case types.ADMIN_GET_PLATFORM_OPERATIONS_FAILURE:
        return Object.assign({}, state, {
          ...state,
          success: false,
          platformOperations: {},
          failed: true,
          error: action.error.data
        })
      case types.ADMIN_GET_PLATFORM_OPERATIONS_SUCCESS:
        return Object.assign({}, state, {
          ...state,
          platformOperations: action.req.data,
          success: true,
          failed: false
        })
      case types.ADMIN_GET_ORDER_OPERATIONS_REQUEST:
        return Object.assign({}, state, {
          ...state,
          success: null,
          failed: null,
        })
      case types.ADMIN_GET_ORDER_OPERATIONS_FAILURE:
        return Object.assign({}, state, {
          ...state,
          success: false,
          orderOperations: {},
          failed: true,
          error: action.error.data
        })
      case types.ADMIN_GET_ORDER_OPERATIONS_SUCCESS:
        return Object.assign({}, state, {
          ...state,
          orderOperations: action.req.data,
          success: true,
          failed: false
        })
      case types.ADMIN_SET_ENTERPRISE_TABS_KEY:
        return Object.assign({}, state, {
          ...state,
          enterpriseTabsKey: action.key
        })
      default:
        return state
    }
  }

export default PlatformAdminReducer
