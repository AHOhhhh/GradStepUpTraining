import React, { Component } from 'react'
import { connect } from 'react-redux'
import cssModules from 'react-css-modules'
import _ from 'lodash'
import { guid } from 'utils'
import BASE_URL from 'configs'
import { Input, Button, Tabs } from 'antd'
import styles from './index.module.scss'
import { getOrders } from './actions'
import OrderLogistic from '../../ACG/OrderManagementContainer/OrderTrackingContainer/OrderLogistic/index'


const cssWrapper = Component => cssModules(Component, styles, {allowMultiple: true})

@connect(({ acg: orders = {} }) => ({ ...orders }), { getOrders })
@cssWrapper
export default class OrderSearchContainer extends Component {
  state = {
    validator: {},
    captchaId: guid()
  }
  validate(orderIdsStr, captcha) {
    const orderIds = _.compact(orderIdsStr ? orderIdsStr.split(/,|，/) : [])
    const isOrderIdsValid = !_.isEmpty(orderIds) && _.size(orderIds) <= 5
    const getMessage = () => ({
      orderIds: isOrderIdsValid ? '' : '请检查运单号码格式是否正确: 运单号以逗号“,”隔开,最多查询5个运单号。',
      captcha: captcha ? '' : '请输入验证码!'
    })
    const isValid = !!(isOrderIdsValid && captcha)
    return {
      isValid,
      orderIds,
      message: isValid ? '' : getMessage()
    }
  }
  search(orderIdsStr, captcha, captchaId) {
    const { getOrders } = this.props
    const validator = this.validate(orderIdsStr, captcha)
    if (validator.isValid) {
      this.setState({ validator, captchaId: guid() }, () => {
        getOrders(validator.orderIds.join(','), captchaId, captcha)
      })
    } else {
      this.setState({ validator, captchaId: guid() })
    }
  }
  searchHandler() {
    const value = _.trim(this.orderIds.refs.input.value)
    const captcha = _.trim(this.captcha.refs.input.value)
    this.search(value, captcha, this.state.captchaId)
  }
  renderTabPanes(orders) {
    const { validator: { isValid, orderIds } } = this.state
    if (isValid) {
      const TabPane = Tabs.TabPane
      return (
        <div className="tab-panes">
          <Tabs type="card" animated={false}>
            {orderIds.map(
              (orderId) => {
                const order = _.find(orders, ({id}) => id === orderId)
                return (
                  <TabPane tab={orderId} key={orderId}>
                    <div styleName="logistic-wrapper">
                      <OrderLogistic
                        order={order}
                        needTitle={false}
                        needShowMore={false}
                        warningMessage="抱歉！当前无法获取订单的物流信息，请检查订单号是否正确或稍后查询！"
                      />
                    </div>
                  </TabPane>
                )
              }
            )}
          </Tabs>
        </div>
      )
    }
  }
  getQuery() {
    const queryArray = decodeURI(window.location.search).match(/^\?ids=(.+)&captchaId=(.+)&captcha=(.+)/)
    return {
      defaultOrderIds: _.get(queryArray, '[1]', ''),
      defaultCaptchaId: _.get(queryArray, '[2]', ''),
      defaultCaptcha: _.get(queryArray, '[3]', '')
    }
  }
  componentDidMount() {
    const { defaultOrderIds, defaultCaptcha, defaultCaptchaId } = this.getQuery()
    if (!_.isEmpty(defaultOrderIds) && defaultCaptcha && defaultCaptchaId) {
      this.search(defaultOrderIds, defaultCaptcha, defaultCaptchaId)
    }
  }
  refreshCaptcha() {
    this.setState({ captchaId: guid() })
  }
  hasError(item) {
    return !!_.get(this.state.validator, `message.${item}`)
  }
  render() {
    const { validator: { message = {} }, captchaId } = this.state
    const { defaultOrderIds, defaultCaptcha } = this.getQuery()
    const orders = _.get(this.props.orders, 'data', [])
    const captchaError = _.get(this.props.orders, 'error.data.message') === 'invalid captcha' ? '验证码错误或失效' : ''
    return (
      <div styleName="search-container">
        <div styleName="order-search">
          <div>
            <h2 styleName="title">订单物流跟踪</h2>
          </div>
          <div>
            <div styleName="search-box">
              <div styleName={`search-item order${this.hasError('orderIds') ? ' invalid' : ''}`}>
                <Input
                  placeholder="最多查询5个订单号，订单号以逗号“,”隔开"
                  defaultValue={defaultOrderIds}
                  ref={item => { this.orderIds = item }}
                  onPressEnter={::this.searchHandler}
                />
                <p>{message.orderIds}</p>
              </div>
              <div styleName={`search-item captcha-box${(this.hasError('captcha') || !!captchaError) ? ' invalid' : ''}`}>
                <Input
                  placeholder="输入验证码"
                  defaultValue={defaultCaptcha}
                  ref={item => { this.captcha = item }}
                  onPressEnter={::this.searchHandler}
                />
                <img src={`${BASE_URL}/captcha?captchaId=${captchaId}`} onClick={::this.refreshCaptcha}/>
                <p>{ captchaError || message.captcha }</p>
              </div>
              <Button type="primary" icon="search" onClick={::this.searchHandler}>订单查询</Button>
            </div>
          </div>
          {_.isEmpty(captchaError) && this.renderTabPanes(orders)}
        </div>
      </div>
    )
  }
}
