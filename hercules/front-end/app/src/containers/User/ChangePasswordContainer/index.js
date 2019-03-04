import React, {Component} from 'react';
import * as actions from 'actions'
import {connect} from 'react-redux'
import {message} from 'antd';
import {guid} from 'utils'
import cssModule from 'react-css-modules'
import {bindActionCreators} from 'redux'
import Modal from 'antd/lib/modal';
import {SignStepper} from 'components'
import {steps} from './maps';
import ChangePasswordForm from './ChangePasswordForm';
import styles from './index.module.scss';

class ChangePasswordContainer extends Component {
  state = {
    timer: 5,
    errorMessage: null,
    isSucceed: false
  }

  componentDidUpdate() {
    if (this.state.isSucceed && (this.timerID === null || this.timerID === undefined)) {
      this.timerID = setInterval(::this.tick, 1000)
    }
  }

  componentDidMount() {
    if (!this.props.user.resettable && !this.changePasswordTipsModal) {
      this.renderChangePasswordTipsModal()
    }

    this.props.actions.refreshCaptcha(guid())
  }

  changPassword(value) {
    this.props.actions.changePassword(this.props.user, value)
      .then((data) => {
        if (data.isSucceed) {
          message.success('密码修改成功！')
        }
        if (!data.isSucceed) {
          this.getCaptcha()
        }
        const valueObj = {...this.state, ...data}
        this.setState(valueObj)
      })
  }

  componentWillUnmount() {
    clearInterval(this.timerID)
    if (this.changePasswordTipsModal) {
      this.changePasswordTipsModal.destroy()
    }
  }

  tick() {
    if (this.state.timer > 0) {
      this.setState({
        timer: this.state.timer - 1
      })
    } else {
      clearInterval(this.timerID)
      this.timerID = null
      this.props.actions.jumpHome()
    }
  }

  getCaptcha() {
    this.props.actions.refreshCaptcha(guid())
  }

  renderSuccessTip() {
    return (<div styleName="succeed-content">
      <div styleName="succeed-icon"/>
      <div styleName="succeed-info">
        <p>密码修改成功！</p>
      </div>
      <div styleName="timer-guide">
        <p styleName="timer"><span styleName="highlight-span">{this.state.timer}s</span>后，自动返回首页</p>
      </div>
    </div>)
  }

  renderChangePasswordTipsModal() {
    this.changePasswordTipsModal = Modal.warning({
      iconType: null,
      title: '您是第一次登录系统，请先进行密码修改吧！',
    })
  }

  render() {
    return (
      <div className="container">
        <div styleName="change-password">
          <div styleName="change-password-header">
            <h3 styleName="change-password-title">修改密码</h3>
          </div>
          <div styleName="step-box">
            <SignStepper current={steps[this.state.isSucceed]}>
              <SignStepper.Step title="Step1" description="修改登录密码"/>
              <SignStepper.Step title="Step2" description="完成"/>
            </SignStepper>
          </div>
          {this.state.isSucceed ? this.renderSuccessTip() :
          <ChangePasswordForm
            submit={this.changPassword.bind(this)}
            errorMessage={this.state.errorMessage}
            captchaUuid={this.props.captchaUuid}
            refreshCaptcha={this.getCaptcha.bind(this)}
          />
          }
        </div>
      </div>
    );
  }
}

export default connect(
  state => ({
    userId: state.auth.user.id,
    user: state.auth.user,
    captchaUuid: state.auth.captchaUuid
  }),
  dispatch => ({actions: bindActionCreators(actions, dispatch)})
)(cssModule(ChangePasswordContainer, styles, {allowMultiple: true}))
