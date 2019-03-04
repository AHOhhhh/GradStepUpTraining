import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import {isEmpty} from 'lodash'
import Form from 'antd/lib/form'
import Input from 'antd/lib/input'
import message from 'antd/lib/message'
import * as action from 'actions'
import {bindActionCreators} from 'redux'
import {browserHistory} from 'react-router'
import {connect} from 'react-redux'
import {Section} from 'components'
import styles from './index.module.scss'
import Contact from '../../shared/Contact/index'
import {ORDER_CONFIG, ORDER_TYPE} from '../share/constants/order-config'

const FormItem = Form.Item

class CreateOrderContainer extends Component {

  constructor(props) {
    super(props)
    this.state = {
      isSelectedContact: true
    }
  }

  componentWillReceiveProps = (nextProps) => {
    if (nextProps.orderId) {
      this.props.actions.clearWMSOrder()
      browserHistory.push('/wms/orders/' + nextProps.orderId)
    }
  }

  componentDidMount() {
    this.props.actions.getWMSOrderByEnterprise({
      enterpriseId: this.props.enterpriseId,
      orderType: this.props.location.query.type || ORDER_TYPE.open
    })
    this.props.actions.getWMSOrderTemplate()
  }

  submit() {
    const defaultAddress = this.props.contact
    if (!isEmpty(defaultAddress)) {
      const type = this.props.location.query.type || ORDER_TYPE.open
      let order = {
        contact: {
          country: defaultAddress.country,
          province: defaultAddress.province,
          city: defaultAddress.city,
          district: defaultAddress.district,
          name: defaultAddress.name,
          address: defaultAddress.address,
          telephone: defaultAddress.telephone,
          cellphone: defaultAddress.cellphone,
        },
        type,
      }
      const approvedPrice = this.props.form.getFieldValue('approvedPrice')
      order = type !== ORDER_TYPE.open ? Object.assign({}, order, {currency: 'CNY', approvedPrice}) : order
      if (type === ORDER_TYPE.recharge) {
        this.props.form.validateFieldsAndScroll((err) => {
          if (!err) {
            this.props.actions.createWMSOrder(order)
          }
        })
      } else {
        this.props.actions.createWMSOrder(order)
      }
    } else {
      this.setState({isSelectedContact: false})
      message.error('请添加联系人')
    }
  }

  goBack() {
    history.back()
  }

  getOrderTypeDescription(orderType) {
    if (orderType) {
      if (ORDER_TYPE[orderType.toLowerCase()]) {
        return ORDER_CONFIG[orderType].title
      }
      browserHistory.push('/not_found')
    }
    return ORDER_CONFIG[ORDER_TYPE.open].title
  }

  getOrderContent(orderType, orderTemplate) {
    const {getFieldDecorator} = this.props.form
    if (orderType === ORDER_TYPE.recharge) {
      return (<div styleName="row">
        <div styleName="label price-label">充值金额：</div>
        <div styleName="text vertical-middle">
          <Form>
            <FormItem hasFeedback styleName="price-item">
              {getFieldDecorator('approvedPrice', {
                rules: [{
                  pattern: /^[0-9]*$/, message: '充值金额必须为整数'
                }, {
                  required: true, message: '请输入充值金额'
                }],
              })(
                <Input styleName="price-input"/>
              )}
            </FormItem>
          </Form>
          <div styleName="price-statement">充值金额必须为整数，比如50，100，200。</div>
        </div>
      </div>)
    }
    return (<div>
      <div styleName="row">
        <div styleName="label">服务期限：</div>
        <div styleName="text">{orderTemplate.duration}</div>
      </div>
      <div styleName="row has-tips">
        <div styleName="label">参考价格：</div>
        <div styleName="text">{orderTemplate.price}</div>
        <div styleName="tips-icon"/>
        <div styleName="tips-box">
          <div styleName="tips">
            <div styleName="tips-title"/>
            <div styleName="describe">{orderTemplate.priceTips}</div>
          </div>
        </div>
      </div>
      <div styleName="row">
        <div styleName="label">参考价格区间：</div>
        <div styleName="text">{orderTemplate.priceRegion}</div>
      </div>
    </div>)
  }

  render() {
    const orderTemplate = this.props.orderTemplate
    const orderType = this.props.location.query.type
    const orderTypeDescription = this.getOrderTypeDescription(orderType)
    return (
      <div className="container">
        <div styleName="create-order">
          <div styleName="title">填写订单信息</div>
          <Section title="订单内容">
            <div styleName="content-box">
              <div styleName="row">
                <div styleName="label">订单类型：</div>
                <div styleName="text">{orderTypeDescription}</div>
              </div>
              <div styleName="row">
                <div styleName="label">服务内容：</div>
                <div styleName="text">{orderTemplate.describe}</div>
              </div>
              {this.getOrderContent(orderType, orderTemplate)}
              <div styleName="row">
                <div styleName="label">服务商电话：</div>
                <div styleName="text">{orderTemplate.telephone}</div>
              </div>
            </div>
          </Section>

          <Section>
            <Contact/>
          </Section>

          <Section title="确认付款信息">
            <div styleName="content-box">
              <div styleName="row negotiable">
                <div styleName="label">应付总额：</div>
                <div styleName="text">
                  {orderType === ORDER_TYPE.recharge
                    ? this.props.form.getFieldValue('approvedPrice')
                    : orderTemplate.price}
                </div>
              </div>
            </div>
          </Section>
          <div styleName="row confirm-box">
            <button className="button" styleName="button" onClick={::this.goBack}>取消</button>
            <button className="button primary" styleName="button primary" onClick={::this.submit}>提交订单</button>
          </div>
        </div>
      </div>
    )
  }
}

export default connect(
  state => ({
    orderTemplate: state.wms.orderTemplate,
    orderId: state.wms.orderId,
    contact: state.contact.contact,
    enterpriseId: state.auth.enterpriseId
  }),
  dispatch => ({actions: bindActionCreators(action, dispatch)})
)(Form.create()(cssModules(CreateOrderContainer, styles, {allowMultiple: true})))
