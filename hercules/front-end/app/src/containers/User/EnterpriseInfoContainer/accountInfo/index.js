/* eslint-disable react/prefer-stateless-function */
import React, {Component, PropTypes} from 'react'
import cssModules from 'react-css-modules'
import {connect} from 'react-redux'
import {USER_ROLE} from 'constants'
import {bindActionCreators} from 'redux'
import * as actions from './../actions'
import styles from './index.module.scss'

import AccountBasicInfo from '../../../shared/AccountBasicInfo'

@connect(
  state => ({
    user: state.auth.user
  }),
  dispatch => ({ actions: bindActionCreators(actions, dispatch) })
)
class AccountBasicInfoContainer extends Component {
  static propTypes = {
    user: PropTypes.object
  }

  componentDidMount() {
    const {id, role} = this.props.user
    if (id) {
      this.props.actions.getUserById(id, role)
    }
  }

  render() {
    const accountInfo = this.props.user

    return (
      <div className={styles.accountInfoBody}>
        {accountInfo.role === USER_ROLE.enterpriseAdmin &&
        (<div className={styles.accountRoleTag}>
          <div className={styles.smallCircle}/>
          <h3 className={styles.accountRoleName}>管理员</h3>
        </div>)}
        <div className={styles.accountRole}>身份：{accountInfo.role === USER_ROLE.enterpriseAdmin ? '企业管理员' : '企业用户'}</div>
        <section>
          <AccountBasicInfo accountInfo={accountInfo} canEdit={accountInfo.role === USER_ROLE.enterpriseAdmin}/>
        </section>
      </div>
    )
  }
}

export default cssModules(AccountBasicInfoContainer, styles)