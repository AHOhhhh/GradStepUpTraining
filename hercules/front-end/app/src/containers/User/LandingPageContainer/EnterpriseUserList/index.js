import {Table, message} from 'antd'
import React from 'react'
import cssModules from 'react-css-modules'
import result from 'lodash/result'
import Joyride from 'react-joyride'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import * as action from 'actions'

import AccountStatusManager from './AccountStatusManager';
import EditUserModal from '../../EditUserModal'
import CreateUserModal from '../CreateUserModal'
import ResetPasswordModal from '../ResetPasswordModal'

import styles from './index.module.scss'

const statusMap = {
  ENABLED: '停用',
  DISABLED: '启用'
}

class EnterpriseUserList extends React.Component {

  constructor(props) {
    super(props)
    this.columns = [
      {
        title: '用户名',
        dataIndex: 'username',
        key: 'username',
      },
      {
        title: '姓名',
        dataIndex: 'fullname',
        key: 'name',
        className: 'max-width-140'
      },
      {
        title: '联系方式',
        render: (text, record) => (
          <div>
            <div>{record.cellphone}</div>
            <div>{record.telephone}</div>
          </div>

        ),
        key: 'contact',
      },
      {
        title: '邮箱',
        dataIndex: 'email',
        key: 'email',
      },
      {
        title: '操作',
        key: 'action',
        render: (text, record) => (
          <span className="actions">
            <a href="javascript: void(0)" onClick={this.popUpAccountEditor.bind(this, record)}>编辑</a>
            <span className="ant-divider"/>
            <a href="javascript: void(0)" onClick={this.popUpResetPassword.bind(this, record)}>重置密码</a>
            <span className="ant-divider"/>
            <a href="javascript: void(0)" onClick={this.popUpUserStatusManager.bind(this, true, record)}>{statusMap[record.status]}</a>
          </span>
        ),
      }
    ]
  }

  state = {
    pageSize: 10,
    pageNum: 0
  }

  componentDidMount() {
    this.loadEnterpriseUserList();
  }

  manageUserStatus(currentUser) {
    this.refs.manageUserStatusModal.manageStatusModal(false, currentUser)
    this.props.actions.manageUserStatus(currentUser)
      .then(() => {
        this.loadEnterpriseUserList()
      })
  }

  popUpUserStatusManager(status, currentUser) {
    this.refs.manageUserStatusModal.manageStatusModal(status, currentUser)
  }

  loadEnterpriseUserList() {
    const {enterpriseId, actions} = this.props
    const {pageSize, pageNum} = this.state;

    if (enterpriseId) {
      actions.getEnterpriseUsers(enterpriseId, pageSize, pageNum)
        .catch(() => {
          message.error('请求用户列表失败！')
        })
    }
  }

  showModal() {
    this.refs.createUserModal.showModal()
  }

  popUpAccountEditor(record) {
    this.refs.editUserModal.showModal(record)
  }

  popUpResetPassword(record) {
    this.refs.resetPasswordModal.showModal(record)
  }

  getRowClassName(record) {
    return record.status === 'DISABLED' ? 'disabled' : ''
  }

  generatePaginationConfig() {
    const total = result(this.props.userList, 'totalElements', 0)
    return {
      total,
      showSizeChanger: true,
      onChange: (pageNum) => {
        this.setState({
          pageNum: pageNum - 1
        }, () => {
          this.loadEnterpriseUserList()
        })
      },
      onShowSizeChange: (pageNum, pageSize) => {
        this.setState({
          pageNum: pageNum - 1,
          pageSize
        }, () => {
          this.loadEnterpriseUserList()
        })
      }
    }
  }

  renderUserGuide() {
    return (
      <Joyride
        ref="joyride"
        steps={[
          {
            text: '恭喜！贵公司已经通过了平台企业资质认证审核！现在试着去添加第一个企业用户吧。',
            selector: '.add-user'
          }
        ]}
        autoStart={true}
        run={true}
        callback={() => {}}
        offsetParentSelector={'.users-sidebar'}  // Notice: should set selector correctly
      />
    )
  }

  shouldShowUserGuide() {
    const {userId, userList} = this.props
    const length = result(userList, 'content.length', undefined)

    if (length === 0 && !this.hasUserReadGuide(userId)) {
      const users = JSON.parse(localStorage.getItem('hasReadGuide')) || []
      users.push(userId)
      localStorage.setItem('hasReadGuide', JSON.stringify(users))
      return true
    }
    return false
  }

  hasUserReadGuide = (userId) => {
    const users = JSON.parse(localStorage.getItem('hasReadGuide')) || []
    return users.indexOf(userId) > -1
  }

  render() {
    const content = this.props.userList.content || []

    const pagination = this.generatePaginationConfig()

    return (
      <div styleName="user-list-container">
        {
          this.shouldShowUserGuide() && this.renderUserGuide()
        }
        <span styleName="add-user" className="anticon" onClick={::this.showModal}>新增用户</span>
        <div className="user-landing-page-enterprise-user-list">
          <Table
            dataSource={content}
            columns={this.columns}
            pagination={pagination}
            rowKey={record => record.id}
            rowClassName={this.getRowClassName}/>
          <EditUserModal ref="editUserModal" onCloseCallback={::this.loadEnterpriseUserList}/>
          <CreateUserModal ref="createUserModal" onCloseCallback={::this.loadEnterpriseUserList} />
          <ResetPasswordModal ref="resetPasswordModal"/>
          <AccountStatusManager ref="manageUserStatusModal" handleSubmit={this.manageUserStatus.bind(this)}/>
        </div>
      </div>
    )
  }
}

export default connect(
  state => ({
    userId: state.auth.userId,
    enterpriseId: state.auth.enterpriseId || (state.auth.user ? state.auth.user.enterpriseId : null),
    userList: state.enterpriseUser.userList
  }),
  dispatch => ({
    actions: bindActionCreators(action, dispatch)
  })
)(cssModules(EnterpriseUserList, styles))
