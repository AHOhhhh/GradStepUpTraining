import React from 'react'
import cssModules from 'react-css-modules'
import {map, get} from 'lodash'
import moment from 'moment'
import {Link} from 'react-router'
import {Row, Col} from 'antd'
import {orderStatusMap} from 'constants/order'

import styles from './index.module.scss'

const MAP_ORDER_DETAIL = {
  status: '订单状态',
  refundStatus: '退款状态',
  orderName: '产品名称',
  enterpriseName: '客户名称',
  createAt: '提交时间',
  updatedAt: '更新时间'
}

const MAP_ORDER_TYPE = {
  scf: '供应链金融',
  mwp: '口岸报关',
  acg: '航空货运',
  wms: 'WMS'
}

const renderOrderDetail = (order, current) => {
  return map(MAP_ORDER_DETAIL, (text, label) => {
    return (<Row styleName="detail" key={label}>
      <Col span={8} styleName="label">{text}：</Col>
      {label === 'enterpriseName' ?
        <Col span={16} styleName={label}><Link
          to={`/admin/preview_enterprise_info/${order.enterpriseId}`}>{order[label] || ''}</Link></Col> :
        <Col span={16} styleName={current === 5 ? '' : label}>{order[label] || ''}</Col>
      }
    </Row>)
  })
}

const formatOrder = (order, status) => {
  const {orderType, enterpriseName, createdAt, updatedAt, enterpriseId, refundStatus} = order

  return {
    enterpriseId,
    status,
    refundStatus: refundStatus ? '已退款' : '--',
    orderName: MAP_ORDER_TYPE[orderType],
    enterpriseName,
    createAt: moment.unix(createdAt).format('YYYY-MM-DD HH:mm'),
    updatedAt: moment.unix(updatedAt).format('YYYY-MM-DD HH:mm')
  }
}

const getStatus = ({order}) => get(orderStatusMap, [order.orderType, order.status])

const OrderDetailStatus = ({order, current}) => {
  const status = getStatus({order})
  return (<div styleName="order-detail-status">
    <Row styleName="title">
      <Col span={8} styleName="order-title">订单号：</Col>
      <Col span={16} styleName="order-id">{order.id}</Col>
    </Row>
    <hr/>
    <div styleName="order-detail-content">
      {renderOrderDetail(formatOrder(order, status), current)}
    </div>
  </div>)
}

export default cssModules(OrderDetailStatus, styles)