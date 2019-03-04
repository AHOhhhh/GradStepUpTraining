import React, {Component, PropTypes} from 'react';
import {Form, Input} from 'antd';
import BASE_URL from '../../../../config'
import * as constants from '../constants'
import styles from './index.module.scss';

const FormItem = Form.Item;

class ChangePasswordForm extends Component {
  static propTypes = {
    submit: PropTypes.func.isRequired,
    errorMessage: PropTypes.string.isRequired,
    captchaUuid: PropTypes.string.isRequired
  }

  state = {
    confirmDirty: false
  }

  handleConfirmBlur = (e) => {
    const value = e.target.value
    this.setState({confirmDirty: this.state.confirmDirty || !!value})
  }

  checkPassword = (rule, value, callback) => {
    const form = this.props.form
    if (value && value !== form.getFieldValue('password')) {
      callback(constants.VALIDATION_MESSAGES.passwordConfirmCheck)
    } else {
      callback()
    }
  }


  checkConfirm(rule, value, callback) {
    const form = this.props.form
    if (value && this.state.confirmDirty) {
      form.validateFields(['confirm'], {force: true})
    }
    callback()
  }

  handleSubmit(e) {
    e.preventDefault()
    this.props.form.validateFields((errors, values) => {
      if (!errors) {
        const resultObj = {
          password: values.password,
          originalPassword: values.oldPassword,
          captcha: values.captcha,
          captchaId: this.props.captchaUuid
        }
        this.props.submit(resultObj);
      }
    })
  }

  matchPassword = (rule, value, callback) => {
    const reg = /^(?![^a-zA-Z]+$)(?!\D+$)/
    const reg2 = /[\~\@\#\*\_\-\[\]\,\{\}\:\!\?\(\)]+/ //eslint-disable-line
    if (value && !(reg.test(value) && reg2.test(value))) {
      callback(constants.VALIDATION_MESSAGES.passwordFormat)
    } else {
      callback()
    }
  }


  render() {
    const {getFieldDecorator} = this.props.form

    return (
      <div className={styles['form-box']}>
        <Form onSubmit={this.handleSubmit.bind(this)}>
          <div className="row">
            <div className="title">旧密码：</div>
            <FormItem hasFeedback className="form-item">
              {getFieldDecorator('oldPassword', {
                rules: [{
                  min: 8, message: constants.VALIDATION_MESSAGES.passwordLength
                }, {
                  required: true, message: constants.VALIDATION_MESSAGES.inputOldPassword
                }],
              })(
                <Input type="password" placeholder={constants.VALIDATION_MESSAGES.inputOldPassword}/>
              )}
            </FormItem>
          </div>
          <div className="row">
            <div className="title">新密码：</div>
            <FormItem hasFeedback className="form-item">
              {getFieldDecorator('password', {
                rules: [{
                  min: 8, message: constants.VALIDATION_MESSAGES.passwordLength
                }, {
                  validator: this.matchPassword.bind(this)
                }, {
                  validator: this.checkConfirm.bind(this)
                }, {
                  required: true, message: constants.VALIDATION_MESSAGES.passwordFormat
                }],
              })(
                <Input type="password" placeholder={constants.VALIDATION_MESSAGES.inputPassword}/>
              )}
            </FormItem>
          </div>
          <div className="row">
            <div className="title">确认密码：</div>
            <FormItem hasFeedback className="form-item">
              {getFieldDecorator('confirm', {
                rules: [{
                  required: true, message: constants.VALIDATION_MESSAGES.passwordConfirm
                }, {
                  validator: this.checkPassword.bind(this)
                }]
              })(
                <Input type="password" placeholder="再次输入密码" onBlur={this.handleConfirmBlur}/>
              )}
            </FormItem>
          </div>
          <div className="row">
            <div className="title">输入验证码：</div>
            <FormItem className="form-item">
              {getFieldDecorator('captcha', {
                rules: [{required: true, message: '请输入验证码!'}]
              })(
                <Input placeholder="输入验证码" className="captcha-code"/>
              )}
              <img src={BASE_URL + '/captcha?captchaId=' + this.props.captchaUuid} className="captcha-image"/>
              <span className="captcha-refresh" onClick={this.props.refreshCaptcha}/>
            </FormItem>
          </div>
          <div className="row">
            <div className="error-message">{this.props.errorMessage}</div>
          </div>
          <div className="operate">
            <button type="submit" className="button primary">提交</button>
          </div>
        </Form>
      </div>
    );
  }
}

export default Form.create()(ChangePasswordForm);
