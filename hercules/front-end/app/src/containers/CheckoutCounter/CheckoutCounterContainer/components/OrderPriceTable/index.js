import React from 'react'
import cssModules from 'react-css-modules'
import {Table} from 'antd';
import currencyFormatter from 'currency-formatter'
import styles from './index.module.scss'
import * as constants from './constants'

const getUserOrderList = (orderPrice) => {
  return [Object.assign({}, {orderId: orderPrice.orderId}, {totalPrice: currencyFormatter.format(orderPrice.amount, {code: 'CNY'})})]
}

const OrderPriceTable = ({orderPrice}) => {
  return (
    <div styleName="order-info">
      <h2 styleName="price-title">订单信息</h2>
      <div styleName="price-table">
        <Table
          columns={constants.ORDER_LIST_COLUMNS}
          dataSource={getUserOrderList(orderPrice)}
          pagination={false}
        />
      </div>
    </div>
  )
}

export default cssModules(OrderPriceTable, styles, {allowMultiple: true})