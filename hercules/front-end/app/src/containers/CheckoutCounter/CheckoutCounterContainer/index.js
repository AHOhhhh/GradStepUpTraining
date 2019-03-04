import React, {Component, PropTypes} from 'react'
import {connect} from 'react-redux'
import {Button} from 'antd';
import {isNull} from 'lodash'
import cssModule from 'react-css-modules'
import moment from 'moment'
import {bindActionCreators} from 'redux'
import currencyFormatter from 'currency-formatter'
import * as actions from '../../../actions'
import PaymentConfirmModal from '../../../components/PaymentConfirmModal'
import BankList from './components/BankList'
import OrderPriceTable from './components/OrderPriceTable'
import styles from './index.module.scss'
import * as constants from './constants'
import * as maps from './maps/index'
import SubmitForm from './components/SubmitForm/index'
import OfflinePaymentConfirmModal from './components/OfflinePaymentConfirmModal/index'

class CheckoutCounterContainer extends Component {
  static propTypes = {
    orderPrice: PropTypes.object,
    submitInfo: PropTypes.object,
    bankList: PropTypes.array
  }

  state = {
    paymentStatus: constants.UN_CHECK_OUT,
    offlinePaymentValue: {}
  }

  componentDidMount() {
    this.props.actions.getOrderPrice(this.props.params.orderId)
    this.props.actions.getPaymentInfo(this.props.params.orderType)
  }

  resetPaymentStatus() {
    this.setState({
      paymentStatus: constants.UN_CHECK_OUT
    })
  }
  getUserOrderList() {
    return [Object.assign({}, {orderId: this.props.orderPrice.orderId}, {totalPrice: currencyFormatter.format(this.props.orderPrice.amount, {code: 'CNY'})})]
  }

  getPaymentResult() {
    this.props.actions.handlePaymentStatus(this.props.params.orderId, this.props.submitInfo.paymentId, this.props.params.orderType)
      .then((status) => {
        if (status === constants.PAYMENT_STATUS.WAITING) {
          this.setState({
            paymentStatus: constants.CHECKING
          })
        } else {
          this.onlinePaymentConfirmModal.close()
        }
      })
  }

  getOnlinePaymentInfo() {
    const cardType = this.bankList.getSelectedCardType()
    const selectedBank = this.bankList.getSelectedBank()
    const bankList = constants.BANK_LIST;

    const submitInfo = {
      orderId: this.props.params.orderId,
      payAmount: this.props.orderPrice.amount,
      payType: maps.paymentTypeMap[cardType],
      currencyCode: 'CNY',
      businessType: this.props.params.orderType.toUpperCase(),
      payRequestIds: this.props.orderPrice.requestIds,
      payCustId: JSON.parse(localStorage.getItem('user')).id,
      payCustName: JSON.parse(localStorage.getItem('user')).username,
      payChannel: this.props.paymentInfo.find(payment => payment.payMethod === 'ONLINE').paymentAccount.payChannel,
      payMethod: 'ONLINE',
      orgCode: bankList[Object.keys(bankList).find(bank => bankList[bank].id === selectedBank)].abbreviation
    }

    return submitInfo
  }

  getOfflinePaymentInfo() {
    const offlinePaymentInfo = this.props.paymentInfo.find(payment => payment.payMethod === 'OFFLINE')
    this.bankList.getOfflineForm().validateFields((errors, values) => {
      let valueObj = {}
      if (isNull(errors)) {
        valueObj = {
          orderId: this.props.params.orderId,
          payRequestIds: this.props.orderPrice.requestIds,
          payAmount: this.props.orderPrice.amount,
          currencyCode: 'CNY',
          businessType: this.props.params.orderType.toUpperCase(),
          payCustId: JSON.parse(localStorage.getItem('user')).id,
          payCustName: JSON.parse(localStorage.getItem('user')).username,
          bankTransactionNumber: values.bankTransactionNumber,
          bankTransactionTime: values.paidTime ? moment(values.paidTime).format('YYYY-MM-DD') : null,
          payMethod: 'OFFLINE',
          payChannel: offlinePaymentInfo.paymentAccount.payChannel,
          bankTransactionComment: values.comment,
          depositBank: offlinePaymentInfo.paymentAccount.depositBank,
          collectionAccountName: offlinePaymentInfo.paymentAccount.collectionAccountName,
          collectionAccountNumber: offlinePaymentInfo.paymentAccount.collectionAccountNumber
        }
        this.setState({
          offlinePaymentValue: valueObj
        }, () => {
          this.offlinePaymentConfirmModal.open()
        })
      }
      return valueObj
    })
  }

  getPaymentMethod() {
    const paymentMethod = this.bankList.getPaymentMethod()
    return maps.paymentMethodMap[paymentMethod]
  }

  payForOfflineOrder() {
    this.props.actions.handleOfflinePayment(this.state.offlinePaymentValue, this.props.params.orderType, this.props.params.orderId)
    this.offlinePaymentConfirmModal.close()
    this.props.actions.handleSpin(true)
  }

  payForOnlineOrder() {
    window.open('', 'newPayment')
    this.props.actions.handleOrderPayment(this.orderForm, this.getOnlinePaymentInfo());
  }

  payForOrder() {
    const paymentMethod = this.getPaymentMethod()
    if (paymentMethod === constants.PAYMENT_METHOD.OFFLINE) {
      this.getOfflinePaymentInfo()
    } else {
      this.resetPaymentStatus()
      this.onlinePaymentConfirmModal.open()
      this.payForOnlineOrder()
    }
  }

  render() {
    const offlinePayment = this.props.paymentInfo && this.props.paymentInfo.find(payment => payment.payMethod === 'OFFLINE')
    const offlinePaymentInfo = offlinePayment ? offlinePayment.paymentAccount : {}
    return (
      <div className="wise-port-container">
        <div styleName="checkout-counter">
          <h1 styleName={'title'}>收银台</h1>
          <OrderPriceTable
            orderPrice={this.props.orderPrice}
          />

          {this.props.availablePaymentMethod.length > 0 && <BankList
            payeeInfo={offlinePaymentInfo}
            availablePaymentMethod={this.props.availablePaymentMethod}
            ref={(ref) => {
              this.bankList = ref
            }}
          />}

          <Button className="button primary" styleName="payment" onClick={::this.payForOrder}>付款</Button>
        </div>

        <SubmitForm
          submitInfo={this.props.submitInfo}
          ref={(ref) => {
            this.orderForm = ref
          }}/>

        <PaymentConfirmModal
          ref={(ref) => {
            this.onlinePaymentConfirmModal = ref
          }}
          content={maps.mapModalContent[this.state.paymentStatus]}
          paymentStatus={this.state.paymentStatus}
          handlePaymentResult={::this.getPaymentResult}
        />

        <OfflinePaymentConfirmModal
          ref={ref => {
            this.offlinePaymentConfirmModal = ref
          }}
          handlePaymentResult={::this.payForOfflineOrder}
        />
      </div>
    )
  }
}

export default connect(
  state => ({
    paymentInfo: state.checkoutCounter.paymentInfo,
    submitInfo: state.checkoutCounter.submitInfo,
    orderPrice: state.checkoutCounter.orderPrice,
    availablePaymentMethod: state.checkoutCounter.availablePaymentMethod
  }),
  dispatch => ({
    actions: bindActionCreators(actions, dispatch)
  })
)(cssModule(CheckoutCounterContainer, styles, {allowMultiple: true}))
