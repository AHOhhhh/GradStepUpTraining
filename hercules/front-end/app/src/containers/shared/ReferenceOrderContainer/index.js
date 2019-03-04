import React from 'react'
import _ from 'lodash'
import { orderIsSubmitted } from '../../ACG/OrderManagementContainer/utils/orderStatusUtils'
import adWisePortImage from '../assets/ad_wise_port.png'
import adAirCargoImage from '../assets/ad_air_cargo.png'
import ReferenceOrderContent from './ReferenceOrderContent'

const getReferenceOrderId = order => _.get(order, 'referenceOrder.id')

const hasRelatedOrder = (order) => !_.isEmpty(getReferenceOrderId(order))

const isAbroad = (order) => {
  const departureIsAbroad = _.get(order, 'shippingInfo.departure.abroad', false)
  const arrivalIsAbroad = _.get(order, 'shippingInfo.arrival.abroad', false)
  return departureIsAbroad || arrivalIsAbroad
}

const getGoodsNames = (goods) => {
  const goodNames = _.map(goods, item => item.name)
  return _.join(goodNames, '，');
}

const getAirportNames = (order) => {
  const departureAirportName = _.get(order, 'referenceOrder.shippingInfo.departure.airportName', '')
  const arrivalAirportName = _.get(order, 'referenceOrder.shippingInfo.arrival.airportName', '')
  return `${departureAirportName} - ${arrivalAirportName}`
}

const renderAcgAdInfo = ({ id, goods }) => ({
  title: '您的订单还可以选用智能报关服务',
  tip: getGoodsNames(goods),
  link: '去下单',
  location: {
    pathname: '/mwp/create_order',
    query: { acgOrderId: id }
  },
  imgSrc: adWisePortImage
})

const renderMwpAdInfo = ({ id, goods }) => ({
  title: '您的货品可以使用航空货运',
  tip: getGoodsNames(goods),
  link: '去下单',
  location: {
    pathname: '/acg/create',
    query: { mwpOrderId: id }
  },
  imgSrc: adAirCargoImage
})

const renderAcgOrderInfo = (order) => ({
  title: '本批货物相关口岸报关信息',
  tip: '申报口岸：' + _.get(order, 'referenceOrder.portName', ''),
  link: '查看详情',
  location: {
    pathname: `/mwp/orders/${getReferenceOrderId(order)}`
  },
  imgSrc: adWisePortImage
})

const renderMwpOrderInfo = (order) => ({
  title: '本批货物相关航空货运信息',
  tip: getAirportNames(order),
  link: '查看详情',
  location: {
    pathname: `/acg/orders/${getReferenceOrderId(order)}`
  },
  imgSrc: adAirCargoImage
})

const AdInfo = ({
  acg: {
    validation: order => orderIsSubmitted(order) && isAbroad(order) && !hasRelatedOrder(order),
    render: renderAcgAdInfo
  },
  mwp: {
    validation: order => !hasRelatedOrder(order),
    render: renderMwpAdInfo
  }
})

const OrderInfo = ({
  acg: {
    validation: hasRelatedOrder,
    render: renderAcgOrderInfo
  },
  mwp: {
    validation: hasRelatedOrder,
    render: renderMwpOrderInfo
  }
})

const renderContent = (typeInfo, order) => (
  !_.isEmpty(typeInfo) && typeInfo.validation(order)
    ? <ReferenceOrderContent data={typeInfo.render(order)} />
    : null)

export const ReferenceOrderAd = ({ order, type }) => renderContent(AdInfo[type], order)

export const ReferenceOrderBanner = ({ order, type }) => renderContent(OrderInfo[type], order)
