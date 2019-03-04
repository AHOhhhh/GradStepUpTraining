// WisePortOrderContainer
export const WISE_PORT_GET_ORDER_DETAIL = 'WISE_PORT_GET_ORDER_DETAIL'
export const WISE_PORT_GET_ORDER_DETAIL_SUCCESS = 'WISE_PORT_GET_ORDER_DETAIL_SUCCESS'
export const WISE_PORT_GET_ORDER_DETAIL_FAILURE = 'WISE_PORT_GET_ORDER_DETAIL_FAILURE'

// WisePortPaymentStep
export const GET_ORDER_PRICE_DETAIL = 'GET_ORDER_PRICE_DETAIL'
export const GET_ORDER_PRICE_DETAIL_SUCCESS = 'GET_ORDER_PRICE_DETAIL_SUCCESS'
export const GET_ORDER_PRICE_DETAIL_FAILURE = 'GET_ORDER_PRICE_DETAIL_FAILURE'

export const GET_ORDER_OFFERS = 'GET_ORDER_OFFERS'
export const GET_ORDER_OFFERS_SUCCESS = 'GET_ORDER_OFFERS_SUCCESS'

export const GET_ORDER_DETAILS = 'GET_ORDER_DETAILS'
export const GET_ORDER_DETAILS_SUCCESS = 'GET_ORDER_DETAILS_SUCCESS'
export const GET_ORDER_DETAILS_FAILURE = 'GET_ORDER_DETAILS_FAILURE'

export const SET_ORDER_TOTAL_PRICE = 'SET_ORDER_TOTAL_PRICE'
export const COLUMNS = [{
  key: 0,
  title: '服务商',
  dataIndex: 'companyName',
}, {
  key: 1,
  title: '费用名称',
  dataIndex: 'itemName',
}, {
  key: 2,
  title: '价格',
  dataIndex: 'amount',
}];

export {PAYMENT_METHOD, ORDER_STATUS as PAYMENT_ORDER_STATUS} from '../../../CheckoutCounter/CheckoutCounterContainer/constants'

export const PAYMENT_STATUS = {
  PRICE_CHANGE: 'priceChange',
  NOT_START_PAYMENT: 'notStartPayment',
  PAYMENT_FAILURE: 'failure',
  PAYMENT_SUCCESS: 'success',
  OFFLINE_PAID_AWAITING_CONFIRM: 'OfflinePaidAwaitingConfirm'
}

export const SET_PAYMENT_STATUS = 'SET_PAYMENT_STATUS'

export const GET_ORDER_TOTAL_PRICE = 'GET_ORDER_TOTAL_PRICE'
export const GET_ORDER_TOTAL_PRICE_SUCCESS = 'GET_ORDER_TOTAL_PRICE_SUCCESS'
export const GET_ORDER_TOTAL_PRICE_FAILURE = 'GET_ORDER_TOTAL_PRICE_FAILURE'

export const SET_PRICE_CHANGED_STATUS = 'SET_PRICE_CHANGED_STATUS'

export const GET_OPERATION_DOCUMENTS = 'GET_OPERATION_DOCUMENTS'
export const GET_OPERATION_DOCUMENTS_SUCCESS = 'GET_OPERATION_DOCUMENTS_SUCCESS'
export const GET_OPERATION_DOCUMENTS_FAILURE = 'GET_OPERATION_DOCUMENTS_FAILURE'

export const GET_ORDER_RECORDS_CHANGE = 'GET_ORDER_RECORDS_CHANGE'
export const GET_ORDER_RECORDS_CHANGE_FAILURE = 'GET_ORDER_RECORDS_CHANGE_FAILURE'
export const GET_ORDER_RECORDS_CHANGE_SUCCESS = 'GET_ORDER_RECORDS_CHANGE_SUCCESS'

export const REFRESH_TIPS = '订单价格有变动，支付前如有疑问请联系客服人员。'

export const CANCEL_ORDER = 'CANCEL_ORDER'

export const serviceTypeMap = {
  Declaration_Clearance: {
    text: '报关/清关',
    styleName: 'import_clearance',
  },
  Agent: {
    text: '进出口代理',
    styleName: 'export_agent'
  },
  Inspection_Declaration: {
    text: '报检',
    styleName: 'export_declaration'
  }
}

export const WISE_PORT_ORDER_CANCEL_SUCCESS = 'WISE_PORT_ORDER_CANCEL_SUCCESS'

