
export const ORDER_STATUS = {
  submitted: 'Submitted',
  orderAccepted: 'OrderAccepted',
  orderRejected: 'OrderRejected',
  waitForUploadSupplementaryFiles: 'WaitForUploadSupplementaryFiles',
  waitForFinancier: 'WaitForFinancier',
  waitForFinancierOffer: 'WaitForFinancierOffer',
  closed: 'Closed'
}

export const STEPS_CONSTANT = [
  {id: 1, text: '订单已提交', iconType: 'check'},
  {id: 2, text: '订单审核', iconType: 'tag-o'},
  {id: 3, text: '补充材料', iconType: 'file-text'},
  {id: 4, text: '申请授信额度', iconType: 'credit-card'},
  {id: 5, text: '服务完成', iconType: 'check'},
]
