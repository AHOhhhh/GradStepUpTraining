import React, {Component} from 'react'
import {connect} from 'react-redux'
import {Form, Input, Button, message} from 'antd'
import {bindActionCreators} from 'redux'
import cssModules from 'react-css-modules'
import * as action from './actions'

import styles from './index.module.scss'

const FormItem = Form.Item

class ResetPasswordForm extends Component {

  constructor(props) {
    super(props)
    this.state = {
      confirmPassword: ''
    }
  }

  checkNewPassword(rule, value, callback) {
    const form = this.props.form
    if (value && value !== form.getFieldValue('newPassword')) {
      callback('两次输入的密码不相同')
    } else {
      callback()
    }
  }

  handleSubmit(e) {
    e.preventDefault();

    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        this.props.actions.resetPassword(this.props.user.id, values)
          .then(() => {
            message.success('重置密码成功！')
            this.props.onComplete();

          })
          .catch(() => {
            message.error('重置密码失败！')
            this.props.onComplete();
          })
      }
    })
  }

  checkConfirmPassword(rule, value, callback) {
    const form = this.props.form

    if ((value !== this.state.confirmPassword) && (this.state.confirmPassword !== '')) {
      form.validateFields(['password'], {force: true})
    }
    callback()
  }

  liveConfirmPassword(e) {
    const value = e.target.value;
    this.setState({
      confirmPassword: value
    })
  }

  render() {

    const {getFieldDecorator} = this.props.form
    return (
      <div styleName="reset-password">
        <Form onSubmit={this.handleSubmit.bind(this)}>
          <FormItem
            label="输入新密码"
            labelCol={{span: 5, offset: 1}}
            wrapperCol={{span: 14}}>
            {getFieldDecorator('newPassword', {
              rules: [
                {min: 8, message: '新密码不能小于8位'},
                {pattern: /^(?![^a-zA-Z]+$)(?!\D+$)/, message: '必须包含字母, 数字'},
                {required: true, message: '请输入新密码'},
                {type: 'string'},
                {validator: this.checkConfirmPassword.bind(this)}]
            })(<Input type="password"/>)}
          </FormItem>
          <FormItem
            label="确认新密码"
            labelCol={{span: 5, offset: 1}}
            wrapperCol={{span: 14}}>
            {getFieldDecorator('password', {
              rules: [
                {validator: this.checkNewPassword.bind(this)},
                {required: true, message: '请确认新密码'},
                {type: 'string'}]
            })(<Input type="password" onBlur={this.liveConfirmPassword.bind(this)}/>)}
          </FormItem>
          <FormItem
            wrapperCol={{span: 16, offset: 6}}>
            <Button type="submit" htmlType="submit" styleName="submit">确定</Button>
            <Button styleName="cancelButton" onClick={this.props.onComplete}>取消</Button>
          </FormItem>
        </Form>
      </div>
    )
  }
}

export default connect(
  state => ({
    enterprise: state.enterpriseUser
  }),
  dispatch => ({
    actions: bindActionCreators(action, dispatch)
  })
)(Form.create()(cssModules(ResetPasswordForm, styles)))