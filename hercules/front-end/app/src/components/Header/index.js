import React, { Component, PropTypes } from 'react'
import cssModules from 'react-css-modules'
import {USER_ROLE} from 'constants'
import _ from 'lodash'
import { Link } from 'react-router'
import styles from './index.module.scss'
import ProductDropdown from './product-dropdown'
import CorporationDropdown from './corporation-dropdown'
import {official} from '../../config/url'

class Header extends Component {
  logout() {
    this.props.actions.logoutAndRedirect()
  }

  isPlatformAdmin(user) {
    return (user && [USER_ROLE.platformAdmin].includes(user.role)) || _.get(this.props, 'location.pathname') === '/admin/login'
  }
  isSearchPage() {
    return _.get(this.props, 'location.pathname') === '/order_search'
  }
  goToHome() {
    window.location.href = official.home
  }
  renderMenu() {
    const {user} = this.props
    const host = official.home
    const shouldShowMenu = !this.isPlatformAdmin(user) && !this.isSearchPage()
    if (shouldShowMenu) {
      return (
        <ul styleName="menu">
          <li><a href={official.home} styleName="menu-item">首页</a></li>
          <li styleName="dropdown production">
            <div styleName="relative">产品<span styleName="icon-down"/></div>
            <div styleName="dropdown-content production-dropdown-content"><ProductDropdown/></div>
          </li>
          <li styleName="dropdown relative corporation">
            <a styleName="menu-item corporation">我要合作<span styleName="icon-down"/></a>
            <div styleName="dropdown-content corporation-dropdown-content"><CorporationDropdown/></div>
          </li>
          <li><a href={`${host}/case`} styleName="menu-item">成功案例</a></li>
        </ul>
      )
    }
  }
  renderHeaderFuntions() {
    let header
    if (!this.isPlatformAdmin()) {
      header = (
        <div styleName="login-header right">
          <Link to="/login">登录</Link>
          <span styleName="split-line"/>
          <Link to="/terms">注册</Link>
        </div>
      )
    }
    const {user} = this.props
    if (user) {
      header = (
        <div styleName="login-header right">
          <span><Link to={this.isPlatformAdmin(user) ? '/admin/orders' : '/'}>{user.username}</Link>，欢迎回来</span>
          <span styleName="split-line"/>
          <button styleName="logout-button" onClick={::this.logout}>退出</button>
        </div>
      )
    }
    return header
  }
  render() {
    const {user} = this.props
    const topBarTheme = 'top-bar-theme-' + (this.isPlatformAdmin(user) ? 'platform' : 'enterprise')
    
    return (
      <div styleName="header">
        <div styleName={'topBar ' + topBarTheme}>
          <div styleName="container">
            <div styleName="row" className="row">
              {this.renderHeaderFuntions()}
              <div styleName="tel right"><span styleName="icon"/><span>400-890-0505</span></div>
            </div>
          </div>
        </div>
        <div styleName="container">
          <div styleName="row header-row">
            <div styleName="logo left" onClick={::this.goToHome} />
            <div styleName="describe left"><div>Hercules</div><div>Logistics Platform</div></div>
            {this.renderMenu()}
          </div>
        </div>
      </div>
    );
  }
}

Header.propTypes = {
  user: PropTypes.object,
  location: PropTypes.object
}

export default cssModules(Header, styles, {allowMultiple: true})