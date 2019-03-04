import OrderTrackingContainer from '../OrderTrackingContainer'
import PaymentContainer from '../PaymentContainer'
import PositionConfirmation from '../PositionConfirmation'

export const ORDER_STATUS = {
  submitRequirements: '',
  waitForSpace: 'Submitted',
  pendingPayment: 'WaitForPay',
  orderTracking: 'OrderTracking',
  closed: 'Closed',
  cancelled: 'Cancelled',
  offlinePaidAwaitingConfirm: 'OfflinePaidAwaitingConfirm'
}

export const ACG_STEPS = [{
  id: 1,
  status: [ORDER_STATUS.submitRequirements],
  cancelReason: '',
  text: '需求已提交',
  iconType: 'check'
}, {
  id: 2,
  status: [ORDER_STATUS.waitForSpace],
  cancelReason: 'BookingFailure',
  text: '待舱位确认',
  iconType: 'tag-o',
  StepComponent: PositionConfirmation
}, {
  id: 3,
  status: [ORDER_STATUS.pendingPayment, ORDER_STATUS.offlinePaidAwaitingConfirm],
  cancelReason: '',
  text: '待支付',
  iconType: 'pay-circle-o',
  StepComponent: PaymentContainer
}, {
  id: 4,
  status: [ORDER_STATUS.orderTracking],
  cancelReason: '',
  text: '订单跟踪',
  iconType: 'environment-o',
  StepComponent: OrderTrackingContainer
}, {
  id: 5,
  status: [ORDER_STATUS.closed],
  cancelReason: '',
  text: '服务完成',
  iconType: 'check',
  StepComponent: OrderTrackingContainer
}]

export const INCLUDE_ORDER_PRICE_STATUS = ['OfflinePaidAwaitingConfirm', 'WaitForPay', 'OrderTracking']
