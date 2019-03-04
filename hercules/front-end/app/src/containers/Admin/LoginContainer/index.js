import React, {Component, PropTypes} from 'react'
import {guid, cookie} from 'utils'
import cssModules from 'react-css-modules'
import Form from 'antd/lib/form'
import Input from 'antd/lib/input'
import Button from 'antd/lib/button'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import * as action from 'actions'
import BASE_URL from '../../../config'
import styles from './index.module.scss'

class LoginContainer extends Component {

  static propTypes = {
    isAuthenticating: PropTypes.bool,
    captchaUuid: PropTypes.string
  }

  componentDidMount() {
    cookie.remove('TOKEN')
    window.localStorage.clear()
    this.getCaptcha()
  }

  getCaptcha() {
    this.props.actions.refreshCaptcha(guid())
  }

  handleSubmit = (e) => {
    e.preventDefault()
    this.props.form.validateFields((err, values) => {
      if (!err) {
        const params = Object.assign({}, values, {captchaId: this.props.captchaUuid})
        this.props.actions.platformUserLogin(params, null)
      }
    })
  }

  render() {
    const FormItem = Form.Item
    const {getFieldDecorator} = this.props.form
    return (
      <div styleName="login">
        <div className="container">
          <Form onSubmit={this.handleSubmit}>
            <h3>登录</h3>
            <FormItem>
              {getFieldDecorator('username', {
                rules: [{required: true, message: '请输入Hercules账号!'}]
              })(
                <Input placeholder="输入Hercules账号"/>,
              )}
            </FormItem>
            <FormItem>
              {getFieldDecorator('password', {
                rules: [{required: true, message: '请输入密码!'}],
              })(
                <Input type="password" placeholder="密码"/>
              )}
            </FormItem>
            <FormItem>
              {getFieldDecorator('captcha', {
                rules: [{required: true, message: '请输入验证码!'}]
              })(
                <Input placeholder="输入验证码" styleName="captcha-code"/>
              )}
              <img src={BASE_URL + '/captcha?captchaId=' + this.props.captchaUuid} styleName="captcha-image"/>
              <span onClick={::this.getCaptcha} styleName="captcha-refresh"/>
            </FormItem>
            <FormItem>
              <Button
                type="primary"
                htmlType="submit"
                className="button primary">
                登录
              </Button>
            </FormItem>
            <span styleName="error-message">{this.props.errorMessage}</span>
          </Form>
        </div>
      </div>
    )
  }
}

export default connect(
  state => ({
    captchaUuid: state.auth.captchaUuid,
    errorMessage: state.auth.errorMessage
  }),
  dispatch => ({actions: bindActionCreators(action, dispatch)})
)(Form.create()(cssModules(LoginContainer, styles, {allowMultiple: true})))