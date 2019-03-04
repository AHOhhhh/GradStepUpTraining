import React, {Component, PropTypes} from 'react'; // eslint-disable-line
import {Tabs, Radio, Row, Col} from 'antd'
import {includes} from 'lodash'
import cssModules from 'react-css-modules';
import styles from './index.module.scss';
import * as constants from '../../constants'
import * as maps from '../../maps'
import PaymentForm from '../../components/PaymentForm'

const pathToImages = require.context('../../assets');

const originalBankList = pathToImages.keys().map((key) => {
  return Object.assign({}, constants.BANK_LIST[key], {url: pathToImages(key)})
});


const RadioGroup = Radio.Group
const TabPane = Tabs.TabPane;

class BankList extends Component {
  static propTypes = {
    content: PropTypes.string,
    handlePaymentResult: PropTypes.func,
    availablePaymentMethod: PropTypes.array,
    payeeInfo: PropTypes.payeeInfo
  }

  state = {
    paymentMethod: this.props.availablePaymentMethod.some(method => method === constants.PAYMENT_METHOD.ONLINE) ? 'personal' : 'offline',
    cardType: 'debitCard',
    selectedBank: {
      personal_creditCard: 1,
      personal_debitCard: 1,
      enterprise_debitCard: 1
    }
  }

  selectBank(bank) {
    this.setState({
      selectedBank: Object.assign({}, this.state.selectedBank, {[`${this.state.paymentMethod}_${this.state.cardType}`]: bank.id})
    })
  }

  changePaymentMethod(e) {
    if (e === '.$enterprise') {
      this.setState({
        paymentMethod: 'enterprise',
        cardType: 'debitCard',
      })
    }
    if (e === '.$personal') {
      this.setState({
        paymentMethod: 'personal',
      })
    }

    if (e === '.$offline') {
      this.setState({
        paymentMethod: 'offline'
      })
    }
  }

  getPaymentMethod() {
    return this.state.paymentMethod
  }

  getSelectedCardType() {
    return `${this.state.paymentMethod}_${this.state.cardType}`
  }

  getSelectedBank() {
    return this.state.selectedBank[this.getSelectedCardType()]
  }

  selectPaymentMethod(e) {
    this.setState({
      cardType: e.target.value,
    })
  }

  formatBankList() {
    const bankListLength = maps.PAYMENT_MODE_MAP[`${this.state.paymentMethod}_${this.state.cardType}`] || 14
    return originalBankList.filter((bank) => bank.id <= bankListLength)
  }

  renderBankList() {
    const bankList = this.formatBankList();
    const selectedBank = this.state.selectedBank[`${this.state.paymentMethod}_${this.state.cardType}`]

    return (
      <Row gutter={50}>
        {bankList.map(bank => {
          return (
            <Col className="gutter-row" span={6}>
              <div className="box-bank">
                <div
                  className={'gutter-box'}
                  onClick={this.selectBank.bind(this, bank)}>
                  {selectedBank === bank.id && (<div className="selected"/>)}
                  <img src={bank.url} width="40px" height="40px"/>
                  <span>{bank.name}</span>
                </div>
              </div>
            </Col>
          )
        })}
      </Row>
    )
  }

  renderAccountInfo() {
    const {depositBank, collectionAccountName, collectionAccountNumber} = this.props.payeeInfo
    return (
      <ul styleName="beneficiary">
        <li><span styleName="beneficiary-key">开户行：</span><span>{depositBank}</span></li>
        <li><span styleName="beneficiary-key">账户名称：</span><span>{collectionAccountName}</span></li>
        <li><span styleName="beneficiary-key">账户号：</span><span>{collectionAccountNumber}</span></li>
      </ul>
    )
  }

  isDisplay(paymentMethod) {
    return this.props.availablePaymentMethod.some(method => paymentMethod === method)
  }

  getOfflineForm() {
    return this.offlineTab
  }

  render() {
    return (
      <div styleName="payment-bank">
        <div styleName="title">选择支付银行</div>
        <div styleName="tab">
          <Tabs
            defaultActiveKey={includes(this.props.availablePaymentMethod, constants.PAYMENT_METHOD.ONLINE) ? '.$personal' : '.$offline'}
            onTabClick={::this.changePaymentMethod}>
            {this.isDisplay(constants.PAYMENT_METHOD.ONLINE) && (<TabPane tab="个人支付" key="personal">
              <div styleName="card-type">
                <div styleName="selector">
                  <RadioGroup
                    name="personal" defaultValue="debitCard"
                    onChange={::this.selectPaymentMethod}>
                    <Radio value="debitCard" name="personal">借记卡网银支付</Radio>
                    <Radio value="creditCard" name="personal">信用卡网银支付</Radio>
                  </RadioGroup>
                </div>
              </div>
              <div styleName="bank-list">
                {this.renderBankList()}
              </div>
            </TabPane>)}

            {this.isDisplay(constants.PAYMENT_METHOD.ONLINE) && (<TabPane tab="企业支付" key="enterprise">
              <div styleName="card-type">
                <div styleName="selector">
                  <RadioGroup name="enterprise" defaultValue="enterpriseDebitCard">
                    <Radio value="enterpriseDebitCard" name="enterprise">借记卡网银支付</Radio>
                  </RadioGroup>
                </div>
              </div>

              <div styleName="bank-list">
                {this.renderBankList()}
              </div>
            </TabPane>)}

            {this.isDisplay(constants.PAYMENT_METHOD.OFFLINE) && (<TabPane tab="线下付款" key="offline">
              <Row gutter={16} styleName="offline-payment">
                <Col styleName="account-info" span={9}>
                  <h2 styleName="title">收款方信息</h2>
                  {this.renderAccountInfo()}
                </Col>
                <Col span={4}>
                  <div styleName="diving-line"/>
                </Col>
                <Col styleName="payment-info" span={11}>
                  <h2 styleName="payment-info-title title">支付信息</h2>
                  <PaymentForm
                    ref={ref => {
                      this.offlineTab = ref
                    }}
                  />
                </Col>
              </Row>
            </TabPane>)}

          </Tabs>
        </div>

      </div>

    )
  }
}

export default cssModules(BankList, styles, {allowMultiple: true});
