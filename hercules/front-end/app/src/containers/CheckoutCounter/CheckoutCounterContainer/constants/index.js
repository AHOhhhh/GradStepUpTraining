export {BANK_LIST} from './imagesMap'
export {PAYMENT_MESSAGE} from '../components/PaymentForm/constants'
export {ORDER_STATUS} from '../../OfflinePaymentSection/constants'

export const PAYMENT_STATUS = {
  SUCCESS: ['Paid', 'Closed', 'OrderTracking'],
  FAILURE: 'Fail',
  WAITING: 'PayInProcess'
}

export const PAYMENT_METHOD = {
  ONLINE: 'ONLINE',
  OFFLINE: 'OFFLINE'
}

export const ERROR_MESSAGE = '服务器出错！'


export const SET_ORDER_PRICE = 'SET_ORDER_PRICE'
export const HANDLE_SPIN = 'HANDLE_SPIN'

export const GET_PAYMENT_METHOD = 'GET_PAYMENT_METHOD'
export const GET_PAYMENT_METHOD_SUCCESS = 'GET_PAYMENT_METHOD_SUCCESS'

export const GET_PAYEE_INFO = 'GET_PAYEE_INFO'
export const GET_PAYEE_INFO_SUCCESS = 'GET_PAYEE_INFO_SUCCESS'

export const SET_SUBMIT_INFO = 'SET_SUBMIT_INFO'

export const UN_CHECK_OUT = 'unCheckout'
export const CHECKING = 'checking'

export const SET_OFFLINE_PAYMENT_AUDIT_OPINION = 'SET_OFFLINE_PAYMENT_AUDIT_OPINION'