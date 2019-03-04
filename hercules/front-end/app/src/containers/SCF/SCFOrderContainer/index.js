import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import {get, includes, isEmpty} from 'lodash';
import {connect} from 'react-redux'
import {BreadcrumbV2} from 'components'
import WisePortOrderSteppers from 'components/Steppers/WisePortOrderSteppers'
import {getSCFOrderById, goNextForAcceptedOrder} from './actions'
import {ORDER_STATUS, STEPS_CONSTANT} from '../constants/steps'
import FinancingStep from './FinancingStep'
import UpdateProfileStep from './UpdateProfileStep'
import styles from './index.module.scss'
import ApproveStep from './ApproveStep';
import OrderDetailPanel from './components/OrderDetailPanel'
import OrderDetailStatus from '../../shared/OrderDetailStatus'
import WisePortTimeLine from '../../MWP/share/components/WisePortTimeLine'
import OrderAction from './components/OrderAction'
import OrderLoadPanel from './components/OrderLoadPanel'

const statusToComponentMap = [
  {
    status: [ORDER_STATUS.submitted, ORDER_STATUS.orderAccepted, ORDER_STATUS.orderRejected],
    stepIndex: 2,
    component: ApproveStep
  },
  {
    status: [ORDER_STATUS.waitForUploadSupplementaryFiles, ORDER_STATUS.orderRejected],
    stepIndex: 3,
    component: UpdateProfileStep
  },
  {
    status: [ORDER_STATUS.waitForFinancier, ORDER_STATUS.waitForFinancierOffer],
    stepIndex: 4,
    component: FinancingStep
  },
  {
    status: [ORDER_STATUS.closed],
    stepIndex: 5,
    component: FinancingStep
  }
]

const getBreadCrumb = orderId => ({
  paths: [{
    item: '个人中心',
    url: '/'
  }, {
    item: '我的订单',
    url: '/orders'
  }],
  currentPage: `订单: ${orderId}`
})

export class SCFOrderContainer extends Component {
  componentDidMount() {
    const {getSCFOrderById, params: {orderId}} = this.props
    getSCFOrderById(orderId)
  }

  getComponentItem() {
    const status = get(this.props, 'order.status', null)
    const supplementaryFiles = get(this.props, 'order.supplementaryFiles', null)
    const componentItem = status && statusToComponentMap.find((item) => {
      const statusArr = [].concat(item.status)
      let result = includes(statusArr, status)
      if (result && status === ORDER_STATUS.orderRejected && supplementaryFiles && item.stepIndex === 2) {
        result = false
      }
      return result
    })

    return componentItem || null
  }

  renderNextBtn = () => {
    const {goNextForAcceptedOrder, params: {orderId}} = this.props
    const status = get(this.props, 'order.status', null)
    return status && status === ORDER_STATUS.orderAccepted
      ? (<div styleName="next-container">
        <button
          onClick={() => {
            goNextForAcceptedOrder(orderId)
          }}>下一步
        </button>
      </div>)
      : null
  }

  renderClosedOrderPanel = () => {
    const {preview, order} = this.props
    return order.status && order.status === ORDER_STATUS.closed
      ? <OrderLoadPanel preview={preview} order={order}/>
      : null
  }

  render() {
    const {order, preview, params: {orderId}, user} = this.props
    const componentItem = this.getComponentItem()
    if (!isEmpty(componentItem) && componentItem.stepIndex) {
      const StepComponent = componentItem.component
      const stepIndex = componentItem.stepIndex
      const operationLogs = get(order, 'operationLogs', [])

      return (
        <div styleName="order-container">
          {!preview && <BreadcrumbV2 breadcrumb={getBreadCrumb(orderId)}/>}
          <div styleName="stepper-container">
            <WisePortOrderSteppers data={STEPS_CONSTANT} current={stepIndex}/>
          </div>
          <div styleName="content">
            <div styleName="left">
              <StepComponent order={order} preview={preview} data={STEPS_CONSTANT} current={stepIndex}/>
              <WisePortTimeLine operationLogs={operationLogs}/>
              {this.renderNextBtn()}
            </div>
            <div styleName="right">
              {this.renderClosedOrderPanel()}
              {preview && <OrderDetailStatus order={order} data={STEPS_CONSTANT} current={stepIndex}/>}
              <OrderDetailPanel preview={preview}/>
              <OrderAction order={order} user={user}/>
            </div>
          </div>
        </div>
      )
    }
    return null
  }
}

export default connect(state => ({order: state.scf.order, user: state.auth.user}), {
  getSCFOrderById,
  goNextForAcceptedOrder
})(
  cssModules(SCFOrderContainer, styles, {allowMultiple: true}))
