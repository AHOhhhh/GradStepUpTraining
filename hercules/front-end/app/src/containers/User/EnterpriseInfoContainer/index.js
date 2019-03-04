import React, {Component, PropTypes} from 'react' // eslint-disable-line
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import {result} from 'lodash'
import moment from 'moment'
import Tabs from 'antd/lib/tabs'
import Row from 'antd/lib/row'
import Col from 'antd/lib/col'
import {Link} from 'react-router'
import cssModule from 'react-css-modules'
import LeftNavigation from 'components/LeftNavigation'
import * as action from './actions'
import EnterpriseQualificationInfo from '../../shared/EnterpriseQualificationInfo'
import AccountInfo from './accountInfo/index'
import styles from './index.module.scss'
import statesMap from './verify-states-map'

const TabPane = Tabs.TabPane
const PERSONAL = 'personal'
const ENTERPRISE = 'enterprise'

class EnterpriseInfoContainer extends Component { // eslint-disable-line
  static propTypes = {
    actions: PropTypes.object.isRequired,
    enterpriseInfo: PropTypes.object,
    imageUrl: PropTypes.string,
    comment: PropTypes.string
  }

  constructor(props) { // eslint-disable-line
    super(props)
    this.state = {
      visible: false,
    }
  }

  componentDidMount() {
    const enterpriseId = this.props.auth.enterpriseId || this.props.auth.user.enterpriseId
    if (enterpriseId) {
      this.props.actions.getEnterpriseInfo(enterpriseId)
      this.props.actions.getEnterpriseHistory(enterpriseId)
    } 
  }

  componentWillUnmount() {
    this.props.actions.clearEnterpriseInfo()
  }

  getBackgroundClass(status) {
    switch (status) {
      case 'AuthorizationInProcess':
        return 'under-verified'
      case 'Authorized':
        return 'verified-succeed'
      case 'Unauthorized':
        return 'verified-failed'
      default:
        return 'non-verified'
    }
  }

  getComment(status) {
    return status === 'Unauthorized' ? (<div styleName="comment">
      <div styleName="label">审核意见：</div>
      <div styleName="comment-text">{this.props.comment}</div>
    </div>) : ''
  }

  reset(digit) {
    return `0${digit}`.slice(-2);
  }

  format(dateString) {
    return moment(dateString).format('YYYY-MM-DD HH:mm:ss')  // TODO: need to extract to common method
  }

  canSeeAttachment = () => {
    return result(this.props.auth, 'user.role') === 'EnterpriseAdmin'
  }

  changeTab(key) {
    this.setState({status: key})
  }

  render() {
    return (<div className="user-container">
      <Row>
        <Col span={4}>
          <LeftNavigation
            location={this.props.location.pathname}/>
        </Col>
        <Col span={20}>
          <div styleName="enterprise-container">
            <h2>信息查看</h2>
            <Tabs onChange={this.changeTab.bind(this)} type="card">
              <TabPane tab="企业信息" key={ENTERPRISE}>
                {this.renderEnterpriseInfo()}
              </TabPane>

              <TabPane tab="个人信息" key={PERSONAL}>
                <AccountInfo />
              </TabPane>
            </Tabs>
          </div>
        </Col>
      </Row>
    </div>)
  }

  renderEnterpriseInfo() {
    const enterpriseInfo = this.props.enterpriseInfo

    const status = enterpriseInfo.validationStatus
    const comment = this.getComment(status)
    const submitTime = this.props.enterpriseInfo.createdAt ? `提交时间： ${this.format(this.props.enterpriseInfo.createdAt)}` : ''
    const verifyState = status ? statesMap.find(item => item.status === status).character : '未认证'
    const statusBackgroudClass = this.getBackgroundClass(status)
    const reUploadInfo = status === 'Unauthorized' ?
      (<Link to={{pathname: '/signup_enterprise', state: {isEdit: true}}}>
        <span styleName="reupload">重新上传资料</span></Link>) : ''
    return (
      <div styleName="enterprise">
        <div styleName="enterprise-info">
          <div styleName="enterprise-content">
            <div styleName="relative">
              <div styleName="verify-state">
                <div>
                  <div styleName="label">企业认证状态：</div>
                  <div styleName={'state ' + statusBackgroudClass}>
                    <span styleName="diamond"/>
                    <span styleName="text">{verifyState}</span>
                  </div>
                  {reUploadInfo}
                </div>
                {comment}
              </div>
              <div styleName="submit-time">{submitTime}</div>
            </div>
            <div styleName="enterprise-qualification-container">
              <EnterpriseQualificationInfo
                user={this.props.user}
                showAttachment={this.canSeeAttachment()}
                enterpriseInfo={enterpriseInfo}
                imageUrl={this.props.imageUrl}
                getAttachmentImage={this.props.actions.getAttachmentImage}/>
            </div>
          </div>
        </div>
      </div>
    )
  }
}

export default connect(
  state => ({
    user: state.auth.user,
    enterpriseInfo: state.enterpriseInfo.info,
    auth: state.auth,
    imageUrl: state.enterpriseInfo.imageUrl,
    comment: state.enterpriseInfo.comment
  }),
  dispatch => ({
    actions: bindActionCreators(
      action,
      dispatch
    )
  })
)(cssModule(EnterpriseInfoContainer, styles, {allowMultiple: true}))