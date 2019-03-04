import React from 'react'
import cssModules from 'react-css-modules'
import {map} from 'lodash'
import {connect} from 'react-redux'
import {Row, Col} from 'antd'
import {formatPrice, formatDate} from 'utils'

import styles from './index.module.scss'
import {LABELS_MAP} from './constants';

const renderSection = (label, text) => {
  return (
    <Row className={styles.detail} key={label}>
      <Col span={8} className={styles.label}>{label}：</Col>
      <Col span={16} className={styles.text}><span>{text}</span></Col>
    </Row>
  )
}

const renderSections = (order) => {
  return map(order, (value, key) => {
    return renderSection(LABELS_MAP[key], value)
  })
}

const formatOrderDetail = (order) => {
  const {
    counterPartyName,
    credit,
    createAt,
    dueDate,
    description,
    requireAmount,
    expectRate
  } = order

  return {
    counterPartyName,
    credit: formatPrice(credit),
    createAt: formatDate(createAt),
    dueDate: formatDate(dueDate),
    description,
    requireAmount: formatPrice(requireAmount),
    expectRate: `${expectRate.toString().split('%')[0]}%`
  }
}

const renderOrderId = (id, preview) => {

  if (preview) return null
  return (<div>
    <Row className={styles.title}>
      <Col span={8} className={styles.label}>订单号：</Col>
      <Col span={16} className={styles.text}>{id}</Col>
    </Row>
    <hr/>
  </div>)
}

const OrderDetailPanel = ({order, preview}) => {
  return (
    <div className={styles.container}>
      {renderOrderId(order.id, preview)}
      <div className={styles.section}>
        {renderSections(formatOrderDetail(order))}
      </div>
    </div>
  )
}

const mapStateToProps = (state) => {
  return {
    order: state.scf.order
  }
}

export default connect(mapStateToProps)(cssModules(OrderDetailPanel, styles, {allowMultiple: true}))