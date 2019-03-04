import React, { Component } from 'react'
import Modal from 'antd/lib/modal'
import Button from 'antd/lib/button'
import message from 'antd/lib/message'
import { connect } from 'react-redux'
import cssModules from 'react-css-modules'
import { cancelOrder } from './actions'
import styles from './index.module.scss'
import { canCancel } from './helpers/orderCancelHelper'
import constant from './constant'

const cssWrapper = Component => cssModules(Component, styles)

class CancelOrderButton extends Component {
  state = {
    visible: false
  }

  showCancelModal() {
    this.setState({ visible: true })
  }

  handleOrderCancel() {
    const { cancelOrder, order } = this.props
    cancelOrder(order)
      .catch(() => message.error('订单取消失败！'))
  }

  hideCancelModal() {
    this.setState({ visible: false })
  }
  renderBtn() {
    const CustomButton = this.props.children
    if (CustomButton) {
      return React.cloneElement(CustomButton, { onClick: () => this.showCancelModal()})
    }
    return (
      <div styleName="btn-container">
        <Button onClick={::this.showCancelModal}>取消订单</Button>
      </div>
    )
  }
  render() {
    const order = this.props.order
    const { id, orderType = '' } = order
    if (canCancel(order)) {
      return (
        <div>
          {this.renderBtn()}
          <Modal
            title="取消订单"
            visible={this.state.visible}
            maskClosable={false}
            footer={null}
            width={642}
            onCancel={::this.hideCancelModal}
          >
            <div styleName="modal-body">
              <p styleName="message">
                确定取消该笔
                <span>{constant[orderType]}</span>
                订单吗？订单取消后，将不可再恢复。
              </p>
              <p styleName="order-id">{`（订单号：${id}）`}</p>
              <div styleName="btn-group">
                <Button onClick={::this.handleOrderCancel}>确定</Button>
                <Button onClick={::this.hideCancelModal}>取消</Button>
              </div>
            </div>
          </Modal>
        </div>
      )
    }
    return null
  }
}

export default connect(null, { cancelOrder })(cssWrapper(CancelOrderButton))