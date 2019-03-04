import React from 'react'
import cssModule from 'react-css-modules'
import {Button} from 'antd'
import currencyFormatter from 'currency-formatter'

import styles from './index.module.scss'


const OrderSubmission = ({price, onSubmit}) => {
  const buttonClassName = price ? '' : 'submit-button_disable'

  const currency = currencyFormatter.format(price, { code: 'CNY' })

  return (
    <div styleName="order-submission">
      <div styleName="total">
        <span styleName="text">订单总额:</span>
        <span styleName="price">{currency}</span>
      </div>
      <Button className={buttonClassName} styleName="submit-button" onClick={onSubmit}>选定服务商</Button>
    </div>
  )
}

export default cssModule(OrderSubmission, styles)