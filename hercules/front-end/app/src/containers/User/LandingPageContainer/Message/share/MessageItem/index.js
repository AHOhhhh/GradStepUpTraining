import React from 'react'
import cssModules from 'react-css-modules'
import {Row} from 'antd'
import {browserHistory} from 'react-router'
import {formatDateTime} from 'utils/format'
import styles from './index.module.scss'
import {orderStatusMap} from '../../../../../../constants/order'
import ServiceCard from '../ServiceCard/index'
import {serviceType, ORDER_TYPE_MAP} from './constants'

const NotificationItem = ({info, type}) => {
  const status = (info.serviceTypes[0] === 'Recharge' && info.status === 'Submitted') ? 'Audited' : info.status
  const messageMap = {
    notification: {
      key: '订单状态：',
      value: orderStatusMap[info.orderType][status] || '待支付',
      style: 'order-status',
      descriptionStyle: ''
    },
    logistics: {
      key: '订单号：',
      value: info.orderId || '0101301090',
      style: 'order-id',
      descriptionStyle: 'logistics-description'
    }
  }

  let description = info.description
  if (info.orderType === 'acg' && type === 'logistics') {
    description = info.description.split('\n').reverse()
    description = description.map((desc, i) => `${i + 1}.${desc}`).join('\t')
  }

  return (
    <div
      styleName="item-container"
      onClick={() => {
        browserHistory.push(`/${info.orderType}/orders/${info.orderId}`)
      }}>
      <Row>
        <div styleName="title">{info.name}</div>
        <div styleName="order-type">{ORDER_TYPE_MAP[info.orderType]}</div>
        <div styleName="order-location"><span>{info.orderInfo}</span></div>
        <div styleName={messageMap[type].style}>{`${messageMap[type].key} ${messageMap[type].value}`}</div>
      </Row>
      <Row styleName={'description ' + messageMap[type].descriptionStyle}>{description}</Row>
      <Row styleName="offer-types">
        {info.serviceTypes.map((service, index) => <ServiceCard key={index} {...serviceType[service]}/>)}
        <div styleName="date">{formatDateTime(info.createdAt)}</div>
      </Row>
    </div>
  )
}

export default cssModules(NotificationItem, styles, {allowMultiple: true})
