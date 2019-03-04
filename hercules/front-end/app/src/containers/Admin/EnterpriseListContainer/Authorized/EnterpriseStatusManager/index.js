import React, {Component, PropTypes} from "react" // eslint-disable-line
import PopModal from 'components/PopModal/index'
import cssModules from 'react-css-modules'
import styles from './index.module.scss'


const statusMap = {
  ENABLED: '停用',
  DISABLED: '启用'
}

class EnterpriseUserStatusManagerContainer extends Component { // eslint-disable-line
  static propTypes = {
    enterprise: PropTypes.object,
    handleSubmit: PropTypes.func
  }

  state = {
    visible: false,
    selectedEnterprise: {}
  }

  manageStatusModal(isVisible, selectedEnterprise) {
    this.setState({
      visible: isVisible,
      selectedEnterprise
    })
  }

  closeModal() {
    this.setState({
      visible: false
    })
  }

  render() {
    const enterprise = this.state.selectedEnterprise

    return (
      <span>
        <PopModal
          title={statusMap[enterprise.status] + '企业账号'}
          handleSubmit={() => { this.props.handleSubmit(this.state.selectedEnterprise) }}
          onCancel={this.closeModal.bind(this)}
          visible={this.state.visible}
        >
          <div className={styles.content}>
            <div className={styles.alertFont}>您确定{statusMap[enterprise.status]}该企业-</div>
            <div className={styles.accountUsername}>{enterprise.name}</div>
            <div className={styles.alertFont}>的账号？</div>
          </div>
        </PopModal>
      </span>
    )
  }
}

export default cssModules(EnterpriseUserStatusManagerContainer, styles)
