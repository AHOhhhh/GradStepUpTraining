/* eslint-disable react/self-closing-comp */
import React, {Component} from 'react'
import {includes, result} from 'lodash'
import {connect} from 'react-redux'
import cssModule from 'react-css-modules'
import {BreadcrumbV2} from 'components'
import WisePortOrderSteppers from 'components/Steppers/WisePortOrderSteppers'
import {getOrderDetail} from 'actions'
import {formatDateTime} from 'utils/format'
import styles from './index.module.scss'
import OfferSelectionPendingStep from './OfferSelectionPendingStep'
import OfferSelectionStep from './OfferSelectionStep'
import InServiceStep from './InServiceStep'
import PaymentStep from './PaymentStep'
import FundInfo from '../../shared/FundInfo'
import PaymentFinishStep from './PaymentFinishStep'
import OrderDetailPanel from '../share/components/OrderDetailPanel'
import WisePortTimeLine from '../share/components/WisePortTimeLine'
import OrderDetailStatus from '../../shared/OrderDetailStatus'

import WISEPORT_STEPS_CONSTANT from '../share/constants/wisePortStepsConstant';

const statusToComponentMap = [
  {
    status: ['Submitted'],
    stepIndex: 2,
    component: OfferSelectionPendingStep
  },
  {
    status: ['TenderOffer'],
    stepIndex: 2,
    component: OfferSelectionStep
  },
  {
    status: ['InProgress'],
    stepIndex: 3,
    component: InServiceStep
  },
  {
    status: ['WaitForPay', 'OfflinePaidAwaitingConfirm', 'OfflinePaidNotConfirmed'],
    stepIndex: 4,
    component: PaymentStep
  },
  {
    status: ['Paid', 'Closed'],
    stepIndex: 5,
    component: PaymentFinishStep
  }
]

class WisePortOrderContainer extends Component {
  componentDidMount() {
    const {params: {orderId}, getOrderDetail} = this.props
    getOrderDetail(orderId);
  }

  getComponentItem() {
    const {status} = this.props.order
    if (status === 'TenderOffer' && this.props.preview) {
      return statusToComponentMap[0]
    }
    return statusToComponentMap.find((item) => {
      const statusArr = [].concat(item.status)
      return includes(statusArr, status)
    })
  }

  getBreadCrumb() {
    const {orderId} = this.props.params
    return {
      paths: [{
        item: '个人中心',
        url: '/'
      }, {
        item: '我的订单',
        url: '/orders'
      }],
      currentPage: `订单: ${orderId}`
    }
  }


  renderBreadCrumb() {
    if (this.props.preview) return null
    return (
      <BreadcrumbV2 breadcrumb={::this.getBreadCrumb()}/>
    )
  }

  render() {
    const {order, preview} = this.props
    const {status, updatedAt} = order
    if (status === 'Cancelled') {
      return (
        <div styleName="wise-port-container">
          {::this.renderBreadCrumb()}
          <div styleName="main-section">
            <div styleName="cancel-container">
              <div styleName="message">
                <h2>您已取消该笔关务服务订单</h2>
                <div styleName="date">
                  <p><span>取消时间：</span>{formatDateTime(updatedAt)}</p>
                </div>
              </div>
            </div>
            {preview && <FundInfo order={this.props.order}/>}
            <WisePortTimeLine operationLogs={result(order, 'operationLogs', [])}/>
          </div>
          <div styleName="right-side-bar">
            {preview && (<OrderDetailStatus order={this.props.order} data={WISEPORT_STEPS_CONSTANT}/>)}
            <OrderDetailPanel
              detail={order}
              preview={preview}
            />
            {!preview && (<div styleName="re-order">
              <p>若您还需要关务服务，您可以</p>
              <a href="/mwp/create_order">重新下单</a>
            </div>)}
          </div>
        </div>
      )
    }
    const ComponentItem = this.getComponentItem()
    if (ComponentItem) {
      const currentStepIndex = ComponentItem.stepIndex
      const StepComponent = ComponentItem.component
      return (
        <div styleName="wise-port-container">
          {::this.renderBreadCrumb()}
          <div styleName="stepper-container">
            <WisePortOrderSteppers data={WISEPORT_STEPS_CONSTANT} current={currentStepIndex}/>
          </div>
          <StepComponent
            order={this.props.order} preview={this.props.preview} data={WISEPORT_STEPS_CONSTANT}
            current={currentStepIndex}/>
        </div>
      )
    }
    return null
  }
}

const wrappedContainer = connect(
  state => ({order: state.wisePortOrder.order}),
  {getOrderDetail}
)(cssModule(WisePortOrderContainer, styles, {allowMultiple: true}))

export default wrappedContainer
