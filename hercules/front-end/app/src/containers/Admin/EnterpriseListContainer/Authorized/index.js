import React, {Component} from 'react' // eslint-disable-line
import * as action from 'actions'
import {bindActionCreators} from 'redux'
import cssModules from 'react-css-modules'
import {Input, Select} from 'antd'
import {connect} from 'react-redux'
import {Link} from 'react-router'
import EnterpriseStatusManager from './EnterpriseStatusManager'

import styles from './index.module.scss'
import EnterpriseListTable from '../../share/components/EnterpriseListTable/index'
import ResetPasswordModal from './ResetPasswordModal'

const Search = Input.Search

const statusMap = {
  ENABLED: '停用',
  DISABLED: '启用'
}

const columns = [
  {
    title: '企业名称',
    dataIndex: 'name',
    key: 'name',
    styleName: 'max-width-140',
    width: 300,
  }, {
    title: '企业管理员',
    dataIndex: 'admin.fullname',
    key: 'admin.fullname'
  }, {
    title: '管理员联系方式',
    key: 'adminContact',
    render: (text, record) => (
      <span>
        {record.admin.cellphone ? record.admin.cellphone : ''}
        {record.admin.cellphone ? <br/> : ''}
        {record.admin.telephone}
      </span>
    )
  }, {
    title: '法人',
    dataIndex: 'artificialPersonName',
    key: 'artificialPersonName'
  }, {
    title: '法人联系电话',
    dataIndex: 'artificialPersonContact',
    key: 'artificialPersonContact'
  }
]

class AuthorizationInProcess extends Component { // eslint-disable-line
  state = {
    pageSize: 10,
    pageNum: 1,
    enterpriseName: null,
    status: null,
    visible: false,
    admin: {}
  }

  componentDidMount() {
    this.getEnterprises({name: null})
  }

  getEnterprises() {

    const {status, pageNum, pageSize, enterpriseName} = this.state

    const realStatus = (['DISABLED', 'ENABLED'].indexOf(status) > -1) ? status : undefined

    this.props.actions.adminGetEnterprisesAuthorized({
      name: enterpriseName,
      validationStatus: 'Authorized',
      page: pageNum - 1,
      size: pageSize,
      status: realStatus
    })
  }

  search(name) {
    this.setState({enterpriseName: name}, () => this.getEnterprises())
  }

  manageEnterpriseStatus(enterprise) {
    this.refs.manageEnterpriseStatusModal.manageStatusModal(false, enterprise)
    this.props.actions.manageEnterpriseStatus(enterprise)
      .then(() => {
        this.getEnterprises()
      })
  }

  popUpEnterpriseStatusManager(status, enterprise) {
    this.refs.manageEnterpriseStatusModal.manageStatusModal(status, enterprise)
  }

  getRowClassName(record) {
    return record.status === 'DISABLED' ? 'disabled' : ''
  }

  handleChange(status) {
    this.setState({
      status,
      pageNum: 1
    }, () => this.getEnterprises())
  }

  handlePageParamChange(pageNum, pageSize) {
    this.setState({
      pageNum,
      pageSize
    }, () => {
      this.getEnterprises()
    })
  }

  renderActionColumn(text, record) {
    return (
      <span>
        <Link to={`/admin/view_enterprise_info/${record.id}`} className="red-button">查看</Link>
        <span className="divider" />
        <a
          href="javascript: void(0)"
          className="red-button"
          onClick={this.popUpEnterpriseStatusManager.bind(this, true, record)}>{statusMap[record.status]}</a><br/>
        <a href="javascript: void(0)" onClick={this.popUpResetPasswordModal.bind(this, record)} className="red-button">重置管理员密码</a>
      </span>
    )
  }

  onComplete() {
    this.setState({
      visible: false
    })
  }

  popUpResetPasswordModal(record) {
    this.setState({
      visible: true,
      admin: record.admin
    })
  }

  render() {
    const enterprises = this.props.admin.enterpriseAuthorized ? this.props.admin.enterpriseAuthorized.content : []
    const total = this.props.admin.enterpriseAuthorized.totalElements || 0

    return (
      <div styleName="main-panel">
        <div styleName="header-area">
          <h2 styleName="title">
            <span styleName="main">已审核企业</span>（
            <span styleName="count"> {total}</span>家 ）
          </h2>
          <span styleName="enterprise-status">企业状态：</span>
          <Select defaultValue="全部" onChange={::this.handleChange}>
            <Select.Option value="ALL">全部</Select.Option>
            <Select.Option value="DISABLED">停用</Select.Option>
            <Select.Option value="ENABLED">启用</Select.Option>
          </Select>
          <Search
            styleName="search-box"
            placeholder="输入企业名称进行查找"
            onSearch={value => this.search(value.trim())}
          />
          <div styleName="clear-line"/>
        </div>
        <EnterpriseListTable
          dataSource={enterprises}
          columns={columns}
          onPageParamChange={::this.handlePageParamChange}
          renderActionColumn={::this.renderActionColumn}
          total={total}
          getRowClassName={::this.getRowClassName}
        />
        <EnterpriseStatusManager
          ref="manageEnterpriseStatusModal" user={this.state.currentUser}
          handleSubmit={this.manageEnterpriseStatus.bind(this)}/>
        <ResetPasswordModal
          user={this.state.admin}
          onComplete={::this.onComplete}
          visible={this.state.visible}
        />
      </div>
    )
  }
}

export default connect(
  state => ({
    admin: state.admin,
  }),
  dispatch => ({actions: bindActionCreators(action, dispatch)})
)(cssModules(AuthorizationInProcess, styles, {allowMultiple: true}))
