export const statusMap = {
  Submitted: '待确认',
  Audited: '待支付',
  PendingPayment: '等待支付',
  Paid: '服务完成',
  InProgress: '服务中',
  Closed: '服务完成',
  Cancelled: '已取消',
  TenderOffer: '待确认服务商',
  WaitForPay: '待支付',
  Invalid: '无效',
  OrderTracking: '运输中',
  WaitForSignConfidentialAgreement: '待签署保密协议',
  WaitForUploadContract: '待上传纸质协议',
  WaitForReUploadContract: '待重新上传纸质协议',
  WaitForContractConfirmation: '提交审核',
  OfflinePaidAwaitingConfirm: '已支付待确认',
  OrderAccepted: '待确认融资需求',
  OrderRejected: '已取消',
  WaitForUploadSupplementaryFiles: '待补充材料',
  WaitForFinancier: '待申请融资授信',
  WaitForFinancierOffer: '待申请融资授信'
}

export const orderStatusMap = {
  mwp: {
    Submitted: '待确认服务商',
    TenderOffer: '待确认服务商',
    InProgress: '服务中',
    WaitForPay: '待支付',
    Paid: '服务完成',
    Closed: '服务完成',
    OfflinePaidAwaitingConfirm: '已支付待确认',
    Cancelled: '已取消'
  },
  wms: {
    Submitted: '待确认价格',
    Audited: '待支付',
    WaitForPay: '待支付',
    OfflinePaidAwaitingConfirm: '已支付待确认',
    Paid: '服务完成',
    Closed: '服务完成',
    Cancelled: '已取消'
  },
  acg: {
    Submitted: '待确认舱位',
    WaitForPay: '待支付',
    OrderTracking: '运输中',
    Paid: '运输中',
    Closed: '服务完成',
    OfflinePaidAwaitingConfirm: '已支付待确认',
    Cancelled: '已取消'
  },
  scf: {
    Submitted: '待确认融资需求',
    OrderAccepted: '待确认融资需求',
    OrderRejected: '已取消',
    WaitForUploadSupplementaryFiles: '待补充材料',
    WaitForFinancier: '待申请融资授信',
    WaitForFinancierOffer: '待申请融资授信',
    Closed: '服务完成'
  }
}

export const typeNameMap = {
  mwp: '关务服务',
  wms: 'WMS',
  scf: '供应链金融',
  acg: '航空货运'
}

export const typeVendorMap = {
  mwp: '口岸报关',
  wms: 'WMS',
  ssh: 'Hercules',
  yzra: '大力神货运'
}

export const serviceTypeMap = {
  Declaration_Clearance: '报关/清关',
  Agent: '进出口代理',
  Inspection_Declaration: '报检',
  Renew: '续费',
  Open: '开通',
  Recharge: '充值',
  PickUp: '上门取货',
  DropOff: '机场派送',
  AirCargo: '航空运输'
}
