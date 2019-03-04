import React, {Component, PropTypes} from 'react'
import cssModules from 'react-css-modules'
import Form from 'antd/lib/form'
import Input from 'antd/lib/input'
import message from 'antd/lib/message'
import {trackPageview} from 'utils'
import {bindActionCreators} from 'redux'
import {browserHistory, Link} from 'react-router'
import {connect} from 'react-redux'
import _ from 'lodash'
import {SignStepper} from 'components'
import * as action from 'actions'
import styles from './index.module.scss'

const FormItem = Form.Item

const VALIDITION_MESSAGES = {
  inputUsername: '请输入用户名称',
  userNameCheck: '该用户名称已被注册',
  minUsername: '用户名称不能小于6位',
  MAX_USERNAME: '用户名称不能大于18位',
  usernameFormat: '用户名称只能包含字母和数字',
  inputPassword: '请输入账户密码',
  passwordFormat: '必须包含字母, 数字和特殊字符(~@#*_-[],{}:!?()）)',
  passwordLength: '账户密码不能小于8位',
  passwordConfirm: '请输入确认密码',
  passwordConfirmCheck: '两次输入的密码不相同',
  inputFullName: '请输入姓名',
  fullNameLength: '姓名不能大于30位',
  inputCellphone: '请输入手机号',
  cellphoneFormat: '手机号码必须为数字，且必须为11位',
  telephoneFormat: '固定电话必须为数字或-',
  inputEmail: '请输入邮箱',
  emailLength: '邮箱不能大于50位',
  telephoneLength: '固定电话不能大于20位',
  emailCheck: '邮箱格式不正确',
}

class SignUpContainer extends Component {
  static propTypes = {
    form: PropTypes.object
  }

  state = {
    confirmDirty: false,
  }

  componentDidMount() {
    trackPageview('/signup')
  }

  componentWillReceiveProps = (nextProps) => {
    if (nextProps.signupUserName) {
      this.props.actions.clearSignupData()
      browserHistory.push('/signup_enterprise')
    }
  }
  componentDidUpdate = () => {
    if (this.props.signupErrorCode === 30006 && !this.props.form.getFieldError('username')) {
      this.props.form.setFields({
        username: {
          value: this.props.form.getFieldValue('username'),
          errors: [new Error(VALIDITION_MESSAGES.userNameCheck)]
        }
      })
      this.props.actions.clearSignupData()
    }
  }
  handleConfirmBlur = (e) => {
    const value = e.target.value
    this.setState({confirmDirty: this.state.confirmDirty || !!value})
  }
  matchPassword = (rule, value, callback) => {
    const reg = /^(?![^a-zA-Z]+$)(?!\D+$)/
    const reg2 = /[\~\@\#\*\_\-\[\]\,\{\}\:\!\?\(\)]+/ //eslint-disable-line
    if (value && !(reg.test(value) && reg2.test(value))) {
      callback(VALIDITION_MESSAGES.passwordFormat)
    } else {
      callback()
    }
  }
  checkPassword = (rule, value, callback) => {
    const form = this.props.form
    if (value && value !== form.getFieldValue('password')) {
      callback(VALIDITION_MESSAGES.passwordConfirmCheck)
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
  checkCellphone = (rule, value, callback) => {
    const regCellphone = /^[0-9]{11}$/;
    if (value && !regCellphone.test(value)) {
      callback(VALIDITION_MESSAGES.cellphoneFormat)
    }
    callback()

  }
  checkTelephone = (rule, value, callback) => {
    const regTelephone = /^\d+-*\d+$/
    if (value && !regTelephone.test(value)) {
      callback(VALIDITION_MESSAGES.telephoneFormat)
    }
    callback()
  }
  handleSubmit = (e) => {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        if (values.telephone || values.cellphone) {
          const newValues = _.omitBy({...values}, (value) => (!value))
          this.props.actions.signupUser(this.trimValues(newValues))
        } else {
          message.error('手机号和固定电话请至少填写一项')
        }
      }
    })
  }
  trimValues = (obj) => {
    Object.keys(obj).forEach(key => {
      obj[key] = typeof obj[key] === 'string' ? obj[key].trim() : obj[key];
    });
    return obj
  }

  render() {
    const {getFieldDecorator} = this.props.form
    return (
      <div className="container signup-form">
        <div styleName="signup">
          <div styleName="step-box">
            <SignStepper current={0}>
              <SignStepper.Step title="Step1" description="填写账户基本信息"/>
              <SignStepper.Step title="Step2" description="填写公司资质信息"/>
              <SignStepper.Step title="Step3" description="提交成功，等待审核"/>
            </SignStepper>
          </div>
          <Form styleName="form-box" onSubmit={this.handleSubmit}>
            <div styleName="box-title">填写账户基本信息</div>
            <hr/>
            <div styleName="row">
              <div styleName="title"><span styleName="required-icon">*</span>用户名称：</div>
              <FormItem hasFeedback>
                {getFieldDecorator('username', {
                  rules: [{
                    min: 6, message: VALIDITION_MESSAGES.minUsername
                  }, {
                    max: 18, message: VALIDITION_MESSAGES.MAX_USERNAME
                  }, {
                    pattern: /^[a-zA-Z0-9]*$/, message: VALIDITION_MESSAGES.usernameFormat
                  }, {
                    required: true, message: VALIDITION_MESSAGES.inputUsername
                  }],
                })(
                  <Input placeholder="您的账户名和登录名"/>
                )}
              </FormItem>
            </div>
            <div styleName="row">
              <div styleName="title"><span styleName="required-icon">*</span>账户密码：</div>
              <FormItem hasFeedback>
                {getFieldDecorator('password', {
                  rules: [{
                    min: 8, message: VALIDITION_MESSAGES.passwordLength
                  }, {
                    validator: this.matchPassword,
                  }, {
                    required: true, message: VALIDITION_MESSAGES.inputPassword
                  }, {
                    validator: this.checkConfirm
                  }],
                })(
                  <Input type="password" placeholder="支持字母、数字、特殊字符(~@#*_-[],{}:!?()）)"/>
                )}
              </FormItem>
            </div>
            <div styleName="row">
              <div styleName="title"><span styleName="required-icon">*</span>确认密码：</div>
              <FormItem hasFeedback>
                {getFieldDecorator('confirm', {
                  rules: [{
                    required: true, message: VALIDITION_MESSAGES.passwordConfirm
                  }, {
                    validator: this.checkPassword
                  }]
                })(
                  <Input type="password" placeholder="再次输入密码" onBlur={this.handleConfirmBlur}/>
                )}
              </FormItem>
            </div>
            <div styleName="row">
              <div styleName="title"><span styleName="required-icon">*</span>姓名：</div>
              <FormItem hasFeedback>
                {getFieldDecorator('fullname', {
                  rules: [{
                    required: true, message: VALIDITION_MESSAGES.inputFullName
                  }, {
                    max: 30, message: VALIDITION_MESSAGES.fullNameLength
                  }]
                })(
                  <Input placeholder="联系人的真实姓名"/>
                )}
              </FormItem>
            </div>
            <div styleName="row">
              <div styleName="title"><span styleName="required-icon">*</span>联系方式：</div>
              <div styleName="inline-box">
                <div styleName="row">
                  <div styleName="title">手机：</div>
                  <FormItem hasFeedback>
                    {getFieldDecorator('cellphone', {
                      rules: [{
                        validator: this.checkCellphone,
                      }, {
                        max: 11, message: VALIDITION_MESSAGES.inputCellphone
                      }]
                    })(
                      <Input/>
                    )}
                  </FormItem>
                </div>
                <div styleName="row">
                  <div styleName="title">固定电话：</div>
                  <FormItem>
                    {getFieldDecorator('telephone', {
                      rules: [{
                        validator: this.checkTelephone
                      }, {
                        max: 20, message: VALIDITION_MESSAGES.telephoneLength
                      }]
                    })(
                      <Input/>
                    )}
                  </FormItem>
                </div>
              </div>
            </div>
            <div styleName="row">
              <div styleName="title"><span styleName="required-icon">*</span>邮箱：</div>
              <FormItem hasFeedback>
                {getFieldDecorator('email', {
                  rules: [{
                    type: 'email', message: VALIDITION_MESSAGES.emailCheck
                  }, {
                    max: 50, message: VALIDITION_MESSAGES.emailLength
                  }, {
                    required: true, message: VALIDITION_MESSAGES.inputEmail
                  }]
                })(
                  <Input placeholder="联系人的邮箱"/>
                )}
              </FormItem>
            </div>
            <div styleName="operate">
              <button type="submit" styleName="button" className="button primary">下一步</button>
              <Link to="/login">
                <button type="button" styleName="button" className="button">取消</button>
              </Link>
            </div>
          </Form>
        </div>
      </div>
    )
  }
}

export default connect(
  state => ({
    signupUserName: state.auth.signupUserName,
    signupErrorCode: state.auth.signupErrorCode
  }),
  dispatch => ({actions: bindActionCreators(action, dispatch)})
)(Form.create()(cssModules(SignUpContainer, styles, {allowMultiple: true})))