import React, {Component} from 'react'
import {Modal} from 'antd'

import ResetPasswordForm from './ResetPasswordForm'

class ResetPasswordModal extends Component {
  constructor(props) {
    super(props)
    this.state = {
      visible: false,
      key: 0,
      user: {}
    }
  }

  showModal(record) {
    this.setState({
      visible: true,
      user: record,
      key: this.state.key + 1
    })
  }

  handleComplete() {
    this.setState({
      visible: false
    })
  }

  render() {
    const {user, visible} = this.state
    if (!user) { return null }
    return (
      <Modal
        key={this.state.key}
        width="570px"
        title={<span>重置密码 - <span style={{color: '#f9a300'}}>{user.username}</span></span>}
        visible={visible}
        footer={null}
        onCancel={this.handleComplete.bind(this)}
      >
        <ResetPasswordForm onComplete={::this.handleComplete} user={user}/>
      </Modal>
    )
  }
}

export default ResetPasswordModal