import React, {Component} from 'react'
import {Link, browserHistory} from 'react-router'
import {connect} from 'react-redux'
import {SignStepper} from 'components'
import cssModules from 'react-css-modules'
import styles from './index.module.scss'

class SignUpSucceedContainer extends Component { // eslint-disable-line
  constructor(props) {
    super(props)
    this.state = {
      timer: 4
    }
  }

  componentDidMount() {
    this.timerID = setInterval(
      () => this.tick(),
      1000
    )
  }

  tick() {
    if (this.state.timer > 0) {
      this.setState({
        timer: this.state.timer - 1
      })
    } else {
      browserHistory.push('/')
      clearInterval(this.timerID)
    }
  }

  render() {
    return (<div className="container">
      <div styleName="signup-succeed">
        <div className="step-box" styleName="step-box">
          <SignStepper current={3}>
            <SignStepper.Step title="Step1" description="填写账户基本信息"/>
            <SignStepper.Step title="Step2" description="填写公司资质信息"/>
            <SignStepper.Step title="Step3" description="提交成功，等待审核"/>
          </SignStepper>
        </div>
        <div styleName="succeed-content">
          <div styleName="succeed-icon"/>
          <div styleName="succeed-info">
            <p>企业注册信息提交成功！</p>
          </div>
          <div styleName="user-guide">
            <p styleName="guide-links">
              您可以使用注册时的账户名称
              <span styleName="highlight-span">{this.props.user.name}</span>
              ，登录 <Link to="/enterprise_info" styleName="highlight-span link">查看审核状态。</Link>
            </p>
            <p styleName="timer"><span styleName="highlight-span">{this.state.timer}s</span>后，自动返回首页</p>
          </div>
        </div>
      </div>
    </div>)
  }
}

export default connect(
  state => ({
    user: state.auth.user
  })
)(cssModules(SignUpSucceedContainer, styles, {allowMultiple: true}))

