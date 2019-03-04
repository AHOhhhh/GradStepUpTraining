import React, {Component} from 'react';
import cssModules from 'react-css-modules';
import {connect} from 'react-redux'
import {Link} from 'react-router'
import {get, includes, values, result, isNull} from 'lodash'
import {Button, Icon} from 'antd'

import {USER_ROLE} from 'constants'
import {Breadcrumb, OrderStepper, Section, Record, CancelOrderButton} from 'components';
import {getOrder, getOfflinePaymentAuditOpinion} from 'actions'
import FundInfo from '../../shared/FundInfo'
import {concatArgs, formatPrice, formatDate} from '../../../utils/format'
import {ORDER_CONFIG, ORDER_TYPE, OFFLINE_PAYMENT_ORDER_STATUS} from '../share/constants/order-config'
import AuditPaymentModal from '../../Admin/share/components/AuditPaymentModal'

import styles from './index.module.scss';

class OrderDetailsContainer extends Component {
  componentDidMount() {
    this.props.getOfflinePaymentAuditOpinion(this.props.params.orderId)
  }

  isOfflinePaymentFailure() {
    const offlinePaymentAuditOpinionMsg = get(this.props, 'offlinePaymentAuditOpinion')
    return offlinePaymentAuditOpinionMsg.payMethod === 'OFFLINE' && !isNull(offlinePaymentAuditOpinionMsg.comment)
  }

  constructor(props) {
    super(props)
    this.state = {
      confirmCancelModalVisible: false
    }
  }

  getChargingRules(order) {
    return order.chargingRules ? order.chargingRules.map(rule => (
      <tr key={order.chargingRules.indexOf(rule)}>
        <td>{rule.quantityFrom}</td>
        <td>{rule.quantityTo}</td>
        <td>{rule.unitPrice}</td>
      </tr>)) : ''
  }

  getOrderContent(order) {
    return (order.type === ORDER_TYPE.recharge) ? '' : (<div>
      <div styleName="row">
        <div styleName="label">计费规则：</div>
        <div styleName="text">
          <table>
            <thead>
              <tr>
                <th>最低单量</th>
                <th>最高单量</th>
                <th>单价（元）</th>
              </tr>
            </thead>
            <tbody>
              {this.getChargingRules(order)}
            </tbody>
          </table>
        </div>
      </div>
      <div styleName="row">
        <div styleName="label">账号期限：</div>
        <div styleName="text">{formatDate(order.effectiveFrom)}&nbsp;
          至&nbsp;{formatDate(order.effectiveTo)}</div>
      </div>
    </div>)
  }

  getOrderTypeDescription(orderType) {
    return ORDER_CONFIG[orderType || ORDER_TYPE.open].title
  }

  displayContactView(contact) {
    return (
      (<div styleName="table-like">
        <div styleName="row">
          <div styleName="cell"><span styleName="person-icon"/></div>
          <div styleName="cell">{contact.name}</div>
          <div styleName="cell">{
            concatArgs('', contact.country, contact.province, contact.city, contact.district, contact.address)
          }</div>
          <div styleName="cell">{concatArgs(', ', contact.cellphone, contact.telephone)}</div>
        </div>
      </div>)
    )
  }

  renderBreadcrumb(preview, breadCrumbData) {
    if (preview) {
      return null
    }
    return (<Breadcrumb breadcrumb={breadCrumbData}/>)
  }

  renderPriceRange(order) {
    return (order.type === ORDER_TYPE.recharge) ? '' : (<div styleName="row">
      <div styleName="label">价格区间：</div>
      <div styleName="text">
        {formatPrice(order.minPrice, 'CNY') + '~' + formatPrice(order.maxPrice, 'CNY')}
      </div>
    </div>)
  }

  renderOfflinePaidAwaitingStatusPaymentButton() {
    const {approvedPrice, currency} = this.props.order;
    return (
      <button styleName="primary wait-for-confirm" className="button primary" disabled>
        {formatPrice(approvedPrice, currency)}，已支付待确认
      </button>
    )
  }

  renderOfflinePaymentFailurePaymentButton() {
    const {approvedPrice, currency} = this.props.order
    return (
      <div>
        <Link to={`/wms/orders/${this.props.order.id}/checkout_counter`}>
          <button styleName="primary" className="button primary">
            {formatPrice(approvedPrice, currency)}，重新支付
          </button>
        </Link>
        <div styleName="result-statement">
          <Icon type="exclamation-circle-o" style={{fontSize: 16, color: '#4f4fdf'}} styleName="failure-icon"/>
          <div styleName="failure-comment">
            <span>线下支付失败！</span>
            <div>审核意见：{result(this.props.offlinePaymentAuditOpinion, 'comment', '')}</div>
          </div>
        </div>
      </div>
    )
  }

  renderWaitForPayStatusPaymentButton() {
    const {approvedPrice, currency} = this.props.order
    const userRole = get(this.props, 'auth.user.role')

    return (includes(values(USER_ROLE), userRole)
      ? (<Link to={`/wms/orders/${this.props.params.orderId}/checkout_counter`}>
        <button styleName="primary" className="button primary">
          {formatPrice(approvedPrice, currency)}&nbsp;&nbsp;立即付款
        </button>
      </Link>)
      : (<button styleName="primary" className="button primary" disabled>
        {formatPrice(approvedPrice, currency)}&nbsp;&nbsp;立即付款
      </button>))
  }

  renderPaymentButton() {
    const {status} = this.props.order

    if (status === OFFLINE_PAYMENT_ORDER_STATUS.OFFLINE_PAID_AWAITING_CONFIRM) {
      return this.renderOfflinePaidAwaitingStatusPaymentButton()
    }

    if (this.isOfflinePaymentFailure()) {
      return this.renderOfflinePaymentFailurePaymentButton()
    }

    return this.renderWaitForPayStatusPaymentButton()
  }

  handleAuditPayment() {
    const modal = this.refs.auditPaymentModal.getWrappedInstance()
    modal.showModal(this.props.order)
  }

  afterSubmit() {
    const {params: {orderId}, actions} = this.props
    actions.getOrder(orderId)
  }

  renderAuditPayment() {
    const {auth: {user}, order} = this.props
    const isNotPlatformAdmin = user && user.role !== USER_ROLE.platformAdmin

    if (order.status !== 'OfflinePaidAwaitingConfirm' || isNotPlatformAdmin) {
      return <span styleName="paymentText">待支付</span>
    }

    return (
      <div>
        <div styleName="auditPayment">
          <Button
            type="primary"
            htmlType="button"
            className="button primary audit-payment-button"
            onClick={::this.handleAuditPayment}>
            审核线下付款
          </Button>
          <div styleName="desc">
            <Icon type="info-circle-o" styleName="icon"/>
            有一笔线下账款支付，请审核
          </div>
          <AuditPaymentModal ref="auditPaymentModal" afterSubmit={::this.afterSubmit}/>
        </div>
      </div>
    )
  }

  renderPaymentStatus() {
    if (this.props.preview) {
      return ::this.renderAuditPayment()
    }
    const {telephone} = this.props.orderTemplate;
    return (
      <div>
        <div styleName="payment-btn">
          {this.renderPaymentButton()}
        </div>
        <div styleName="statement">
          <div>如协商完成，请尽快完成支付；如有疑问，请拨打服务商电话<span
            styleName="phone-number">{telephone}</span>，与客服人员取得联系。
          </div>
        </div>
      </div>
    )
  }

  render() {
    const orderId = this.props.params.orderId
    const preview = this.props.preview
    const breadcrumb = {
      paths: [{item: '个人中心', url: '/'}, {item: '我的订单', url: '/orders'}],
      currentPage: `订单: ${orderId}`
    };
    const interval = '200px';
    const {auth: {user}, order} = this.props
    const isNotPlatformAdmin = user && user.role !== USER_ROLE.platformAdmin
    const orderType = order.type
    const data = orderType === ORDER_TYPE.recharge
      ? [
        {id: 1, name: '订单提交'},
        {id: 2, name: '订单支付'},
        {id: 3, name: '充值成功'}
      ]
      : [
        {id: 1, name: '订单提交'},
        {id: 2, name: '价格审核'},
        {id: 3, name: '订单支付'},
        {id: 4, name: ORDER_CONFIG[order.type || ORDER_TYPE.open].finalStepText || '账号开通'}
      ]
    const currentStep = (orderType === ORDER_TYPE.recharge) ? 2 : 3

    return (
      <div className="container">
        {this.renderBreadcrumb(preview, breadcrumb)}
        <div styleName="order-details">

          <h2 styleName="title">订单详情</h2>
          <div styleName="stepBox">
            <OrderStepper data={data} interval={interval} current={currentStep}/>
          </div>

          <Section>
            <div styleName="order-code">
              <div styleName="row">
                <div styleName="label">订单号：</div>
                <div styleName="text">{orderId}</div>
                {isNotPlatformAdmin ?
                  <CancelOrderButton order={order}>
                    <button styleName="cancel-button">取消订单</button>
                  </CancelOrderButton> : ''}
              </div>
            </div>
            <hr/>
            <div styleName="content">
              <div styleName="row order-status">
                <div styleName="label">订单状态：</div>
                <div styleName="text">
                  {::this.renderPaymentStatus()}
                </div>
              </div>
            </div>
          </Section>

          <Section title="订单内容">
            <div styleName="content">
              <div styleName="order-details">
                <div styleName="row">
                  <div styleName="label">订单类型：</div>
                  <div styleName="text">{this.getOrderTypeDescription(orderType)}</div>
                </div>
                <div styleName="row">
                  <div styleName="label">服务内容：</div>
                  <div styleName="text">
                    {order.serviceIntro}
                  </div>
                </div>
                {this.renderPriceRange(order)}
                {this.getOrderContent(order, orderType)}
                <div styleName="row">
                  <div styleName="label">{(order.type === ORDER_TYPE.recharge) ? '充值金额：' : '核定价格：'}</div>
                  <div styleName="text approved-price">{formatPrice(order.approvedPrice)}</div>
                </div>
                <div styleName="row">
                  <div styleName="label">服务商电话：</div>
                  <div styleName="text">{order.serviceTelephone}</div>
                </div>
              </div>
            </div>
          </Section>

          <FundInfo order={order}/>

          <Section title="操作记录">
            <Record data={order.operationLogs}/>
          </Section>

          <Section title="联系方式">
            <div styleName="content">
              {this.displayContactView(order.contact)}
            </div>
          </Section>
        </div>
      </div>
    );
  }
}

export default connect(state => ({
  offlinePaymentAuditOpinion: state.checkoutCounter.offlinePaymentAuditOpinion,
  auth: state.auth
}), ({
  getOrder,
  getOfflinePaymentAuditOpinion
}))(cssModules(OrderDetailsContainer, styles, {allowMultiple: true}))

