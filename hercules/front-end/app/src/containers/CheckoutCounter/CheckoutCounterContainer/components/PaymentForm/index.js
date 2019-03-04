import React, {Component} from 'react';
import {Form, Input, DatePicker} from 'antd';
import cssModule from 'react-css-modules'
import * as constants from '../../constants'
import styles from './index.module.scss';

const FormItem = Form.Item;
const TextArea = Input.TextArea;

class PaymentForm extends Component {
  componentDidMount() {

  }

  render() {
    const {getFieldDecorator} = this.props.form

    return (
      <div styleName="form-box">
        <Form>
          <div styleName="row">
            <div styleName="title">
              <icon styleName="icon">*</icon>
              支付流水账号：
            </div>
            <FormItem styleName="form-item">
              {getFieldDecorator('bankTransactionNumber', {
                rules: [{required: true, message: constants.PAYMENT_MESSAGE.paymentAccount}, {
                  max: 36,
                  message: constants.PAYMENT_MESSAGE.paymentAccountNumberMaxLengthErrorMessage
                }],
              })(
                <Input />
              )}
            </FormItem>
          </div>

          <div styleName="row">
            <div styleName="title">
              支付日期：
            </div>
            <FormItem styleName="form-item">
              {getFieldDecorator('paidTime')(
                <DatePicker />
              )}
            </FormItem>
          </div>

          <div styleName="row" className="payment-info-textarea">
            <div styleName="title">备注：</div>
            <FormItem styleName="form-item">
              {getFieldDecorator('comment')(
                <TextArea />
              )}
            </FormItem>
          </div>
        </Form>
      </div>
    );
  }
}

export default Form.create()(cssModule(PaymentForm, styles, {allowMultiple: true}));
