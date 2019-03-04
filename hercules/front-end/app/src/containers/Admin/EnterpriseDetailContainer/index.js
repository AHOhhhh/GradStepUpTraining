import React, {Component} from 'react' // eslint-disable-line
import {browserHistory} from 'react-router' // eslint-disable-line
import cssModules from 'react-css-modules'
import {Breadcrumb} from 'components'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import * as action from './actions'
import styles from './index.module.scss'
import ReviewEnterpriseButton from '../share/components/ReviewEnterpriseButton';
import InfoTableGroup from '../share/components/InfoTableGroup'
import formatEnterpriseInfo from '../EnterpriseListContainer/Authorized/ViewEnterpriseInfoContainer/formatEnterpriseInfo'

class EnterpriseDetailContainer extends Component { // eslint-disable-line
  
  componentDidMount() {
    this.fetchInfo()
  }
  
  fetchInfo() {
    const id = this.props.params.id
    this.props.actions.getEnterpriseInfo(id)
    this.props.actions.getEnterpriseAdminByEnterpriseId(id)
  }
  
  openModal() {
    this.refs.reviewEnterpriseModal.showModal(this.props.enterpriseInfo)
  }
  
  handleCancel() {
    browserHistory.push('/admin/enterprise_list')
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
      currentPage: '企业审核'
    }
    
    const {enterpriseInfo, enterpriseAdmin} = this.props
    if (enterpriseInfo) {
      return (
        <div className="container enterprise">
          <div styleName="enterprise-detail-container">
            <div styleName="breadcrumb-layout">
              <Breadcrumb breadcrumb={breadcrumb}/>
            </div>
            <div className="enterprise-info" styleName="page-content-wrapper">
              <h2 styleName="page-title">企业审核</h2>
              <div styleName="enterprise-content" className="page-content">
                <h3 styleName="card-title">企业信息</h3>
                <InfoTableGroup
                  data={formatEnterpriseInfo(enterpriseAdmin, enterpriseInfo)}
                  enterpriseInfo={enterpriseInfo}
                  attachments
                    />
                <div styleName="operate">
                  <ReviewEnterpriseButton enterprise={enterpriseInfo} />
                  <button className="button" onClick={this.handleCancel}>取消</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      )
    }
    return null
  }
}

export default connect(
  state => ({
    ...state.admin
  }),
  dispatch => ({
    actions: bindActionCreators(
      action,
      dispatch
    )
  })
)(cssModules(EnterpriseDetailContainer, styles))