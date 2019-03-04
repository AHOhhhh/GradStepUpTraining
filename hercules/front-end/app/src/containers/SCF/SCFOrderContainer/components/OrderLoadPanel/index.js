import React from 'react'
import cssModules from 'react-css-modules'
import {Icon} from 'antd'
import {get} from 'lodash'

import RHSActionPanel from 'components/RHSActionPanel'
import styles from './index.module.scss'

const OrderLoadPanel = ({preview, order}) => {
  const applyLoanUrl = get(order, 'scfFinancierOffer.applyLoanUrl', null)
  return (
    <RHSActionPanel>
      <div styleName="hints hints-success">
        <Icon type="check-circle"/>
        <p>订单服务完成</p>
      </div>
      {!preview && applyLoanUrl &&
      <div styleName="link"><a href={applyLoanUrl} target="_blank" rel="noopener noreferrer">申请贷款</a></div>}
    </RHSActionPanel>
  )
}

export default cssModules(OrderLoadPanel, styles, {allowMultiple: true})
