import React, {Component, PropTypes} from 'react' // eslint-disable-line
import cssModule from 'react-css-modules'
import {browserHistory} from 'react-router'
import styles from './index.module.scss'
import paymentFailureImage from './assets/payment_failure.png'

class WisePortPaymentFailureContainer extends Component {
  static propTypes = {
    orderStatus: PropTypes.string
  }

  returnOrder() {
    browserHistory.push(`/${this.props.params.orderType}/orders/${this.props.params.orderId}`)
  }

  render() {
    return (
      <div className="wise-port-container">
        <div styleName="payment-failure">
          <img src={paymentFailureImage} width="245px" height="239px" styleName="image"/>
          <h2 styleName="message">抱歉，您的支付失败，请重新支付</h2>
          <button className="button primary" styleName="return-order-button" onClick={::this.returnOrder}>返回订单</button>
        </div>
      </div>
    )
  }
}

export default cssModule(WisePortPaymentFailureContainer, styles)