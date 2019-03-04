import {Modal} from 'antd'
import React, {PropTypes} from 'react'
import CreateUserForm from './createUserForm'

export default class CreateUserModal extends React.Component {

  static propTypes = {
    onCloseCallback: PropTypes.func
  }

  constructor(props) {
    super(props)
    this.state = {
      visible: false
    }
  }

  showModal() {
    this.setState({
      visible: true
    })
  }

  handleCancel() {
    this.setState({
      visible: false
    })
  }

  handleClose() {
    this.refs.createUserForm.resetFields()
    this.props.onCloseCallback()
  }

  render() {
    const {visible} = this.state
    return (
      <Modal
        width={635}
        title="新增企业用户"
        visible={visible}
        onCancel={::this.handleCancel}
        afterClose={::this.handleClose}
        maskClosable={false}
        footer={null}
      >
        <CreateUserForm onSubmitCallback={::this.handleCancel} ref="createUserForm" />
      </Modal>
    )
  }
}