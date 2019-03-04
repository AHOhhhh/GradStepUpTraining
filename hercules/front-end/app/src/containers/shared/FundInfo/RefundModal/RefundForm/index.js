import React, {Component} from 'react';
import {Form, Input, Select, Icon} from 'antd';
import cssModule from 'react-css-modules'
import {includes} from 'lodash'
import styles from './index.module.scss';
import {REFUND_MESSAGE, REFUND_ERROR_MESSAGE, ENABLE_SHOW_ORDER_STOP_WARNING} from './contants'

const FormItem = Form.Item;
const TextArea = Input.TextArea;
const {Option} = Select

class RefundForm extends Component {

  validateRefundAmount(rule, value, callback) {
    const reg = new RegExp('^[0-9]*$');
    const newReg = new RegExp('^\\d+(\\.\\d+)?$')
    const amount = parseInt(value)
    if ((newReg.test(value) || reg.test(value)) && amount >= 0) {
      return callback()
    }

    return callback(REFUND_ERROR_MESSAGE.refundAmountNumber)
  }

  isDisplayStopOrderServiceWarning() {
    if (this.props.order.status === 'Paid' && this.props.order.orderType === 'acg') {
      return true
    }
    return includes(ENABLE_SHOW_ORDER_STOP_WARNING, this.props.order.status)
  }

  render() {
    const {getFieldDecorator} = this.props.form

    return (
      <div styleName="form-box">
        <Form>
          <div styleName="row">
            <div styleName="title">
              <icon styleName="icon">*</icon>
              金额：
            </div>
            <FormItem styleName="form-item">
              {getFieldDecorator('refundAmount', {
                rules: [{required: true, message: REFUND_ERROR_MESSAGE.refundAmountRequired},
                  this.validateRefundAmount
                ],
              })(
                <Input placeholder={REFUND_MESSAGE.refundAmount}/>
              )}
            </FormItem>
          </div>

          <div styleName="row">
            <div styleName="title">
              <icon styleName="icon">*</icon>
              退款方式：
            </div>
            <FormItem styleName="form-item">
              {getFieldDecorator('refundMethod', {
                rules: [{required: true, message: REFUND_ERROR_MESSAGE.refundMethodRequired}],
              })(
                <Select initialValue="offline">
                  <Option key={0} value="OFFLINE">线下</Option>
                </Select>
              )}
            </FormItem>
          </div>

          <div styleName="row" className="payment-info-textarea">
            <div styleName="title">
              <icon styleName="icon">*</icon>
              备注信息：
            </div>
            <FormItem styleName="form-item">
              {getFieldDecorator('comments', {
                rules: [{required: true, message: REFUND_ERROR_MESSAGE.commentRequired}, {
                  max: 50,
                  message: REFUND_ERROR_MESSAGE.commentLength
                }],
              })(
                <TextArea />
              )}
            </FormItem>
          </div>
        </Form>
        {this.isDisplayStopOrderServiceWarning() && <div styleName="warning-msg">
          <Icon type="info-circle" style={{fontSize: 16, color: '#4f4fdf'}}/>
          <span>订单正在服务中，若在当前状态下记录退款，订单将被中止</span>
        </div>}
      </div>
    );
  }
}

export default Form.create()(cssModule(RefundForm, styles, {allowMultiple: true}));
