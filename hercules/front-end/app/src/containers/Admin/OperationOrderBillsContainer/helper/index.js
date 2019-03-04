const constant = {
  platformAdmin: 'PlatformAdmin',
  operatorAdmin: 'PlatformAdmin',
  online: '线上',
  offline: '线下',
  afterPayment: '后付',
  received: '已收取',
  waitForReceive: '待收取',
  settled: '已结算',
  waitForSettled: '待结算',
  noNeedSettle: '无需结算',
  deducted: '已扣除',
  waitForDeduct: '待扣除',
  noNeedDeduct: '无需扣除'
}

export default (userRole, orderBill) => {
  const {
    platformAdmin,
    operatorAdmin,
    online,
    offline,
    afterPayment,
    received,
    waitForReceive,
    settled,
    waitForSettled,
    noNeedSettle,
    deducted,
    waitForDeduct,
    noNeedDeduct
  } = constant

  const {
    payMethod,
    productChargeSettled,
    serviceChargeSettled,
    commissionChargeSettled
  } = orderBill

  const isPlatformAdmin = userRole === platformAdmin
  const isOperatorAdmin = userRole === operatorAdmin
  const isOnline = payMethod === online
  const isOffline = payMethod === offline
  const isAfterPayment = payMethod === afterPayment

  const getPlatformAdminResult = () => {
    let titleMap = {}

    if (isOnline) {
      titleMap = {
        productCharge: productChargeSettled ? received : waitForReceive,
        serviceCharge: serviceChargeSettled ? settled : waitForSettled,
        commissionCharge: commissionChargeSettled ? deducted : waitForDeduct
      }
    }

    if (isOffline) {
      titleMap = {
        productCharge: productChargeSettled ? received : waitForReceive,
        serviceCharge: noNeedSettle,
        commissionCharge: commissionChargeSettled ? received : waitForReceive
      }
    }

    if (isAfterPayment) {
      titleMap = {
        productCharge: productChargeSettled ? received : waitForReceive,
        serviceCharge: noNeedSettle,
        commissionCharge: commissionChargeSettled ? received : waitForReceive
      }
    }

    return titleMap
  }

  const getOperatorAdminResult = () => {
    let titleMap = getPlatformAdminResult();

    if (isOnline) {
      titleMap = {
        ...titleMap,
        commissionCharge: noNeedDeduct
      }
    }

    if (isOffline) {
      titleMap = {
        ...titleMap,
        commissionCharge: commissionChargeSettled ? deducted : waitForDeduct
      }
    }

    if (isAfterPayment) {
      titleMap = {
        ...titleMap,
        commissionCharge: commissionChargeSettled ? deducted : waitForDeduct
      }
    }

    return titleMap
  }

  if (isPlatformAdmin) {
    return getPlatformAdminResult()
  }

  if (isOperatorAdmin) {
    return getOperatorAdminResult()
  }
}

