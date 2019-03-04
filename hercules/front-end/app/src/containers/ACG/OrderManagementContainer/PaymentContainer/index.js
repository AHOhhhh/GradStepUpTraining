import React from 'react'
import cssModules from 'react-css-modules'
import {userIsAuthenticated} from 'utils'
import {Section} from 'components'
import {getAirCargoOrder} from 'actions'

import styles from './index.module.scss'

const refresh = (id) => {
  getAirCargoOrder(id)
}

const renderPaymentContainer = (preview, order) => {
  if (preview) {
    return (<div styleName="platform-admin-waiting-payment">
      <div styleName="title">运输安排</div>
      <p>服务商舱位确认完成，等待客户支付！<span onClick={() => refresh(order.id)}>刷新</span>查看更新状态</p>
    </div>)
  }

  return (
    <div styleName="waiting-payment">
      <Section>
        <h2 styleName="info">舱位已确认，请支付后查看运输安排！</h2>
      </Section>
    </div>)
}

const PaymentContainer = ({preview, order}) => (
    renderPaymentContainer(preview, order)
)

export default userIsAuthenticated(cssModules(PaymentContainer, styles, {allowMultiple: true}))