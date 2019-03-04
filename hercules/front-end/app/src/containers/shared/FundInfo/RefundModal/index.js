import React, {Component} from 'react'
import {Modal, Button, message} from 'antd'
import cssModule from 'react-css-modules'

import {handleRefund} from './action'
import RefundForm from './RefundForm'

import styles from './index.module.scss'

class FundInfoModal extends Component {
  state = {
    visible: false,
    order: this.props.order
  }

  close() {
    this.setState({
      visible: false
    }, () => {
      this.RefundForm.resetFields()
    })
  }

  open(record) {
    const order = this.props.order || record
    this.setState({
      visible: true,
      order
    })
  }

  handleRefund() {
    this.RefundForm.validateFieldsAndScroll((errors, values) => {
      if (!errors) {
        this.close()
        handleRefund(this.state.order.id, values)
          .then(() => {
            return this.props.getOrderTransaction(this.state.order.id)
          })
          .then(() => {
            setTimeout(() => {
              message.success('退款成功！')
            }, 1500)
          })
          .catch(() => {
            message.error('退款失败！')
          })
      }
    })
  }

  render() {
    return (
      <Modal
        onCancel={::this.close}
        visible={this.state.visible}
        width={640}
        title="退款"
        maskClosable={false}
        footer={null}
        wrapClassName="edit-order-form"
      >
        <section className={styles.auditPaymentContainer}>
          {this.state.order && <RefundForm
            order={this.state.order}
            ref={ref => {
              this.RefundForm = ref
            }}
          />}
          <div className="row operation">
            <Button
              type="primary"
              htmlType="button"
              className="button primary"
              onClick={::this.handleRefund}
            >
              确定
            </Button>
            <Button
              type="button"
              onClick={::this.close}
              className="button cancel"
            >
              取消
            </Button>
          </div>
        </section>
      </Modal>
    )
  }
}

export default cssModule(FundInfoModal, styles, {allowMultiple: true})
