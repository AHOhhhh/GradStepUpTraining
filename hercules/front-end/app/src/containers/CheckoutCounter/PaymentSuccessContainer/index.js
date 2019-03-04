import React, {Component, PropTypes} from 'react' // eslint-disable-line
import cssModules from 'react-css-modules'
import {browserHistory} from 'react-router'
import Icon from 'antd/lib/icon'
import Button from 'antd/lib/button'
import styles from './index.module.scss'

class WisePortPaymentSuccessContainer extends Component { // eslint-disable-line

  static propTypes = {
    orderStatus: PropTypes.string
  };

  returnOrder() {
    browserHistory.push(`/${this.props.params.orderType}/orders/${this.props.params.orderId}`)
  }

  render() {
    return (
      <div styleName="success-container">
        <div styleName="success-icon">
          <Icon type="check-circle"/>
        </div>
        <div styleName="payment-text">
          <span>您的支付成功</span>
        </div>
        <div styleName="back-button">
          <Button onClick={::this.returnOrder}>返回订单</Button>
        </div>
      </div>
    )
  }
}

export default cssModules(WisePortPaymentSuccessContainer, styles)