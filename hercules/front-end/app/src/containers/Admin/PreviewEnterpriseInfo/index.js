import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import {connect} from 'react-redux'
import {browserHistory} from 'react-router';
import {bindActionCreators} from 'redux'
import * as action from 'actions'

import {Button} from 'antd'
import InfoTableGroup from '../share/components/InfoTableGroup'
import styles from './index.module.scss'

import formatEnterpriseData from './formatEnterpriseInfo'

@connect(
  state => ({
    enterpriseInfo: state.enterpriseInfo.info,
    auth: state.auth,
    imageUrl: state.enterpriseInfo.imageUrl,
    enterpriseAdmin: state.admin.enterpriseAdmin,
    platformAdmin: state.auth.user
  }),
  dispatch => ({
    actions: bindActionCreators(
      action,
      dispatch
    )
  })
)
class ViewEnterpriseInfo extends Component { // eslint-disable-line
  componentDidMount() {
    const enterpriseId = this.props.params.id
    if (enterpriseId) {
      this.props.actions.getEnterpriseInfo(enterpriseId)
      this.props.actions.getEnterpriseAdminByEnterpriseId(enterpriseId)
    }
  }

  render() {

    const {enterpriseAdmin, enterpriseInfo} = this.props

    return (
      <div className="container">
        <div className={styles['display-info']}>
          <h1 className="title">查看企业信息</h1>
          <div className="enterprise-info">
            <h3 className="enterprise-title">企业信息</h3>
            <InfoTableGroup
              data={formatEnterpriseData(enterpriseAdmin, enterpriseInfo)}
              enterpriseInfo={enterpriseInfo}
              attachments/>
          </div>
          <div className="authorization-info">
            <Button onClick={browserHistory.goBack}>返回</Button>
          </div>
        </div>
      </div>
    )
  }
}

export default cssModules(ViewEnterpriseInfo, styles)