import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import {connect} from 'react-redux'
import {Link} from 'react-router';
import {bindActionCreators} from 'redux'
import {Button} from 'antd'
import {userIsAuthenticated} from 'utils'
import {Breadcrumb, Section} from 'components'
import * as action from '../../../../../actions'
import AuthorizationHistory from './AuthorizationHistory'
import AdminOperationHistory from './AdminOperationHistory'
import InfoTableGroup from '../../../share/components/InfoTableGroup'
import EnterprisePayMethods from './EnterprisePayMethods'
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

    const breadcrumb = {
      paths: [
        {
          item: '管理平台首页',
          url: '/admin/orders'
        },
        {
          item: '企业审核列表',
          url: '/admin/enterprise_list'
        }
      ],
      currentPage: '查看企业信息'
    }

    const {enterpriseAdmin, enterpriseInfo} = this.props
    const enterpriseId = this.props.params.id

    return (
      <div className="container">
        <div className={styles['display-info']}>
          <div className="breadcrumb-layout">
            <Breadcrumb breadcrumb={breadcrumb}/>
          </div>

          <h1 className="title">查看企业信息</h1>

          <Section title="企业信息">
            <div className="enterprise-info">
              <InfoTableGroup
                data={formatEnterpriseData(enterpriseAdmin, enterpriseInfo)}
                enterpriseInfo={enterpriseInfo}
                attachments/>
            </div>
          </Section>

          <Section title="审核历史记录">
            <div className="authorization-info">
              <div className="auth-table">
                <AuthorizationHistory enterpriseId={enterpriseId}/>
              </div>
            </div>
          </Section>

          <Section title="企业支付模式设置">
            <div className="payment-method-config">
              <EnterprisePayMethods enterpriseId={enterpriseId} payMethods={enterpriseInfo.payMethods}/>
            </div>
          </Section>

          <Section title="企业管理员操作记录">
            <AdminOperationHistory enterpriseId={enterpriseId}/>
          </Section>

          <div className="back-button">
            <Link to="/admin/enterprise_list"><Button>返回</Button></Link>
          </div>

        </div>
      </div>
    )
  }
}

export default userIsAuthenticated(cssModules(ViewEnterpriseInfo, styles))