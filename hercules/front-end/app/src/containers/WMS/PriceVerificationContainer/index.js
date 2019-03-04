import React, {Component} from 'react'
import {OrderStepper, Breadcrumb, Section, Record, CancelOrderButton} from 'components'
import cssModules from 'react-css-modules'
import {connect} from 'react-redux'
import _ from 'lodash'
import {browserHistory} from 'react-router'

import {USER_ROLE} from 'constants'
import {concatArgs} from 'utils/format'
import FundInfo from '../../shared/FundInfo'

import styles from './index.module.scss';

import {ORDER_CONFIG, ORDER_TYPE} from '../share/constants/order-config'

class PriceVerificationContainer extends Component {
  refreshOrder() {
    const orderId = this.props.params.orderId
    this.props.actions.getOrder(orderId);
    browserHistory.replace(window.location.pathname)
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

  renderPayStatus(preview) {
    if (preview) {
      return null
    }
    return (
      <span>
          （客服人员将很快您联系并协商订单的价格和细节。您可以拨打服务商电话
           <span styleName="info-font nowrap">400-890-0505</span>，尽快完成订单。）
      </span>
    )
  }

  render() {
    const {orderTemplate, order, params, preview, auth: { user }} = this.props
    const isNotPlatformAdmin = user && user.role !== USER_ROLE.platformAdmin

    // const nameMap = {NEW: '账号开通', RENEW: '续费成功', RECHARGE: '充值成功'}
    const data = [{id: 1, name: '订单提交'}, {id: 2, name: '价格审核'}, {id: 3, name: '订单支付'}, {
      id: 4,
      name: ORDER_CONFIG[order.type || ORDER_TYPE.open].finalStepText || '账号开通'
    }];
    const breadCrumbData = {
      paths: [
        {
          item: '个人中心',
          url: '/',
        },
        {
          item: '我的订单',
          url: '/orders',
        }
      ],
      currentPage: '订单：' + order.id,
    };
    const interval = '200px';
    const telephones = [];
    ['telephone', 'cellphone'].forEach(key => {
      const value = order.contact[key];
      if (!_.isEmpty(value)) {
        telephones.push(value)
      }
    })
    return (
      <div styleName="priceVerification" className="container">
        {this.renderBreadcrumb(preview, breadCrumbData)}
        <div styleName="title">
          订单详情
        </div>
        <div styleName="stepBox">
          <OrderStepper data={data} interval={interval} current={2}/>
        </div>
        <Section>
          <div styleName="order-code">
            <div styleName="row">
              <div styleName="label">订单号：</div>
              <div styleName="text">{params.orderId}</div>
              {isNotPlatformAdmin ?
                <CancelOrderButton order={order}>
                  <button styleName="cancel-button">取消订单</button>
                </CancelOrderButton> : ''}
            </div>
          </div>
          <div styleName="content-box order-status">
            <div styleName="row">
              <div styleName="label"><span>订单状态：</span></div>
              <div styleName="text complex-content">
                <span styleName="highlight-font larger-font">等待价格协商</span>
                {this.renderPayStatus(preview)}
              </div>
            </div>
            <div styleName="row">
              <div styleName="label"/>
              <div styleName="text">若已签署合同，价格协商完成，请点击<a styleName="info-font refresh" onClick={() => this.refreshOrder()}>刷新</a>页面，取得最终支付价格进行支付购买
              </div>
            </div>
          </div>
        </Section>
        <Section title="订单内容">
          <div styleName="content-box">
            <div styleName="row">
              <div styleName="label">订单类型：</div>
              <div styleName="text">{this.getOrderTypeDescription(order.type)}</div>
            </div>
            <div styleName="row">
              <div styleName="label">服务内容：</div>
              <div styleName="text">
                {orderTemplate.describe}
              </div>
            </div>
            <div styleName="row">
              <div styleName="label">服务期限：</div>
              <div styleName="text">{orderTemplate.duration}</div>
            </div>
            <div styleName="row">
              <div styleName="label">参考价格：</div>
              <div styleName="text total-price">{orderTemplate.price}</div>
            </div>
            <div styleName="row">
              <div styleName="label">参考价格区间：</div>
              <div styleName="text">{orderTemplate.priceRegion}</div>
            </div>
            <div styleName="row">
              <div styleName="label">服务商电话：</div>
              <div styleName="text">{order.serviceTelephone}</div>
            </div>
          </div>
        </Section>

        <FundInfo order={order}/>

        <Section title="操作记录">
          <Record data={order.operationLogs}/>
        </Section>

        <Section title="联系方式">
          <div styleName="content-box ">
            {this.displayContactView(order.contact)}
          </div>
        </Section>
      </div>
    );
  }
}

export default connect(state => ({
  auth: state.auth
}), undefined)(cssModules(PriceVerificationContainer, styles, {allowMultiple: true}))
