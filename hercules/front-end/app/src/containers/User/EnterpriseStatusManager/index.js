import React, {Component, PropTypes} from "react" // eslint-disable-line
import cssModules from 'react-css-modules'
import PopModal from 'components/PopModal/index'
import styles from './index.module.scss'


const statusMap = {
  ENABLED: '停用',
  DISABLED: '启用'
}

class EnterpriseUserStatusManagerContainer extends Component { // eslint-disable-line
  static propTypes = {
    user: PropTypes.object,
    handleSubmit: PropTypes.func
  }

  state = {
    visible: false,
    currentUser: {}
  }

  manageStatusModal(isVisible, currentUser) {
    this.setState({
      visible: isVisible,
      currentUser
    })
  }

  closeModal() {
    this.setState({
      visible: false
    })
  }

  render() {
    const user = this.state.currentUser

    return (
      <span>
        <PopModal
          title={statusMap[user.status] + '企业账号'}
          handleSubmit={() => { this.props.handleSubmit(this.state.currentUser) }}
          onCancel={this.closeModal.bind(this)}
          visible={this.state.visible}
        >
          <div className={styles.content}>
            <div className={styles.alertFont}>您确定{statusMap[user.status]}该企业-</div>
            <div className={styles.accountUsername}>{user.username}</div>
            <div className={styles.alertFont}>的账号？</div>
          </div>
        </PopModal>
      </span>
    )
  }
}

export default cssModules(EnterpriseUserStatusManagerContainer, styles)
