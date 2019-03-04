import React, {Component, PropTypes} from 'react';
import {browserHistory} from 'react-router'

import {Section} from 'components'
import waitingIcon from './images/waiting.svg';
import successIcon from './images/success.svg';
import {ORDER_TYPE} from '../share/constants/order-config'

class PaymentResult extends Component { // eslint-disable-line react/prefer-stateless-function
  static propTypes = {
    result: PropTypes.object.isRequired
  }

  redirectPage(e) {
    const text = e.target.innerHTML
    if (text === '续费') {
      browserHistory.push('/wms/create?type=' + ORDER_TYPE.renew)
    } else if (text === '充值') {
      browserHistory.push('/wms/create?type=' + ORDER_TYPE.recharge)
    }
  }

  renderPayButton(preview) {
    if (preview) {
      return null
    }
    return (
      <div>
        <div className="row statement">
          <div className="label"/>
          <div className="text">
            {this.props.result.orderStatusStatement}如有其他疑问，请拨打服务商电话<span className="phone-number">400-890-0505</span>，与客服人员取得联系。
          </div>
        </div>
        <div className={this.props.result.buttonsVisible ? 'row statement' : 'hide'}>
          <div className="label"/>
          <div className="text">
            <button type="button" className="button renewal" onClick={::this.redirectPage}>续费</button>
            <button type="button" className="button" onClick={::this.redirectPage}>充值</button>
          </div>
        </div>
      </div>
    )
  }

  render() {
    const order = this.props.order
    const orderFinished = order.status === 'Closed'
    const preview = this.props.preview
    return (
      <Section>
        <div className="order-code">
          <div className="row">
            <div className="label">订单号：</div>
            <div className="text">{order.id}</div>
          </div>
        </div>
        <div className="content">
          <div className="order-status status-content">
            <div className="row">
              <div className="label">订单状态：</div>
              <img className="result-icon" src={orderFinished ? successIcon : waitingIcon}/>
              <div className={'text ' + (orderFinished ? 'success-message' : 'waiting-message')}>
                {this.props.result.orderStatusMessage}
              </div>
            </div>
            {preview && <div className="row">
              <div className="label">退款状态：</div>
              <div className="refund-status">{order.refundStatus ? '已退款' : '--'}</div>
            </div>}
            {::this.renderPayButton(preview)}
          </div>
        </div>
      </Section>
    )
  }
}

export default PaymentResult