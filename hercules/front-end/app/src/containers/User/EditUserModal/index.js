import {Modal} from 'antd'
import React, {Component, PropTypes} from 'react'
import EditUserForm from './editUserForm'

export default class EditUserModal extends Component {

  static propTypes = {
    onCloseCallback: PropTypes.func
  }

  constructor(props) {
    super(props)
    this.state = {
      visible: false
    }
  }

  showModal(record) {
    this.setState({
      visible: true,
      user: record
    })
  }

  handleCancel() {
    if (this.props.onCloseCallback) {
      this.props.onCloseCallback()
    }
    this.setState({
      visible: false
    })
  }

  handleClose() {
    this.refs.editUserForm.resetFields()
    this.handleCancel()
  }

  render() {
    const {visible} = this.state
    return (
      <Modal
        width={635}
        title="编辑企业用户"
        visible={visible}
        onCancel={::this.handleCancel}
        afterClose={::this.handleClose}
        maskClosable={false}
        footer={null}
      >
        <EditUserForm onSubmitCallback={::this.handleCancel} user={this.state.user} ref="editUserForm" />
      </Modal>
    )
  }
}