import { get, map, find } from 'lodash'

const mapAddresses = (shippingInfo, contacts) => {
  const isPickUp = shippingInfo.departure.isPickUpOrDropOff
  const isDropOff = shippingInfo.arrival.isPickUpOrDropOff

  const pickUpAddress = isPickUp ? find(contacts, {id: shippingInfo.departure.addressId}) : {}
  const dropOffAddress = isDropOff ? find(contacts, {id: shippingInfo.arrival.addressId}) : {}

  return {
    pickUpAddress,
    dropOffAddress,
  }
}

const mapShippingInfo = (shippingInfo) => {
  return {
    departure: {
      airportId: get(shippingInfo, 'departure.airport.airportId'),
      delivery: get(shippingInfo, 'departure.isPickUpOrDropOff'),
    },
    arrival: {
      airportId: get(shippingInfo, 'arrival.airport.airportId'),
      delivery: get(shippingInfo, 'arrival.isPickUpOrDropOff'),
    }
  }
}

const mapGoods = (productList) => {
  return map(productList, (product) => ({
    id: Math.random() * 100,
    name: product.name,
    size: {
      length: product.length,
      width: product.width,
      height: product.height,
      unit: 'cm'
    },
    weight: {
      value: product.grossWeight,
      unit: 'kg'
    },
    price: product.unitPrice,
    currency: product.currency,
    quantity: product.totalAmount,
    packageQuantity: product.packageAmount,
    declarationInfo: product.description
  }))
}

export const calcPriceMapper = ({productList, shippingInfo, contacts}, pramas = {}) => {
  return {
    goods: mapGoods(productList),
    ...mapShippingInfo(shippingInfo),
    ...mapAddresses(shippingInfo, contacts),
    estimatedDeliveryTime: shippingInfo.estimatedDeliveryTime,
    ...pramas
  }
}

export const createOrderMapper = ({productList, shippingInfo, contact, contacts, orderPrice, acgPrimaryNum, agentCode}, referenceOrderId) => {
  return {
    contact,
    goods: mapGoods(productList),
    currency: 'CNY',
    price: orderPrice,
    shippingInfo: {
      ...mapShippingInfo(shippingInfo),
      ...mapAddresses(shippingInfo, contacts),
      estimatedDeliveryTime: shippingInfo.estimatedDeliveryTime,
    },
    referenceOrderId,
    acgPrimaryNum,
    agentCode
  }
}

export const formatProductInfo = (product, currency) => ({
  name: product.name,
  weight: {
    value: product.grossWeight
  },
  size: {
    length: product.length,
    width: product.width,
    height: product.height
  },
  price: product.unitPrice,
  currency,
  quantity: product.totalAmount,
  packageQuantity: product.packageAmount,
  declarationInfo: product.description
})
