import React, {Component} from 'react'
import {Icon} from 'antd'
import cssModule from 'react-css-modules'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import {result} from 'lodash'
import * as actions from 'actions'

import FundInfo from '../../../shared/FundInfo'
import OrderDetailPanel from '../../share/components/OrderDetailPanel'
import WisePortTimeLine from '../../share/components/WisePortTimeLine'
import OrderDetailStatus from '../../../shared/OrderDetailStatus'
import styles from './index.module.scss'
import OrderPriceInfo from '../../share/components/OrderPriceInfo'
import {renderReferenceAdBanner, renderReferenceOrderBanner} from '../../share/components/AdContainer'

class PaymentFinishStep extends Component {

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

  renderPaymentFinish(preview) {
    if (preview) {
      return (
        <div styleName="finish-status-108">
          <Icon type="check-circle" styleName="icon"/>
          <span styleName="text">服务完成</span>
        </div>
      )
    }
    return (
      <div styleName="finish-status-217">
        <Icon type="check-circle" styleName="icon"/>
        <span styleName="text">服务完成</span>
      </div>
    )
  }

  renderOrderDetailStatus(preview, order, data, current) {
    if (preview) {
      return <OrderDetailStatus order={order} data={data} current={current}/>
    }
    return null
  }

  componentDidMount() {
    this.props.actions.getOrderPriceDetail(this.props.order.id)
  }

  render() {
    const {orderPriceDetail, order, preview, data, current} = this.props;
    return (
      <div styleName="payment-finish">
        <div styleName="main-container">
          {this.renderReferenceAd(order, preview)}
          <OrderPriceInfo orderPriceDetail={orderPriceDetail}/>
          <FundInfo order={order}/>
          <WisePortTimeLine operationLogs={result(order, 'operationLogs', [])}/>
          {this.renderReferenceOrder(order, preview)}
        </div>
        <div styleName="main-slide">
          <div styleName="finish-status">
            {this.renderPaymentFinish(preview)}
          </div>
          {this.renderOrderDetailStatus(preview, order, data, current)}
          <OrderDetailPanel detail={order} preview={preview}/>
        </div>
      </div>
    )
  }
}

export default connect(state => ({
  order: state.wisePortOrder.order,
  orderPriceDetail: state.wisePortOrder.payment.orderPriceDetail,
}), dispatch => ({
  actions: bindActionCreators(actions, dispatch)
}))(cssModule(PaymentFinishStep, styles))