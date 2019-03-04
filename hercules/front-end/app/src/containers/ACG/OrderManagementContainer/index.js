import React, {Component, PropTypes} from 'react'
import {connect} from 'react-redux'
import {BreadcrumbV2, Record, Section} from 'components'
import CancelOrderButton from 'components/CancelOrderButton'
import WisePortOrderSteppers from 'components/Steppers/WisePortOrderSteppers'
import {formatDateTime} from 'utils/format'
import {getAirCargoOrder} from 'actions'
import FundInfo from '../../shared/FundInfo'
import AuditPaymentButton from '../../shared/AuditPaymentButton'
import styles from './index.module.scss'
import {ACG_STEPS, ORDER_STATUS} from './constants/orderStatus'
import {getStepper, getCurrentStepIndex} from './utils/orderStatusUtils'
import {ReferenceOrderAd, ReferenceOrderBanner} from '../../shared/ReferenceOrderContainer'
import OrderPrice from './share/OrderPrice'
import OrderDetailPanel from '../components/OrderDetailPanel'
import OrderDetailStatus from '../../shared/OrderDetailStatus'

@connect(
  state => ({order: state.acg.order}),
  {getAirCargoOrder}
)
class OrderManagementContainer extends Component {
  static propTypes = {
    getAirCargoOrder: PropTypes.func.isRequired,
    order: PropTypes.object
  }

  componentWillMount() {
    const {getAirCargoOrder, params: {orderId}} = this.props
    getAirCargoOrder(orderId)
  }

  renderOrderDetailStatus = () => {
    const {order, preview} = this.props
    if (preview) {
      return (<div>
        <OrderDetailStatus order={order} data={ACG_STEPS} current={getCurrentStepIndex(order.status, order.cancelReason)}/>
      </div>)
    }
  }

  renderCancelButton = () => CancelOrderButton && <CancelOrderButton order={this.props.order}/>

  handleAfterSubmit = () => {
    const {getAirCargoOrder, params: {orderId}} = this.props
    getAirCargoOrder(orderId)
  }

  renderSpecifiedStepper = () => {
    const {order, preview} = this.props
    const {id, status = '', cancelReason, updatedAt} = order
    const cancelDescribe = cancelReason === 'BookingFailure' ? '很抱歉，由于货品或运力方面原因，仓位预定失败！' : '您已取消该笔航空货运服务订单'
    if (ORDER_STATUS.cancelled === status) {
      return (
        <div className={styles.componentPanel}>
          <div className={styles.leftWrapper}>
            <div className={styles.cancelContainer}>
              <div className={styles.message}>
                <h2>{cancelDescribe}</h2>
                <div className={styles.date}>
                  <p><span>取消时间：</span>{formatDateTime(updatedAt)}</p>
                </div>
              </div>
            </div>
            <FundInfo order={order}/>
            <div className={styles.orderRecord}>
              <Section title="操作记录">
                <Record data={order.operationLogs}/>
              </Section>
            </div>
          </div>
          <div className={styles.rightWrapper}>
            {this.renderOrderDetailStatus()}
            <OrderDetailPanel detail={order} preview={preview}/>
            {!preview && (<div className={styles.reOrder}>
              <p>若您还需要货运服务，您可以</p>
              <a href="/acg/create">重新下单</a>
            </div>)}
          </div>
        </div>
      )
    }
    if (id) {
      const stepper = getStepper(status, cancelReason);
      return (
        <div className={styles.componentPanel}>
          <div className={styles.leftWrapper}>
            <ReferenceOrderAd order={order} type="acg"/>
            {stepper && <stepper.StepComponent order={order} preview={preview}/>}
            <FundInfo order={order}/>
            <div className={styles.orderRecord}>
              <Section title="操作记录">
                <Record data={order.operationLogs}/>
              </Section>
            </div>
            <ReferenceOrderBanner order={order} type="acg"/>
          </div>
          <div className={styles.rightWrapper}>
            <AuditPaymentButton order={order} afterSubmit={this.handleAfterSubmit}/>
            <OrderPrice order={order} preview={preview}/>
            <OrderDetailPanel detail={order} preview={preview}/>
            {this.renderOrderDetailStatus()}
            {!preview && this.renderCancelButton()}
          </div>
        </div>
      )
    }
  }

  breadcrumbInfo = () => {
    const {order: {id}} = this.props
    return {
      paths: [
        {
          item: '个人中心',
          url: '/'
        },
        {
          item: '我的订单',
          url: '/orders'
        }
      ],
      currentPage: `订单: ${id}`
    }
  }

  renderBreadcrumb = () => {
    const {order: {success}, preview} = this.props
    if (preview) return null
    return (success && <BreadcrumbV2 breadcrumb={this.breadcrumbInfo()}/>)
  }

  renderStepper = () => {
    const {order: {id, status, cancelReason}} = this.props
    if (id && ORDER_STATUS.cancelled !== status) {
      return id && <WisePortOrderSteppers data={ACG_STEPS} current={getCurrentStepIndex(status, cancelReason)}/>
    }
  }

  render() {
    return (
      <div className={styles.orderManagement}>
        {this.renderBreadcrumb()}
        <div className={styles.content}>
          {this.renderStepper()}
          {this.renderSpecifiedStepper()}
        </div>
      </div>
    )
  }
}

export default OrderManagementContainer
