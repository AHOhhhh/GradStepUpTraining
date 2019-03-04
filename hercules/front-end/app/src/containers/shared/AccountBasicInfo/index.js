/* eslint-disable react/prefer-stateless-function */
import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import styles from './index.module.scss'
import {concatArgs} from '../../../utils/format'

import EditUserModal from '../../User/EditUserModal/index';

class AccountBasicInfo extends Component {
  
  constructor(props) {
    super(props)
    this.state = {
      visible: false
    }
  }
  
  popUpAccountEditor(e) {
    e.preventDefault()
    this.refs.editUserModal.showModal(this.props.accountInfo)
  }
  
  render() {
    const {accountInfo, canEdit} = this.props

    return (
      <div>
        <header className={styles.accountInfoTitle}>
          <span>账号基本信息</span>
          <a onClick={this.popUpAccountEditor.bind(this)} hidden={!canEdit}>编辑</a>
        </header>
        <ul className={styles.accountContent}>
          <li className={styles.accountInfoItem}>
            <div className={styles.accountInfoKey}>用户名称:</div>
            <div className={styles.accountInfoValue}>{accountInfo.username}</div>
          </li>
          <li className={styles.accountInfoItem}>
            <div className={styles.accountInfoKey}>联系人姓名:</div>
            <div className={styles.accountInfoValue}>{accountInfo.fullname}</div>
          </li>
          <li className={styles.accountInfoItem}>
            <div className={styles.accountInfoKey}>联系方式:</div>
            <div
              className={styles.accountInfoValue}>{concatArgs(';', accountInfo.cellphone, accountInfo.telephone)}</div>
          </li>
          <li className={styles.accountInfoItem}>
            <div className={styles.accountInfoKey}>邮箱:</div>
            <div className={styles.accountInfoValue}>{accountInfo.email}</div>
          </li>
        </ul>
  
        <EditUserModal ref="editUserModal" />

      </div>
    )
  }
}

export default cssModules(AccountBasicInfo, styles)