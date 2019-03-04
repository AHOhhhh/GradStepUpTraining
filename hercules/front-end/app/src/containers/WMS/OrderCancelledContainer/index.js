import React, {Component} from 'react';
import cssModules from 'react-css-modules';
import {connect} from 'react-redux'
import {Link} from 'react-router'

import {Breadcrumb, Section, Record} from 'components';
import {getOrder} from 'actions'
import { concatArgs, formatPrice, formatDate, formatDateWithHourMinute } from 'utils/format'
import {USER_ROLE} from 'constants'

import {ORDER_CONFIG, ORDER_TYPE} from '../share/constants/order-config'

import styles from './index.module.scss';

class OrderCancelledContainer extends Component {
  getChargingRules(order) {
    return order.chargingRules ? order.chargingRules.map(rule => (
      <tr key={order.chargingRules.indexOf(rule)}>
        <td>{rule.quantityFrom}</td>
        <td>{rule.quantityTo}</td>
        <td>{rule.unitPrice}</td>
      </tr>)) : ''
  }

  getOrderContent(order, isNotAudited) {
    return (order.type === ORDER_TYPE.recharge) ? '' : (<div>
      {isNotAudited ? '' :
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
      </div>}
      <div styleName="row">
        <div styleName="label">账号期限：</div>
        <div styleName="text">{isNotAudited ? this.props.orderTemplate.duration : concatArgs(' 至 ', formatDate(order.effectiveFrom), formatDate(order.effectiveTo))}</div>
      </div>
    </div>)
  }

  getOrderTypeDescription(orderType) {
    return ORDER_CONFIG[orderType || ORDER_TYPE.open].title
  }

  displayContactView(contact = {}) {
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

  render() {
    const {orderTemplate, order, params, preview, auth: { user }} = this.props
    const orderId = params.orderId
    const isNotPlatformAdmin = user && user.role !== USER_ROLE.platformAdmin
    const breadcrumb = {
      paths: [{item: '个人中心', url: '/'}, {item: '我的订单', url: '/orders'}],
      currentPage: `订单: ${orderId}`
    };
    const orderType = order.type
    const isNotAudited = !order.approvedPrice

    return (
      <div className="container">
        {this.renderBreadcrumb(preview, breadcrumb)}
        <div styleName="order-details">

          <h2 styleName="title">订单详情</h2>

          <Section>
            <div styleName="order-code">
              <div styleName="row">
                <div styleName="label">订单号：</div>
                <div styleName="text">{orderId}</div>
              </div>
            </div>
            <hr/>
            <div styleName="content">
              <div styleName="row order-status">
                <div styleName="label">订单状态：</div>
                <div styleName="text cancelled-description">
                  <div styleName="status-statement">订单已取消</div>
                  <div styleName="cancel-date"><span>取消时间：</span><span>{formatDateWithHourMinute(order.updatedAt)}</span></div>
                  {(isNotPlatformAdmin && isNotPlatformAdmin) ?
                    <div styleName="recreate-link">若您需要开通WMS服务，您可以<Link to="/wms/create">重新开通服务</Link></div>
                    : ''}
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
                    {isNotAudited ? orderTemplate.describe : order.serviceIntro}
                  </div>
                </div>
                {this.renderPriceRange(order)}
                {this.getOrderContent(order, isNotAudited)}
                <div styleName="row">
                  <div styleName="label">{(order.type === ORDER_TYPE.recharge) ? '充值金额：' : '核定价格：'}</div>
                  <div styleName="text approved-price">{isNotAudited ? orderTemplate.price : formatPrice(order.approvedPrice)}</div>
                </div>
                <div styleName="row">
                  <div styleName="label">服务商电话：</div>
                  <div styleName="text">{order.serviceTelephone}</div>
                </div>
              </div>
            </div>
          </Section>

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
  orderTemplate: state.wms.orderTemplate
}), {
  getOrder
})(cssModules(OrderCancelledContainer, styles, {allowMultiple: true}))
