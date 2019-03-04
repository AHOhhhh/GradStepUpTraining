import React, {Component, PropTypes} from 'react' // eslint-disable-line
import {connect} from 'react-redux'
import cssModule from 'react-css-modules'
import {browserHistory} from 'react-router'
import {userIsAuthenticated} from 'utils'
import {bindActionCreators} from 'redux'
import * as actions from 'actions'
import styles from './index.module.scss'
import paymentFailureImage from './assets/payment_failure.png'

class WisePortPaymentFailureContainer extends Component {
  static propTypes = {
    orderStatus: PropTypes.string
  }

  handleClick(e) {
    browserHistory.push(this.props.previousUrl)
    if (e.target.innerHTML === '重新支付') {
      this.props.repayOrder(this.props.orderId)
    }
  }

  render() {
    return (
      <div className="wise-port-container">
        <div styleName="payment-failure">
          <img src={paymentFailureImage} width="245px" height="239px" styleName="image"/>
          <h2 styleName="message">抱歉，您的支付失败，请重新支付</h2>
          <div styleName="operation">
            <button className="button primary" styleName="return-order-button" onClick={::this.handleClick}>返回订单
            </button>
            <button className="button primary" onClick={::this.handleClick}>重新支付</button>
          </div>
        </div>
      </div>
    )
  }
}

export default userIsAuthenticated(connect(
  state => (state),
  dispatch => ({
    actions: bindActionCreators(actions, dispatch)
  })
)(cssModule(WisePortPaymentFailureContainer, styles, {allowMultiple: true})))
