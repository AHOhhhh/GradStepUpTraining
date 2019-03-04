export const ORDER_STATUS = {
  submitted: 'Submitted',
  tenderOffer: 'TenderOffer',
  inProgress: 'InProgress',
  waitForPay: 'WaitForPay',
  paid: 'Paid',
  closed: 'Closed',
  offlinePaidAwaitingConfirm: 'OfflinePaidAwaitingConfirm'
}

const WISEPORT_STEPS_CONSTANT = [{
  id: 1,
  text: '需求已提交',
  status: ORDER_STATUS.submitted,
  iconType: 'check'
}, {
  id: 2,
  text: '选择服务商',
  status: ORDER_STATUS.tenderOffer,
  iconType: 'tag-o'
}, {
  id: 3,
  text: '服务中',
  status: ORDER_STATUS.inProgress,
  iconType: 'heart-o'
}, {
  id: 4,
  text: '待支付',
  status: [ORDER_STATUS.waitForPay, ORDER_STATUS.offlinePaidAwaitingConfirm],
  iconType: 'pay-circle-o'
}, {
  id: 5,
  text: '服务完成',
  status: [ORDER_STATUS.paid, ORDER_STATUS.closed],
  iconType: 'check'
}]

export default WISEPORT_STEPS_CONSTANT