import React from 'react'
import {Row, Col} from 'antd'
import currencyFormatter from 'currency-formatter'
import cssModules from 'react-css-modules'
import styles from './index.module.scss'

const formatMoney = (money, currency) => {
  return currencyFormatter.format(money, {code: currency})
}

const OrderPriceDetail = ({price, currency}) => {
  const {airlineFee, pickUpFee, dropOffFee, total} = price
  return (
    <div>
      <Row styleName="item">
        <Col span={8} styleName="title">上门取货：</Col>
        <Col span={16} styleName="value">{formatMoney(pickUpFee, currency)}</Col>
      </Row>
      <Row styleName="item">
        <Col span={8} styleName="title">航空货运：</Col>
        <Col span={16} styleName="value">{formatMoney(airlineFee, currency)}</Col>
      </Row>
      <Row styleName="item">
        <Col span={8} styleName="title">机场派送：</Col>
        <Col span={16} styleName="value">{formatMoney(dropOffFee, currency)}</Col>
      </Row>
      <Row styleName="item summary">
        <Col span={9} styleName="title">订单总额：</Col>
        <Col span={15} styleName="value">{formatMoney(total, currency)}</Col>
      </Row>
      <Row/>
    </div>
  )
}

export default cssModules(OrderPriceDetail, styles, {allowMultiple: true})