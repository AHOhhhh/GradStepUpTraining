import React, { Component, PropTypes } from 'react'
import _ from 'lodash'
import cssModules from 'react-css-modules'
import { connect } from 'react-redux';
import Row from 'antd/lib/row'
import Col from 'antd/lib/col'
import Form from 'antd/lib/form'
import Input from 'antd/lib/input'
import Select from 'antd/lib/select'
import { getProductPriceUnitOptions } from 'actions'
import styles from './index.module.scss'

import { validateNumLength } from './validators'

class ProductForm extends Component {
  static propTypes = {
    form: PropTypes.object,
    product: PropTypes.object,
    onSubmitCallback: PropTypes.func
  }

  componentDidMount() {
    // this.props.getProductPriceUnitOptions()
    if (this.props.product) {
      this.props.form.setFieldsValue({
        ...this.props.product
      })
    }
  }

  componentWillReceiveProps = (nextProps) => {
    if (!_.isEqual(nextProps.product, this.props.product)) {
      this.props.form.setFieldsValue({
        ...nextProps.product
      })
    }
  }

  handleSubmit = (e) => {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        if (this.props.product) {
          this.props.editProduct(values)
        } else {
          this.props.addProduct(values)
        }
        this.props.form.resetFields()
        this.props.onSubmitCallback()
      }
    })
  }

  setCurrencyCode = (value) => {
    const selectedPriceUnit = this.props.productPriceUnitOptions.filter(item => {
      return item.unit === value
    })[0]
    this.props.form.setFieldsValue({
      currencyCode: selectedPriceUnit.code
    })
  }

  onClose() {
    this.props.form.resetFields()
    this.props.onSubmitCallback()
  }

  render() {
    const formItemLayout = {
      labelCol: {
        xs: { span: 7 },
        sm: { span: 7 }
      },
      wrapperCol: {
        xs: { span: 17 },
        sm: { span: 17 }
      }
    }
    const FormItem = Form.Item
    const { Option } = Select

    const { TextArea } = Input
    const { getFieldDecorator } = this.props.form

    // const productPriceUnitOptions = this.props.productPriceUnitOptions.map(option => (
    //   <Option key={option.id} value={option.unit}>{option.name}</Option>
    // ))
    const defaultPriceUnit = 'CNY'

    return (
      <div styleName="product-form" ref={(ref) => { this.productForm = ref }}>
        <Form className="form-box" onSubmit={this.handleSubmit}>
          <FormItem
            {...formItemLayout}
            label="名称"
          >
            {getFieldDecorator('name', {
              rules: [
                { max: 50, message: '最多50个字符' },
                { required: true, message: '货品名称不能为空' }
              ]
            })(
              <Input />
            )}
          </FormItem>

          <FormItem
            {...formItemLayout}
            label="重量"
          >
            {getFieldDecorator('grossWeight', {
              rules: [
                { required: true, message: '重量不能为空' },
                validateNumLength(8, 2),
              ]
            })(
              <Input type="number" step=".01" />
            )}
            <span styleName="input-unit">Kg</span>
          </FormItem>

          <Row>
            <Col className="ant-form-item-label" span={7}>
              <label htmlFor="length" className="ant-form-item-required">体积</label>
            </Col>
            <Col span={5}>
              <FormItem>
                {getFieldDecorator('length', {
                  rules: [
                    { required: true, message: '长不能为空' },
                    validateNumLength(4),
                  ]
                })(
                  <Input type="number" step=".01" styleName="volume-input" placeholder="长" />
                )}
                <span styleName="input-unit">cm</span>
              </FormItem>
            </Col>
            <Col span={5}>
              <FormItem>
                {getFieldDecorator('width', {
                  rules: [
                    { required: true, message: '宽不能为空' },
                    validateNumLength(4),
                  ]
                })(
                  <Input type="number" step=".01" styleName="volume-input" placeholder="宽" />
                )}
                <span styleName="input-unit">cm</span>
              </FormItem>
            </Col>
            <Col span={5}>
              <FormItem>
                {getFieldDecorator('height', {
                  rules: [
                    { required: true, message: '高不能为空' },
                    validateNumLength(4),
                  ]
                })(
                  <Input type="number" step=".01" styleName="volume-input" placeholder="高" />
                )}
                <span styleName="input-unit">cm</span>
              </FormItem>
            </Col>
          </Row>

          <FormItem
            {...formItemLayout}
            label="单价"
          >
            {getFieldDecorator('unitPrice', {
              rules: [
                validateNumLength(8, 2),
              ]
            })(
              <Input type="number" step=".01" styleName="price-input" />
            )}
            {getFieldDecorator('currency', {
              initialValue: defaultPriceUnit
            })(
              <Select styleName="price-unit" getPopupContainer={() => this.productForm} onChange={this.setCurrencyCode}>
                {/* {productPriceUnitOptions} */}
                <Option key="1" value={defaultPriceUnit}>{defaultPriceUnit}</Option>
              </Select>
            )}
            {getFieldDecorator('currencyCode', {
              initialValue: '142'
            })(
              <Input styleName="hide" />
            )}
          </FormItem>

          <FormItem
            {...formItemLayout}
            label="总数"
          >
            {getFieldDecorator('totalAmount', {
              rules: [
                { required: true, message: '商品总数不能为空' },
                validateNumLength(8),
              ]
            })(
              <Input type="number" />
            )}
          </FormItem>

          <FormItem
            {...formItemLayout}
            label="包装数量"
          >
            {getFieldDecorator('packageAmount', {
              rules: [
                { required: true, message: '包装数量不能为空' },
                validateNumLength(8),
              ]
            })(
              <Input type="number" />
            )}
            <span styleName="input-unit">CTN</span>
          </FormItem>

          <FormItem
            {...formItemLayout}
            label="申报要素"
          >
            {getFieldDecorator('description', {
              rules: [
                { max: 100, message: '最多输入100个字符' }
              ]
            })(
              <TextArea rows={4} placeholder="在此处输入您申报产品的一些关键信息" />
            )}
          </FormItem>

          <div className="operate">
            <button type="submit" className="button primary" styleName="button">确定</button>
            <button type="button" className="button" styleName="button cancel" onClick={::this.onClose}>取消</button>
          </div>
        </Form>
      </div >
    )
  }
}

export default connect(
  state => ({
    productPriceUnitOptions: state.productList.productPriceUnitOptions,
  }),
  { getProductPriceUnitOptions }
)(Form.create()(cssModules(ProductForm, styles, { allowMultiple: true })))
