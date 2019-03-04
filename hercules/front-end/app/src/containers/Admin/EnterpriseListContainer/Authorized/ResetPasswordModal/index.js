import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import {Modal, Button, message} from 'antd'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import styles from './index.module.scss'
import * as action from './actions'

class RestPasswordModal extends Component {

  handleSubmit(e) {

    e.preventDefault();
    const userId = this.props.user.id;
    this.props.actions.resetPassword(userId)
      .then(() => {
        message.success('重置密码成功！')
        this.handleCancel();
      })
      .catch(() => {
        message.error('重置密码失败！')
        this.handleCancel();
      });
  }

  handleCancel() {
    this.props.onComplete();
  }

  render() {
    return (
      <Modal
        title="重置企业管理员密码"
        width="546px"
        visible={this.props.visible}
        onCancel={this.handleCancel.bind(this)}
        footer={null}>
        <div styleName="reset-password">
          <div styleName="messageInfoBefore">确定重置企业管理员密码为初始密码?</div>
          <div styleName="buttonGroup">
            <Button styleName="submitButton" onClick={this.handleSubmit.bind(this)}>确定</Button>
            <Button styleName="cancelButton" onClick={this.handleCancel.bind(this)}>取消</Button>
          </div>
        </div>
      </Modal>
    )
  }
}

export default connect(
  state => ({enterprise: state.enterpriseUser}),
  dispatch => ({
    actions: bindActionCreators(action, dispatch)
  })
)(cssModules(RestPasswordModal, styles));