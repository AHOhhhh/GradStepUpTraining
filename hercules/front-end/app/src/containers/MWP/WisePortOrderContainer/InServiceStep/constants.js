import {guid} from 'utils'

export const GET_IN_SERVICE_PRODUCTS = 'GET_IN_SERVICE_PRODUCTS'
export const GET_IN_SERVICE_PRODUCTS_SUCCESS = 'GET_IN_SERVICE_PRODUCTS_SUCCESS'
export const GET_IN_SERVICE_PRODUCTS_FAILURE = 'GET_IN_SERVICE_PRODUCTS_FAILURE'

export const REDIRECTED = 'REDIRECTED'

const MWP = {
  ID_APP_KEY: 'e8cfc01b37b8e9f27af09ffae08eae36',
  STATE: guid()
}

const SCF = {
  ID_APP_KEY: '5ccb297155535d3957bbe4b805829c1c',
  STATE: guid()
}

export const SSO_CONSTANT = {
  mwp: MWP,
  scf: SCF,
}

export const billWayStatusMaps = {
  1: '待提交QP',
  2: '已提交QP',
  3: '调用失败',
  4: 'QP调用成功',
  '011': '成功入海关预录入库',
  '012': '担保放行',
  '013': '退单或入库失败',
  '014': '挂起',
  '016': '接单交单',
  '022': '结关',
  '024': '报关单查验',
  '025': '报关单放行',
  '005': '失败',
  '008': '申报成功',
  5: '异常结束',
  not_make: '未制作',
  making: '制作中'
}

export const BUSINESS_LINE = 'mwp'

export const SCOPE = 'enterprise-info,user-info'
