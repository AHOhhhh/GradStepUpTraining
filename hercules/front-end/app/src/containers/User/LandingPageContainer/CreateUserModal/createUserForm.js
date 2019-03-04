import React, {Component, PropTypes} from 'react'
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import cssModules from 'react-css-modules'
import * as actions from 'actions'
import {USER_ROLE} from 'constants'
import message from 'antd/lib/message';
import Form from 'antd/lib/form'
import Input from 'antd/lib/input'
import styles from './index.module.scss'

const FormItem = Form.Item

class CreateUserForm extends Component {
  static propTypes = {
    form: PropTypes.object,
    onSubmitCallback: PropTypes.func
  }

  constructor(props) { // eslint-disable-line
    super(props)
    this.state = {
      confirmDirty: false
    }
  }

  handleConfirmBlur = (e) => {
    const value = e.target.value
    this.setState({confirmDirty: this.state.confirmDirty || !!value})
  }

  matchPassword = (rule, value, callback) => {
    const reg = /^(?![^a-zA-Z]+$)(?!\D+$)/
    if (value && !reg.test(value)) {
      callback('必须包含字母和数字')
    } else {
      callback()
    }
  }

  checkPassword = (rule, value, callback) => {
    const form = this.props.form
    if (value && value !== form.getFieldValue('password')) {
      callback('两次输入的密码不相同')
    } else {
      callback()
    }
  }

  checkConfirm = (rule, value, callback) => {
    const form = this.props.form
    if (value && this.state.confirmDirty) {
      form.validateFields(['confirm'], {force: true})
    }
    callback()
  }

  handleSubmit = (e) => {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        const role = USER_ROLE.enterpriseUser
        const enterpriseId = this.props.enterpriseId
        this.props.actions.createEnterpriseUser({...values, role, enterpriseId})
          .then(() => {
            this.props.form.resetFields()
            this.props.onSubmitCallback()
            message.success('用户创建成功！')
          })
          .catch((error) => {
            if (error.data.code === 30006 && !this.props.form.getFieldError('username')) {
              this.props.form.setFields({
                username: {
                  value: this.props.form.getFieldValue('username'),
                  errors: [new Error('该用户名称已被注册')]
                }
              })
            }
          })
      }
    })
  }

  onClose() {
    this.setState({confirmDirty: false})
    this.props.form.resetFields()
    this.props.onSubmitCallback()
  }

  render() {
    const {getFieldDecorator} = this.props.form
    return (
      <div styleName="createUserForm" className="signup">
        <Form styleName="form-box" onSubmit={this.handleSubmit}>
          <div styleName="row">
            <div styleName="title"><span styleName="required">用户名称：</span></div>
            <FormItem hasFeedback styleName="form-item">
              {getFieldDecorator('username', {
                rules: [{
                  min: 6, message: '用户名称不能小于6位'
                }, {
                  max: 18, message: '用户名称不能大于18位'
                }, {
                  pattern: /^[a-zA-Z0-9]*$/, message: '用户名称只能包含字母和数字'
                }, {
                  required: true, message: '请输入用户名称'
                }],
              })(
                <Input placeholder="您的账户名和登录名"/>
              )}
            </FormItem>
          </div>
          <div styleName="row">
            <div styleName="title"><span styleName="required">账户密码：</span></div>
            <FormItem hasFeedback styleName="form-item">
              {getFieldDecorator('password', {
                rules: [{
                  min: 8, message: '账户密码不能小于8位'
                }, {
                  validator: this.matchPassword,
                }, {
                  required: true, message: '请输入账户密码'
                }, {
                  validator: this.checkConfirm
                }],
              })(
                <Input type="password" placeholder="建议至少使用两种字符组合"/>
              )}
            </FormItem>
          </div>
          <div styleName="row">
            <div styleName="title"><span styleName="required">确认密码：</span></div>
            <FormItem hasFeedback styleName="form-item">
              {getFieldDecorator('confirm', {
                rules: [{
                  required: true, message: '请输入确认密码'
                }, {
                  validator: this.checkPassword
                }]
              })(
                <Input type="password" placeholder="再次输入密码" onBlur={this.handleConfirmBlur}/>
              )}
            </FormItem>
          </div>
          <div styleName="row">
            <div styleName="title">姓名：</div>
            <FormItem hasFeedback styleName="form-item">
              {getFieldDecorator('fullname', {
                rules: [{
                  max: 30, message: '姓名不能大于30位'
                }]
              })(
                <Input placeholder="联系人的真实姓名"/>
              )}
            </FormItem>
          </div>
          <div styleName="row">
            <div styleName="title">联系方式：</div>
            <div styleName="inline-box">
              <div styleName="row">
                <div styleName="title"><span styleName="required">手机：</span></div>
                <FormItem hasFeedback styleName="form-item">
                  {getFieldDecorator('cellphone', {
                    rules: [{
                      required: true, message: '请输入手机号码'
                    }, {
                      max: 11, message: '手机不能大于11位'
                    }, {
                      min: 11, message: '手机不能小于11位'
                    }]
                  })(
                    <Input/>
                  )}
                </FormItem>
              </div>
              <div styleName="no-margin">
                <div styleName="title">固定电话：</div>
                <FormItem hasFeedback styleName="form-item">
                  {getFieldDecorator('telephone', {
                    rules: [{
                      max: 20, message: '固定电话不能大于20位'
                    }]
                  })(
                    <Input/>
                  )}
                </FormItem>
              </div>
            </div>
          </div>
          <div styleName="row">
            <div styleName="title"><span styleName="required">邮箱：</span></div>
            <FormItem hasFeedback styleName="form-item">
              {getFieldDecorator('email', {
                rules: [{
                  type: 'email', message: '邮箱格式不正确'
                }, {
                  max: 50, message: '邮箱不能大于50位'
                }, {
                  required: true, message: '请输入邮箱'
                }]
              })(
                <Input placeholder="联系人的邮箱"/>
              )}
            </FormItem>
          </div>
          <div className="operate">
            <button type="submit" className="button primary">确认</button>
            <button type="button" className="button" onClick={::this.onClose}>取消</button>
          </div>
        </Form>
      </div>
    )
  }
}

export default Form.create()(connect(
  state => ({
    enterpriseId: state.auth.enterpriseId
  }),
  dispatch => ({actions: bindActionCreators(actions, dispatch)})
)(cssModules(CreateUserForm, styles)))