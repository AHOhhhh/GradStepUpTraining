import React, {Component, PropTypes} from 'react' // eslint-disable-line
import {connect} from 'react-redux'
import {includes} from 'lodash'
import cssModules from 'react-css-modules'
import {bindActionCreators} from 'redux'
import {browserHistory, Link} from 'react-router'
import {Button} from 'antd'
import LeftNavigation from 'components/LeftNavigation'
import {enterpriseUserIsNotResetPassword} from 'utils'
import * as actions from 'actions'
import {USER_ROLE} from 'constants/index'
import wmsIcon from './assets/wms.svg'
import SCFOrderList from './SCFOrderList'
import Message from './Message'
import EnterpriseUserList from './EnterpriseUserList/index'
import styles from './index.module.scss'

export const ORDER_TYPE = {
  open: 'Open',
  renew: 'Renew',
  recharge: 'Recharge'
}

class LandingPageContainer extends Component { // eslint-disable-line

  static propTypes = {
    enterpriseId: PropTypes.string,
    enterpriseInfo: PropTypes.object
  }

  componentWillReceiveProps = (nextProps) => {
    if (nextProps.orderId && nextProps.orderId !== this.props.orderId) {
      this.props.actions.getOrder(nextProps.orderId)
    }
  }

  redirectPage(e) {
    this.props.actions.clearWMSOrder()
    const text = e.target.innerHTML
    if (text === '续费') {
      browserHistory.push('/wms/create?type=' + ORDER_TYPE.renew)
    } else if (text === '充值') {
      browserHistory.push('/wms/create?type=' + ORDER_TYPE.recharge)
    }
  }

  renderAdminLandingPage() {
    const {validationStatus} = this.props.enterpriseInfo
    // const statusBackgroudClass = this.getBackgroundClass(validationStatus)
    // const state = validationStatus ? statesMap.find(item => item.status === validationStatus).character : '未认证'

    return (
      <div styleName="users-management">
        <h2 styleName="title">企业用户列表</h2>
        {
          validationStatus === 'Authorized' &&
          <EnterpriseUserList/>
        }
        {
          validationStatus === 'AuthorizationInProcess' &&
          <div styleName="content">
            <p>您的企业资质暂未通过认证，功能尚未开放，</p>
            <p>请再耐心等待一下啦~O(∩_∩)O</p>
          </div>
        }
        {
          (!validationStatus || validationStatus === 'Unauthorized') &&
          <div styleName="content">
            <p>您的企业资质认证失败或未上传，请重新<Link to="/enterprise_info">上传企业资质</Link></p>
          </div>
        }
      </div>
    )
  }

  renderUserLandingPage() {
    const {wmsOrder} = this.props
    const isDisplayWmsOrder = wmsOrder.id && includes([ORDER_TYPE.open, ORDER_TYPE.renew], wmsOrder.type) && wmsOrder.status === 'Closed'
    let date = []
    if (isDisplayWmsOrder) {
      date = wmsOrder.effectiveTo.split('-')
    }

    return (<div styleName="right">
      <div styleName="products-container">
        {this.renderAllProducts()}
      </div>

      <Message />
      <SCFOrderList />

      {isDisplayWmsOrder && (<div styleName="wms-container">
        <h2>
          <img src={wmsIcon}/>
          <span>仓储管理系统  WMS</span>
        </h2>
        <div styleName="effective-date">有效期至 <span>{date[0]}</span> - <span>{date[1]}</span> - <span>{date[2]}</span>
        </div>
        <div styleName="operation">
          <button type="button" className="button primary" onClick={::this.redirectPage}>续费</button>
          <button type="button" className="button primary" onClick={::this.redirectPage}>充值</button>
        </div>
      </div>)}
    </div>)
  }

  componentDidMount() {
    const {user, enterpriseId} = this.props
    const isAdmin = user && user.role === USER_ROLE.enterpriseAdmin
    if (enterpriseId) {
      this.props.actions.getEnterpriseInfo(enterpriseId)
    }
    if (!isAdmin) {
      this.props.actions.clearWMSOrder()
      this.props.actions.getLandingPageWMSOrderByEnterprise(enterpriseId)
    }
  }

  handleClick(orderType) {
    if (includes(['mwp', 'scf'], orderType)) {
      return browserHistory.push({
        pathname: `/${orderType}/create_order`,
        state: {isFromLandingPage: true}
      })
    }

    return browserHistory.push({
      pathname: `/${orderType}/create`,
      state: {isFromLandingPage: true}
    })
  }

  renderAllProducts = () => (
    <div>
      <div styleName="product-item acg">
        <h3>航空货运</h3>
        <Button styleName="create-order-button" onClick={() => this.handleClick('acg')}>快速下单</Button>
      </div>
      <div styleName="product-item wms">
        <h3>仓储管理</h3>
        <Button styleName="create-order-button" onClick={() => this.handleClick('wms')}>快速下单</Button>
      </div>
      <div styleName="product-item mwp">
        <h3>敬请期待</h3>
      </div>
      <div styleName="product-item scf">
        <h3>敬请期待</h3>
      </div>
    </div>
  )

  render() {
    const isAdmin = this.props.user && this.props.user.role === USER_ROLE.enterpriseAdmin

    return (
      <div className="user-container">
        <LeftNavigation location={this.props.location.pathname}/>
        {isAdmin ? this.renderAdminLandingPage() : this.renderUserLandingPage()}
      </div>
    )
  }
}

export default enterpriseUserIsNotResetPassword(connect(
  state => ({
    orderId: state.wms.orderId,
    wmsOrder: state.wms.order,
    enterpriseId: state.auth.enterpriseId || (state.auth.user ? state.auth.user.enterpriseId : null),
    enterpriseInfo: state.enterpriseInfo.info,
    user: state.auth.user,
  }),
  dispatch => ({
    actions: bindActionCreators(actions, dispatch)
  })
)(cssModules(LandingPageContainer, styles, {allowMultiple: true})))
