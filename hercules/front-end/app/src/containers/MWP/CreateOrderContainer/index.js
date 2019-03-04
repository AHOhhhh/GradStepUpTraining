import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import _ from 'lodash'
import moment from 'moment'
import Form from 'antd/lib/form'
import Select from 'antd/lib/select'
import Input from 'antd/lib/input'
import Checkbox from 'antd/lib/checkbox'
import Radio from 'antd/lib/radio'
import DatePicker from 'antd/lib/date-picker'
import message from 'antd/lib/message'
import {
  getPortOptions,
  getSupervisionOptions,
  getTransportOptions,
  getRelatedOrder,
  clearProductList,
  createWisePortOrder,
  getAirCargoOrder
} from 'actions'
import {connect} from 'react-redux'
import {browserHistory} from 'react-router'
import {Section, ProductList} from 'components'
import styles from './index.module.scss'
import Contact from '../../shared/Contact/index'
import {formatDefaultAddress} from './utils/formatter'

class CreateOrderContainer extends Component {
  constructor(props) {
    super(props)
    this.state = {
      isSelectedContact: true
    }
  }

  componentDidMount() {
    const {getPortOptions, getSupervisionOptions, getTransportOptions, clearProductList, getRelatedOrder, getAirCargoOrder} = this.props
    const referenceOrderId = this.getAcgOrderId()
    getPortOptions()
    clearProductList()
    getSupervisionOptions()
    getTransportOptions()
    getRelatedOrder(_.get(this.props, 'location.query', {}))
    if (referenceOrderId) {
      getAirCargoOrder(referenceOrderId)
    }
  }

  getAcgOrderId = () => _.get(this.props, 'location.query.acgOrderId', null)

  handleSubmit(e) {
    const {contact: defaultAddress, productList, createWisePortOrder, form, acgOrder} = this.props
    const fromAcgOrder = !!this.getAcgOrderId()

    e.preventDefault()
    if (_.isEmpty(productList)) {
      message.error('请添加至少一个货品')
      return
    }

    if (fromAcgOrder && acgOrder.referenceOrderId) {
      // alert('该航空货运订单已关联过智能报关服务')
      browserHistory.push(`/mwp/orders/${acgOrder.referenceOrderId}`)
    } else if (!_.isEmpty(defaultAddress)) {
      const order = formatDefaultAddress(defaultAddress)
      form.validateFieldsAndScroll((err, values) => {
        if (!err) {
          const products = _.cloneDeep(productList)
          products.forEach(product => {
            delete product.index
          })
          createWisePortOrder(
            Object.assign({},
              order,
              values,
              { goods: products },
              { orderValidationTime: moment(values.orderValidationTime).format('YYYY-MM-DD') },
              { currency: 'CNY' },
              { referenceOrderId: this.getAcgOrderId() }
            )
          )
        }
      })
    } else {
      this.setState({isSelectedContact: false})
      message.error('请添加联系人')
    }
  }

  goBack() {
    history.back()
  }

  disabledDate = current => current && moment(current.valueOf()).isBefore(moment(), 'day')

  renderTaxForm() {
    const formItemLayout = {
      labelCol: {
        xs: {span: 9},
        sm: {span: 9}
      },
      wrapperCol: {
        xs: {span: 15},
        sm: {span: 15}
      }
    }
    const FormItem = Form.Item
    const CheckboxGroup = Checkbox.Group
    const RadioGroup = Radio.Group
    const {Option} = Select
    const {getFieldDecorator} = this.props.form

    const servicesOptions = [
      {label: '报关/清关', value: 'Declaration_Clearance'},
      {label: '报检', value: 'Inspection_Declaration'},
      {label: '进出口代理', value: 'Agent'}
    ]

    const customsDeclarationTypeOptions = [
      {label: '进口', value: 'Import'},
      {label: '出口', value: 'Export'}
    ]

    const portOptions = this.props.portOptions.map(option => (
      <Option key={option.id} value={option.id}>{option.name}</Option>
    ))

    const supervisionOptions = this.props.supervisionOptions.map(option => (
      <Option key={option.id} value={option.id}>{option.name}</Option>
    ))

    const transportOptions = this.props.transportOptions.map(option => (
      <Option key={option.id} value={option.id}>{option.name}</Option>
    ))

    return (
      <div>
        <FormItem
          {...formItemLayout}
          label="服务项目"
        >
          {getFieldDecorator('services', {
            rules: [{
              required: true, message: '请选择服务项目'
            }]
          })(
            <CheckboxGroup options={servicesOptions}/>
          )}
        </FormItem>

        <FormItem
          {...formItemLayout}
          label="进出口类型"
        >
          {getFieldDecorator('customsDeclarationType', {
            initialValue: 'Import'
          }, {
            rules: [{
              required: true, message: '请选择进出口类型'
            }]
          })(
            <RadioGroup options={customsDeclarationTypeOptions}/>
          )}
        </FormItem>

        <FormItem
          {...formItemLayout}
          label="申报口岸"
        >
          {getFieldDecorator('portId', {
            rules: [{
              required: true, message: '请选择申报口岸'
            }]
          })(
            <Select
              showSearch
              styleName="select"
              filterOption={(input, option) => _.includes(option.props.children, input)}
            >
              {portOptions}
            </Select>
          )}
        </FormItem>

        <FormItem
          {...formItemLayout}
          label="监管方式"
        >
          {getFieldDecorator('supervisionId', {
            rules: [{
              required: true, message: '请选择监管方式'
            }]
          })(
            <Select styleName="select">
              {supervisionOptions}
            </Select>
          )}
        </FormItem>

        <FormItem
          {...formItemLayout}
          label="运输方式"
        >
          {getFieldDecorator('transportId', {
            rules: [{
              required: true, message: '请选择运输方式'
            }]
          })(
            <Select styleName="select">
              {transportOptions}
            </Select>
          )}
        </FormItem>

        <FormItem
          {...formItemLayout}
          label="需求时效"
        >
          {getFieldDecorator('orderValidationTime', {
            rules: [{
              required: true, message: '请选择需求时效'
            }]
          })(
            <DatePicker
              popupStyle={{ width: '340px' }}
              disabledDate={this.disabledDate}
            />
          )}
        </FormItem>

        <FormItem
          {...formItemLayout}
          label="预算金额"
        >
          {getFieldDecorator('budgetAmount', {
            rules: [
              {
                max: 9, message: '最多输入9位'
              }, {
                pattern: /^([1-9]\d*|0)(\.\d{1,2})?$/, message: '请输入小数点后两位的正数'
              }]
          })(
            <div>
              <Input type="number" step=".01" styleName="input"/>
              <span styleName="input-unit">元</span>
            </div>
          )}
        </FormItem>
      </div>
    )
  }

  render() {
    const fromAcgOrder = !!this.getAcgOrderId()

    return (
      <div styleName="create-order">
        <div styleName="title">填写订单信息</div>

        <div>
          <Form className="form-box" onSubmit={::this.handleSubmit}>

            <Section title="货品列表" hideCrossLine={true}>
              <ProductList fromOtherOrder={fromAcgOrder} />
            </Section>

            <Section title="关税信息填写">
              {this.renderTaxForm()}
            </Section>

            <Section>
              <Contact/>
            </Section>

            <div className="operate" styleName="operate">
              <button type="button" className="button" styleName="button" onClick={::this.goBack}>取消</button>
              <button type="submit" className="button primary" styleName="button primary">提交订单</button>
            </div>

          </Form>
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  productList: state.productList.list,
  contact: state.contact.contact,
  enterpriseId: state.auth.enterpriseId,
  portOptions: state.wisePortCreateOrder.portOptions,
  supervisionOptions: state.wisePortCreateOrder.supervisionOptions,
  transportOptions: state.wisePortCreateOrder.transportOptions,
  acgOrder: state.acg.order
})

const mapDispatchToProps = {
  clearProductList,
  getPortOptions,
  getSupervisionOptions,
  getTransportOptions,
  getRelatedOrder,
  createWisePortOrder,
  getAirCargoOrder
}

export default (connect(mapStateToProps, mapDispatchToProps))(Form.create()(cssModules(CreateOrderContainer, styles, {allowMultiple: true})))
