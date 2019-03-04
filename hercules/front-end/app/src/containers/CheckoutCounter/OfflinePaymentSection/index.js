import React, {Component} from 'react'
import Icon from 'antd/lib/icon';
import {connect} from 'react-redux'
import {result, isNull} from 'lodash'
import cssModule from 'react-css-modules'
import styles from './index.module.scss'
import * as maps from './maps'
import * as constants from './constants'

class OfflinePaymentSection extends Component {
  isOfflinePaymentAuditFailure() {
    const isStatusWaitForPay = this.props.orderStatus === constants.ORDER_STATUS.WAIT_FOR_PAY
    const offlinePaymentAuditOpinion = this.props.offlinePaymentAuditOpinion
    const isOfflinePayment = offlinePaymentAuditOpinion.payMethod === constants.PAYMENT_METHOD.OFFLINE

    return isStatusWaitForPay && isOfflinePayment && !isNull(isOfflinePayment.comment)
  }

  render() {
    const disable = this.props.orderStatus === constants.ORDER_STATUS.OFFLINE_PAID_AWAITING_CONFIRM
    return (
      <div>
        <div styleName="payment-button">
          <button
            className={(disable ? 'payment-waiting-button ' : ' ') + 'button primary'}
            disabled={disable}
            onClick={this.props.rePayForOrder}>{maps.paymentMessageMap[this.props.orderStatus]}
          </button>
        </div>

        {this.isOfflinePaymentAuditFailure() && <div styleName="payment-tip">
          <div styleName="icon"><Icon type="info-circle-o"/></div>
          <div styleName="tip-message">线下支付失败！
            <div styleName="opinion">审核意见：{result(this.props.offlinePaymentAuditOpinion, 'comment', '')}</div>
          </div>
        </div>}
      </div>
    );
  }
}

export default connect(state => ( {
  offlinePaymentAuditOpinion: state.checkoutCounter.offlinePaymentAuditOpinion
}))(cssModule(OfflinePaymentSection, styles, {allowMultiple: true}))

