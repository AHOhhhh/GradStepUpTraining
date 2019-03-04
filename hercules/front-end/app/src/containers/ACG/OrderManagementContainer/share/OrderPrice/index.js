import React, {Component} from 'react'
import {isNull, includes} from 'lodash'
import {browserHistory, Link} from 'react-router'
import cssModules from 'react-css-modules'
import {connect} from 'react-redux'
import {getAirCargoOrder, getOfflinePaymentAuditOpinion} from 'actions'
import styles from './index.module.scss'
import OrderPriceDetail from './OrderPriceDetail'
import OfflinePaymentSection from '../../../../CheckoutCounter/OfflinePaymentSection'
import {ORDER_STATUS, INCLUDE_ORDER_PRICE_STATUS} from '../../constants/orderStatus'
import orderFinishedSVG from '../../../../shared/assets/order-finished.svg'

class OrderPrice extends Component { // eslint-disable-line
  componentDidMount() {
    if (includes(['OfflinePaidAwaitingConfirm', 'WaitForPay'], this.props.order.status)) {
      this.props.getOfflinePaymentAuditOpinion(this.props.order.id)
    }
  }

  renderOrderFinishSection() {
    return (
      <div styleName="order-price">
        <div styleName="status">
          <div styleName="title">
            <img className="icon" src={orderFinishedSVG}/>
            <span>订单服务完成</span>
          </div>
        </div>
      </div>
    )
  }

  handlePayment() {
    browserHistory.push(`/acg/orders/${this.props.order.id}/checkout_counter`)
  }

  isOfflinePayment() {
    const {status} = this.props.order
    const {offlinePaymentAuditOpinion} = this.props
    return (status === ORDER_STATUS.offlinePaidAwaitingConfirm) || (!isNull(offlinePaymentAuditOpinion.comment) && offlinePaymentAuditOpinion.payMethod === 'OFFLINE')
  }

  renderOrderPriceSection() {
    const {status, price, currency} = this.props.order

    if (status === ORDER_STATUS.closed) {
      return this.renderOrderFinishSection()
    }

    return includes(INCLUDE_ORDER_PRICE_STATUS, status) && (
      <div styleName="order-price">
        <div>
          <OrderPriceDetail
            price={price}
            currency={currency}
          />
          {this.renderPaymentSection()}
        </div>
      </div>
      )
  }

  renderPaymentSection() {
    const {id, status} = this.props.order
    if (status === ORDER_STATUS.orderTracking || this.props.preview) {
      return null
    }

    if (this.isOfflinePayment()) {
      return (
        <OfflinePaymentSection
          orderId={id}
          orderStatus={status}
          rePayForOrder={::this.handlePayment}
        />
      )
    }

    return this.renderWaitForPayStatusPaymentButton()
  }

  renderWaitForPayStatusPaymentButton() {
    return (
      <div styleName="payment-btn">
        <Link to={`/acg/orders/${this.props.order.id}/checkout_counter`}>
          <button className="button primary" styleName="button">立即支付</button>
          <div styleName="tips">
            <span styleName="tips-icon"/>
            <span styleName="tips-info">此为最终核准报价，如有疑问请拨打客服电话<span>400-890-0505</span></span>
          </div>
        </Link>
      </div>
    )
  }

  render() {
    if (this.props.order.status === ORDER_STATUS.waitForSpace) {
      return null
    }

    return (
      <div>
        {this.renderOrderPriceSection()}
      </div>
    )
  }
}

const mapStateToProps = state => ({
  order: state.acg.order,
  offlinePaymentAuditOpinion: state.checkoutCounter.offlinePaymentAuditOpinion
})

const mapDispatchToProps = {
  getAirCargoOrder,
  getOfflinePaymentAuditOpinion
}

export default connect(mapStateToProps, mapDispatchToProps)(cssModules(OrderPrice, styles, {allowMultiple: true}))
