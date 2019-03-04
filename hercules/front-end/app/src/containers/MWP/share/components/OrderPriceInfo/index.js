import React from 'react'
import cssModule from 'react-css-modules'
import {Table} from 'antd'
import currencyFormatter from 'currency-formatter'

import * as constants from '../../constants'
import styles from './index.module.scss'

function getOrderDetailList(offers) {
  const serviceList = [];
  offers.forEach((offer, i) => {
    const {offerId, companyId, companyName} = offer
    offer.paymentDetails.forEach((service, index) => {
      const amount = currencyFormatter.format(service.amount, {code: 'CNY'})
      serviceList.push(Object.assign({}, service, {offerId, companyId, companyName, amount, key: i + '_' + index}))
    })
  });
  return serviceList;
}

function renderRefreshPrice(preview, refreshOrder) {
  if (preview) {
    return (
      <div styleName="refresh-price-text">
        <span styleName="refresh-price">( <span styleName="refresh" onClick={refreshOrder}>刷新</span>获取最终客户支付价格 )</span>
      </div>
    )
  }
  return null
}

const OrderPriceInfo = ({orderPriceDetail, preview, refreshOrder}) => {
  const totalPrice = currencyFormatter.format(orderPriceDetail.totalPrice, {code: 'CNY'})

  return (
    <div styleName="order-info">
      <h2 styleName="price-title">订单价格详情</h2>
      <div styleName="price-table">
        <Table
          columns={constants.COLUMNS}
          dataSource={getOrderDetailList(orderPriceDetail.offers)}
          pagination={false}
        />
      </div>
      <div styleName="price-total">
        <span styleName="text">总价 : </span>
        <span styleName="total">{totalPrice}</span>
        {renderRefreshPrice(preview, refreshOrder)}
      </div>

    </div>
  )
}

export default cssModule(OrderPriceInfo, styles)