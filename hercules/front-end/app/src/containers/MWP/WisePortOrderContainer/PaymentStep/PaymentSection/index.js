import React from 'react'
import Icon from 'antd/lib/icon';
import cssModule from 'react-css-modules'
import styles from './index.module.scss'

const PaymentSection = (props) => {
  return (
    <div >
      <div styleName="payment-button">
        <button className="button primary" onClick={props.handlePayment}>去支付</button>
      </div>

      <div styleName="payment-tip">
        <div styleName="icon"><Icon type="info-circle-o"/></div>
        <div styleName="tip-message">订单价格可能随时在变动，请点击<a onClick={props.refreshOrder}>刷新</a>页面，以取得最终支付价格。
        </div>
      </div>
    </div>
  );
}

export default cssModule(PaymentSection, styles)

