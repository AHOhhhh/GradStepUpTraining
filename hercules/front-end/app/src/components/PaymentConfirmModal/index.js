import React, {Component, PropTypes} from 'react'; // eslint-disable-line
import {Modal} from 'antd'
import cssModules from 'react-css-modules';
import styles from './index.module.scss';


class PaymentConfirmModal extends Component {
  static propTypes = {
    content: PropTypes.string,
    handlePaymentResult: PropTypes.func
  }

  state = {
    visible: false
  }

  open() {
    this.setState({
      visible: true
    })
  }

  close() {
    this.setState({
      visible: false
    })
  }

  render() {
    return (
      <Modal
        visible={this.state.visible}
        wrapClassName="price-chang-tips-modal"
      >
        <div className="content">{this.props.content}</div>
        {this.props.paymentStatus === 'checking' ?
          <button
            type="submit" styleName="button" className="button payment-success"
            onClick={::this.close}>
            确定
          </button>
          : <div>
            <button
              type="submit" styleName="button" className="button payment-success"
              onClick={this.props.handlePaymentResult}>
              支付完成
            </button>
            <button type="button" className="button payment-failure" onClick={this.props.handlePaymentResult}>遇到问题
            </button>
          </div>
        }
      </Modal>
    )
  }
}

export default cssModules(PaymentConfirmModal, styles, {allowMultiple: true});
