import React, {Component, PropTypes} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import {Spin} from 'antd'
import {browserHistory} from 'react-router'
import Modal from 'antd/lib/modal'
import {EnterpriseUsers} from 'constants'
import * as action from 'actions'
import {Header, Footer} from 'components'

@connect(
  state => ({
    user: state.auth.user,
    showSessionExpiredMask: state.auth.showSessionExpiredMask,
    isSpin: state.checkoutCounter.isSpin
  }),
  dispatch => ({actions: bindActionCreators(action, dispatch)})
)
export default class AppContainer extends Component { // eslint-disable-line react/prefer-stateless-function
  static propTypes = {
    children: PropTypes.node.isRequired
  }
  static defaultProps = {
    children: []
  }

  renderSessionExpiredMask = () => {
    const self = this
    return Modal.warning({
      title: '尊敬的用户，由于您长时间未操作，需要重新登录！',
      width: '535px',
      onOk() {
        self.props.actions.logoutAndRedirect()
        if (self.props.user) {
          if (EnterpriseUsers.includes(self.props.user.role)) {
            browserHistory.push('/login')
          } else {
            browserHistory.push('/admin/login')
          }
        } else {
          browserHistory.push('/login')
        }
      }
    })
  }

  componentWillMount() {
    const regs = [/(^|\s)and($|\s)/i,
      /(^|\s)exec($|\s)/i,
      /(^|\s)count($|\s)/i,
      /(^|\s)chr($|\s)/i,
      /(^|\s)mid($|\s)/i,
      /(^|\s)master($|\s)/i,
      /(^|\s)or($|\s)/i,
      /(^|\s)truncate($|\s)/i,
      /(^|\s)char($|\s)/i,
      /(^|\s)declare($|\s)/i,
      /(^|\s)join($|\s)/i,
      /insert/i,
      /select/i,
      /delete/i,
      /update/i,
      /create/i,
      /drop/i,
      /</i,
      />/i,
      /\/\*/i,
      /\*\//i,
      /’/i,
      /\|/i,
      /;/i,
      /\$/i,
      /"/i,
      /\\/i,
      /'/i,
      /\(\s\)/i,
      /0x0d/i,
      /0x0a/i]
    const path = decodeURIComponent(window.location.search)
    const result = regs.some(reg => reg.test(path))
    if (result) {
      browserHistory.replace('/notfound')
    }
  }

  render() {
    return (
      <Spin spinning={this.props.isSpin} size="large">
        <div>
          <Header {...this.props} />
          {React.cloneElement(this.props.children, this.props)}
          <Footer/>
          {this.props.showSessionExpiredMask && this.renderSessionExpiredMask()}
        </div>
      </Spin>
    )
  }
}
