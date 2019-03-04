import React, {Component, PropTypes} from 'react'; // eslint-disable-line
import {Modal} from 'antd'
import cssModules from 'react-css-modules';
import styles from './index.module.scss';


class OfflinePaymentConfirmModal extends Component {
  static propTypes = {
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
        footer={null}
        title="确认提交"
        onCancel={::this.close}
        wrapClassName="offline-payment-modal"
        maskClosable={false}
        height="300px"
        bodyStyle={{height: '250px'}}
      >
        <div className="content">确认提交线下支付信息？</div>
        <div styleName="operation">
          <button
            type="submit" styleName="button" className="button primary"
            onClick={this.props.handlePaymentResult}>
            确定
          </button>
          <button type="button" className="button" onClick={::this.close}>取消
          </button>
        </div>
      </Modal>
    )
  }
}

export default cssModules(OfflinePaymentConfirmModal, styles, {allowMultiple: true});
