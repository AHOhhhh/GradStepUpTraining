import React, {Component, PropTypes} from 'react'
import Modal from 'antd/lib/modal';
import currencyFormatter from 'currency-formatter'
import cssModule from 'react-css-modules'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import {result} from 'lodash'
import * as actions from 'actions'
import CancelOrderButton from 'components/CancelOrderButton'
import styles from './index.module.scss'
import WisePortTimeLine from '../../share/components/WisePortTimeLine'
import * as constants from '../../share/constants'
import OrderDetailPanel from '../../share/components/OrderDetailPanel'
import OrderPriceInfo from '../../share/components/OrderPriceInfo'
import PaymentSection from './PaymentSection'
import {renderReferenceAdBanner, renderReferenceOrderBanner} from '../../share/components/AdContainer'
import OrderDetailStatus from '../../../shared/OrderDetailStatus'
import AuditPaymentButton from '../../../shared/AuditPaymentButton'
import OfflinePaymentSection from '../../../CheckoutCounter/OfflinePaymentSection'
import FundInfo from '../../../shared/FundInfo'

class PaymentStep extends Component {

  static propTypes = {
    order: PropTypes.object,
    paymentStatus: PropTypes.string,
    orderPriceDetail: PropTypes.object
  }

  componentDidMount() {
    this.props.actions.getOrderPriceDetail(this.props.order.id)
    this.props.actions.getOfflinePaymentAuditOpinion(this.props.order.id)
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.paymentStatus === constants.PAYMENT_STATUS.PRICE_CHANGE && this.modal === undefined) {
      this.renderPriceChangTipsModal()
    }
  }

  refreshOrder() {
    this.props.actions.getOrderPriceDetail(this.props.order.id);
  }

  closePriceChangTipsModal() {
    this.modal.destroy()
    this.modal = undefined
    this.props.actions.setPaymentStatus(constants.PAYMENT_STATUS.NOT_START_PAYMENT)
  }

  renderPriceChangTipsModal() {
    this.modal = Modal.warning({
      title: '',
      okText: '确认',
      content: constants.REFRESH_TIPS,
      onOk: ::this.closePriceChangTipsModal
    });
  }

  handlePayment() {
    this.props.actions.payForOrder(this.props.order.id, this.props.orderPriceDetail);
  }

  renderReferenceAd(order, preview) {
    if (preview) {
      return null
    }
    return renderReferenceAdBanner(this.props.order)
  }

  renderReferenceOrder(order, preview) {
    if (preview) {
      return null
    }
    return renderReferenceOrderBanner(order)
  }

  isShowOfflinePaymentSection() {
    const status = this.props.order.status
    if (status === constants.PAYMENT_ORDER_STATUS.OFFLINE_PAID_AWAITING_CONFIRM) {
      return true
    }

    return status === constants.PAYMENT_ORDER_STATUS.WAIT_FOR_PAY && result(this.props.offlinePaymentAuditOpinion, 'payMethod') === constants.PAYMENT_METHOD.OFFLINE
  }


  renderPaymentSection(orderPriceDetail) {
    const {status} = this.props.order

    return (
      <div>
        {this.isShowOfflinePaymentSection() ?
          <OfflinePaymentSection
            orderId={this.props.order.id}
            orderStatus={status}
            rePayForOrder={::this.handlePayment}
          />
          :
          <PaymentSection
            orderPriceDetail={orderPriceDetail}
            handlePayment={::this.handlePayment}
            refreshOrder={::this.refreshOrder}
          />}
      </div>
    )
  }


  renderOrderDetailStatus(preview, order, data, current) {
    if (preview) {
      return <OrderDetailStatus order={order} data={data} current={current}/>
    }
    return null
  }

  renderCancelButton(order) {
    return (
      CancelOrderButton && <CancelOrderButton order={order}/>
    )
  }

  handleAfterSubmit() {
    const {order} = this.props
    this.props.actions.getOrderDetail(order.id)
  }

  render() {
    const {order, orderPriceDetail, preview, data, current} = this.props
    return (
      <div styleName="content">
        <div styleName="main-section">
          {this.renderReferenceAd(order, preview)}
          <OrderPriceInfo
            orderPriceDetail={orderPriceDetail}
            preview={preview}
            refreshOrder={::this.refreshOrder}
          />
          <FundInfo order={order}/>
          <WisePortTimeLine operationLogs={result(order, 'operationLogs', [])}/>
          {this.renderReferenceOrder(order, preview)}
        </div>

        <div styleName="right-side-bar">
          <AuditPaymentButton order={order} afterSubmit={::this.handleAfterSubmit}/>

          {!preview && <div styleName="payment">
            <h2>订单总额：<span>{currencyFormatter.format(orderPriceDetail.totalPrice, {code: 'CNY'})}</span></h2>
            {::this.renderPaymentSection(preview, orderPriceDetail)}
          </div>}

          {this.renderOrderDetailStatus(preview, order, data, current)}
          <OrderDetailPanel
            detail={this.props.order}
            preview={preview}
          />
          {!preview && this.renderCancelButton(order)}
        </div>

      </div>
    );
  }
}

export default connect(state => ({
  order: state.wisePortOrder.order,
  orderPriceDetail: state.wisePortOrder.payment.orderPriceDetail,
  paymentStatus: state.wisePortOrder.payment.paymentStatus,
  offlinePaymentAuditOpinion: state.checkoutCounter.offlinePaymentAuditOpinion
}), dispatch => ({
  actions: bindActionCreators(actions, dispatch)
}))(cssModule(PaymentStep, styles))

