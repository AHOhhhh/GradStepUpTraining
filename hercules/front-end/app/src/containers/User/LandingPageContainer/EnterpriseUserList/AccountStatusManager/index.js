import React, {Component, PropTypes} from "react" // eslint-disable-line
import cssModules from 'react-css-modules'
import PopModal from 'components/PopModal/index'
import styles from './index.module.scss'


const statusMap = {
  ENABLED: '停用',
  DISABLED: '启用'
}

class AccountStatusManagerContainer extends Component { // eslint-disable-line
  static propTypes = {
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
          title={statusMap[user.status] + '企业用户账号'}
          handleSubmit={() => { this.props.handleSubmit(this.state.currentUser) }}
          onCancel={this.closeModal.bind(this)}
          visible={this.state.visible}
        >
          <div stylesName="content">
            <div styleName="alertFont">您确定{statusMap[user.status]}企业用户-<span styleName="accountUsername">{user.username}</span>的账号？</div>
          </div>
        </PopModal>
      </span>
    )
  }
}

export default cssModules(AccountStatusManagerContainer, styles)
