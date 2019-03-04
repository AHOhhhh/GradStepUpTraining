import { isObject, isEmpty, get, compact, groupBy, includes, find, join } from 'lodash'

const productsValidator = (products = []) => {
  const valid = products.length > 0
  return valid ? null : {
    field: 'products',
    validateStatus: 'error',
    help: '请添加货品',
  }
}

const airportValidator = (airportInfo) => {
  const valid = isObject(airportInfo.airport) && airportInfo.airport.airportId
  return valid ? null : {
    field: 'airport',
    validateStatus: 'error',
    help: '请填写机场',
  }
}

const addressValidator = (airportInfo) => {
  const valid = airportInfo.isPickUpOrDropOff
    ? airportInfo.addressId
    : true
  return valid ? null : {
    field: 'address',
    validateStatus: 'error',
    help: '请填写取货或派送地址',
  }
}

const airportMatchAddressValidator = (airportInfo) => {
  const { isPickUpOrDropOff, contacts, airport = {} } = airportInfo
  const address = find(contacts, {id: airportInfo.addressId}) || {}
  const valid = !isPickUpOrDropOff
    || includes(join(compact([address.country, address.province, address.district, address.city]), ''), airport.city)
  return valid ? null : {
    field: 'address',
    validateStatus: 'error',
    help: '该地址无法提供上门取货服务',
  }
}

const fromToValidator = (shippingInfo) => {
  const departureAirportId = get(shippingInfo, 'departure.airport.airportId')
  const arrivalAirportId = get(shippingInfo, 'arrival.airport.airportId')

  const invalid = arrivalAirportId && departureAirportId && arrivalAirportId === departureAirportId
  return invalid ? {
    field: 'airport',
    validateStatus: 'error',
    help: '始发港和到达港不能相同',
  } : null
}

const fromToAddressValidator = (shippingInfo) => {
  const departureAddressId = get(shippingInfo, 'departure.addressId')
  const arrivalAddressId = get(shippingInfo, 'arrival.addressId')
  const invalid = departureAddressId && departureAddressId && departureAddressId === arrivalAddressId
  return invalid ? {
    field: 'address',
    validateStatus: 'error',
    help: '取货地址和派送地址不能相同',
  } : null
}

const estimatedTimeValidator = (shippingInfo) => {
  const valid = !isEmpty(shippingInfo.estimatedDeliveryTime)
  return valid ? null : {
    field: 'estimatedDeliveryTime',
    validateStatus: 'error',
    help: '请填写航运时效',
  }
}

const primaryNumValidator = (shippingInfo) => {
  const valid = !isEmpty(shippingInfo.acgPrimaryNum.trim()) ? new RegExp(/^[A-Za-z0-9-]{0,12}$/).test(shippingInfo.acgPrimaryNum) : true
  return valid ? null : {
    field: 'acgPrimaryNum',
    validateStatus: 'error',
    help: '主单号只能由字母、数字、-组成且最长为12位',
  }
}

const agentCodeValidator = (shippingInfo) => {
  const valid = !isEmpty(shippingInfo.agentCode.trim()) ? new RegExp(/^.{0,20}$/).test(shippingInfo.agentCode) : true
  return valid ? null : {
    field: 'agentCode',
    validateStatus: 'error',
    help: '代理人代码最长为20位',
  }
}

export const validateProducts = (products) => groupBy(compact([
  productsValidator(products),
]), 'field')


export const validateAirportInfo = (airportInfo) => groupBy(compact([
  airportValidator(airportInfo),
  addressValidator(airportInfo),
  airportMatchAddressValidator(airportInfo),
]), 'field')

export const validateShippingInfo = (shippingInfo) => groupBy(compact([
  fromToValidator(shippingInfo),
  fromToAddressValidator(shippingInfo),
  estimatedTimeValidator(shippingInfo),
  primaryNumValidator(shippingInfo),
  agentCodeValidator(shippingInfo),
]), 'field')

export const hasErrorInForm = ({shippingInfo, productList, contacts}) => {
  const shippingInfoErrors = compact([
    airportValidator(shippingInfo.departure),
    airportValidator(shippingInfo.arrival),
    addressValidator(shippingInfo.departure),
    addressValidator(shippingInfo.arrival),
    airportMatchAddressValidator({contacts, ...shippingInfo.departure}),
    airportMatchAddressValidator({contacts, ...shippingInfo.arrival}),
    fromToValidator(shippingInfo),
    fromToAddressValidator(shippingInfo),
    estimatedTimeValidator(shippingInfo),
    primaryNumValidator(shippingInfo),
    agentCodeValidator(shippingInfo),
    productsValidator(productList),
  ])

  return shippingInfoErrors.length > 0
}
