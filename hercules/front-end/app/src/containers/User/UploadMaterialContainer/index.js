import React, {Component} from 'react'

import {isUserSessionExpired} from 'utils'

import Button from 'antd/lib/button'
import cssModules from 'react-css-modules'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import * as action from 'actions'

import styles from './index.module.scss';

@connect(
  state => ({
    user: state.auth.user,
    token: state.auth.token
  }),
  dispatch => ({actions: bindActionCreators(action, dispatch)})
)
class UploadMaterialContainer extends Component {
  uploadMaterial() {
    if (isUserSessionExpired(this.props.token)) {
      this.props.actions.logoutAndRedirect()
    } else {
      this.props.actions.generateRedirectUrl()
    }
  }

  render() {
    return (
      <div className={styles['mock-container']}>
        <h1>上传资料Mock页面</h1>
        <Button
          type="primary"
          htmlType="button"
          className="button primary"
          onClick={::this.uploadMaterial}>
          上传资料
        </Button>
      </div>
    )
  }
}

export default cssModules(UploadMaterialContainer, styles)
