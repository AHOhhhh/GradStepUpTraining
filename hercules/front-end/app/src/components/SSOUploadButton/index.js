import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'

import {isUserSessionExpired, cookie} from 'utils'
import * as actions from './actions'

import styles from './index.module.scss'

class SSOUploadButton extends Component {

  handleUpload = () => {
    const {businessLine, id} = this.props
    const token = cookie.get('token')
    if (isUserSessionExpired(token)) {
      this.props.actions.logoutAndRedirect()
    } else {
      this.props.actions.generateSSORedirectUrl(businessLine, id)
    }
  }

  render() {
    const {children, buttonClass, buttonStyle} = this.props
    return (
      <div styleName="sso-button-container">
        <button
          styleName={buttonStyle} className={'button ' + buttonClass}
          onClick={this.handleUpload}>{children}</button>
      </div>
    )
  }
}


export default connect(
  state => ({
    token: state.auth.token
  }),
  dispatch => ({actions: bindActionCreators(actions, dispatch)})
)(cssModules(SSOUploadButton, styles))
