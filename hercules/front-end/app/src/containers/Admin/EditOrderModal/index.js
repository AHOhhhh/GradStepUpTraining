import {Modal} from 'antd'
import React, {Component, PropTypes} from 'react'
import EditOrderForm from './editOrderForm'
import './index.module.scss';

export default class EditOrderModal extends Component {

  static propTypes = {
    onCloseCallback: PropTypes.func
  };

  constructor(props) {
    super(props);
    this.state = {
      visible: false,
      orderInfo: {}
    };
  }

  showModal(record) {
    this.setState({
      visible: true,
      orderInfo: record
    });
  }

  handleCancel() {
    this.setState({
      visible: false
    });
  }

  handleSubmit() {
    if (this.props.onCloseCallback) {
      this.props.onCloseCallback();
    }
    this.setState({
      visible: false
    });
  }

  destroyForm() {
    this.form.resetFields()
  }

  render() {
    const {visible} = this.state;
    return (
      <Modal
        afterClose={::this.destroyForm}
        destroyOnClose={true}
        width={800}
        title="订单编辑"
        visible={visible}
        onCancel={::this.handleCancel}
        maskClosable={false}
        footer={null}
        wrapClassName="edit-order-form"
      >
        <EditOrderForm
          onSubmitCallback={::this.handleSubmit}
          onCancelCallback={::this.handleCancel}
          orderInfo={this.state.orderInfo}
          ref={(ref) => { this.form = ref }}
        />
      </Modal>
    )
  }
}