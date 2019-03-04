import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import _ from 'lodash'
import moment from 'moment'

import {formatPrice, formatDate, concatArgs} from 'utils/format'
import {Breadcrumb, OrderStepper, Section, Record} from 'components'

import FundInfo from '../../shared/FundInfo'
import PaymentResult from './paymentResult'
import {ORDER_TYPE} from '../share/constants/order-config'

import styles from './index.module.scss'

const RESULT_MESSAGES = [
  {
    type: ORDER_TYPE.open,
    status: 'Paid',
    data: {
      orderStatusMessage: '正在为您开通账号，请耐心等待！',
      orderStatusStatement: '正在为您的企业开通WMS账号，请耐心等待；',
      buttonsVisible: false,
      orderTypeTitle: 'WMS账号开通',
      chargingRulesVisible: true,
      stepsData: [{id: 1, name: '订单提交'}, {id: 2, name: '价格审核'}, {id: 3, name: '订单支付'}, {id: 4, name: '账号开通'}],
      currentStepIndex: 4,
      recordsMap: {Submitted: '订单提交', Audited: '价格审核', Paid: '订单支付', Closed: '账号开通'}
    }
  },
  {
    type: ORDER_TYPE.open,
    status: 'Closed',
    data: {
      orderStatusMessage: '账号已成功开通 ！',
      orderStatusStatement: '账号已开通。',
      buttonsVisible: true,
      orderTypeTitle: 'WMS账号开通',
      chargingRulesVisible: true,
      stepsData: [{id: 1, name: '订单提交'}, {id: 2, name: '价格审核'}, {id: 3, name: '订单支付'}, {id: 4, name: '账号开通'}],
      currentStepIndex: 5,
      recordsMap: {Submitted: '订单提交', Audited: '价格审核', Paid: '订单支付', Closed: '账号开通'}
    }
  },
  {
    type: ORDER_TYPE.renew,
    status: 'Paid',
    data: {
      orderStatusMessage: '正在为您的账号续费，请耐心等待！',
      orderStatusStatement: '正在为您的企业WMS账号续费，请耐心等待；',
      buttonsVisible: false,
      orderTypeTitle: 'WMS账号续费',
      chargingRulesVisible: true,
      stepsData: [{id: 1, name: '订单提交'}, {id: 2, name: '价格审核'}, {id: 3, name: '订单支付'}, {id: 4, name: '续费成功'}],
      currentStepIndex: 4,
      recordsMap: {Submitted: '订单提交', Audited: '价格审核', Paid: '订单支付', Closed: '续费成功'}
    }
  },
  {
    type: ORDER_TYPE.renew,
    status: 'Closed',
    data: {
      orderStatusMessage: '账号续费成功 ！',
      orderStatusStatement: '账号续费成功 ！',
      buttonsVisible: true,
      orderTypeTitle: 'WMS账号续费',
      chargingRulesVisible: true,
      stepsData: [{id: 1, name: '订单提交'}, {id: 2, name: '价格审核'}, {id: 3, name: '订单支付'}, {id: 4, name: '续费成功'}],
      currentStepIndex: 5,
      recordsMap: {Submitted: '订单提交', Audited: '价格审核', Paid: '订单支付', Closed: '续费成功'}
    }
  },
  {
    type: ORDER_TYPE.recharge,
    status: 'Closed',
    data: {
      orderStatusMessage: '充值成功',
      orderStatusStatement: '充值成功。',
      buttonsVisible: false,
      orderTypeTitle: 'WMS账号充值',
      chargingRulesVisible: false,
      stepsData: [{id: 1, name: '订单提交'}, {id: 2, name: '订单支付'}, {id: 3, name: '充值成功'}],
      currentStepIndex: 5,
      recordsMap: {Submitted: '订单提交', Paid: '订单支付', Closed: '充值成功'}
    }
  }
]

class PaymentResultContainer extends Component {

  componentDidMount() {
    const enterpriseId = this.props.auth.enterpriseId
    if (enterpriseId) {
      this.props.actions.getEnterpriseInfo(enterpriseId)
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

  getOrderContent(order, chargingRulesVisible) {
    if (chargingRulesVisible) {
      return (<div>
        <div className="row">
          <div className="label">计费规则：</div>
          <div className="text">
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
        <div className="row">
          <div className="label">账号期限：</div>
          <div className="text">{formatDate(order.effectiveFrom)}&nbsp;
            至&nbsp;{formatDate(order.effectiveTo)}</div>
        </div>
      </div>)
    }

    return ''
  }

  getMessageByTypeAndStatus(status, type) {
    if (status && type) {
      return (_.find(RESULT_MESSAGES, (item) => (item.status === status && item.type === type)) || {}).data || {}
    }
    return {}
  }

  formatDate(str) {
    return str ? moment(str).format('YYYY.MM.DD HH:mm:ss') : '/'
  }

  displayContactView(contact) {
    return (
      (<div className="table-like">
        <div className="row">
          <div className="cell"><span className="person-icon"/></div>
          <div className="cell">{contact.name}</div>
          <div className="cell">{
            concatArgs('', contact.country, contact.province, contact.city, contact.district, contact.address)
          }</div>
          <div className="cell">{concatArgs(', ', contact.cellphone, contact.telephone)}</div>
        </div>
      </div>)
    )
  }

  renderPriceRange(order) {
    return (order.type === ORDER_TYPE.recharge) ? '' : (<div className="row">
      <div className="label">价格区间：</div>
      <div className="text">
        {formatPrice(order.minPrice, 'CNY') + '~' + formatPrice(order.maxPrice, 'CNY')}
      </div>
    </div>)
  }

  renderBreadcrumb(preview, breadCrumbData) {
    if (preview) {
      return null
    }
    return (<Breadcrumb breadcrumb={breadCrumbData}/>)
  }

  render() {
    const breadcrumb = {
      paths: [{item: '个人中心', url: '/'}, {item: '我的订单', url: '/orders'}],
      currentPage: `订单: ${this.props.params.orderId}`
    };

    const preview = this.props.preview
    const interval = '200px';
    const order = this.props.order;

    const result = this.getMessageByTypeAndStatus(order.status, order.type)
    return (
      <div className="container">
        {this.renderBreadcrumb(preview, breadcrumb)}
        <div className={styles['payment-result']}>
          <h2 className="title">订单详情</h2>
          <div className="stepBox">
            <OrderStepper data={result.stepsData || []} interval={interval} current={result.currentStepIndex}/>
          </div>
          <PaymentResult order={order} enterprise={this.props.enterprise} result={result} preview={preview}/>
          <Section title="订单内容">
            <div className="content">
              <div className="order-details">
                <div className="row">
                  <div className="label">订单类型：</div>
                  <div className="text">{result.orderTypeTitle}</div>
                </div>
                <div className="row">
                  <div className="label">服务内容：</div>
                  <div className="text">
                    {order.serviceIntro}
                  </div>
                </div>
                {this.renderPriceRange(order)}
                {this.getOrderContent(order, result.chargingRulesVisible)}
                <div className="row">
                  <div className="label">{(order.type === ORDER_TYPE.recharge) ? '充值金额：' : '核定价格：'}</div>
                  <div className="text approved-price">{formatPrice(order.approvedPrice)}</div>
                </div>
                <div className="row">
                  <div className="label">服务商电话：</div>
                  <div className="text">{order.serviceTelephone}</div>
                </div>
              </div>
            </div>
          </Section>
          <Section title="操作记录">
            <div className="content">
              <Record data={order.operationLogs} statusMap={result.recordsMap}/>
            </div>
          </Section>

          <FundInfo order={order}/>

          <Section title="联系方式">
            <div className="content">
              {this.displayContactView(order.contact)}
            </div>
          </Section>
        </div>
      </div>
    );
  }
}

export default cssModules(PaymentResultContainer, styles)
