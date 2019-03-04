import React, {Component} from 'react' // eslint-disable-line
import cssModules from 'react-css-modules'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import Tabs from 'antd/lib/tabs'
import Row from 'antd/lib/row'
import Col from 'antd/lib/col'
import LeftNavigation from 'components/LeftNavigation'
import AuthorizationInProcess from './AuthorizationInProcess'
import Authorized from './Authorized'
import styles from './index.module.scss'
import * as action from './actions'

const TabPane = Tabs.TabPane
const AUTHORIZED = 'Authorized'
const AUTHORIZATION_IN_PROCESS = 'AuthorizationInProcess'

class EnterpriseListContainer extends Component { // eslint-disable-line

  changeTab(key) {
    this.props.actions.setEnterpriseTabsKey(key)
  }

  render() {
    const activeKey = this.props.enterpriseTabsKey || '.$' + AUTHORIZED

    return (<div className="admin-container" styleName="enterprise-list-container">
      <Row>
        <Col span={4}>
          <LeftNavigation
            location={this.props.location.pathname}/>
        </Col>
        <Col span={20}>
          <div styleName="card-container">
            <h2>企业审核管理</h2>
            <Tabs onChange={this.changeTab.bind(this)} type="card" defaultActiveKey={activeKey}>
              <TabPane tab="待审核企业" key={AUTHORIZED}>
                <AuthorizationInProcess/>
              </TabPane>
              <TabPane tab="已审核企业" key={AUTHORIZATION_IN_PROCESS}>
                <Authorized/>
              </TabPane>
            </Tabs>
          </div>
        </Col>
      </Row>
    </div>)
  }
}

export default connect(
  state => ({
    enterpriseTabsKey: state.admin.enterpriseTabsKey
  }),
  dispatch => ({actions: bindActionCreators(action, dispatch)})
)(cssModules(EnterpriseListContainer, styles))