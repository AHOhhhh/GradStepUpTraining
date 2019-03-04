import React, { Component } from 'react'
import cssModules from 'react-css-modules'
import { connect } from 'react-redux'
import { Button, Icon } from 'antd'
import { noop } from 'lodash'

import { USER_ROLE } from 'constants'
import AuditPaymentModal from '../../Admin/share/components/AuditPaymentModal'

import styles from './index.module.scss'

@connect(
  state => ({ auth: state.auth }),
  () => ({})
)
class AuditPaymentButton extends Component {
  handleAuditPayment() {
    const modal = this.refs.auditPaymentModal.getWrappedInstance()
    modal.showModal(this.props.order)
  }

  render() {
    const { order, afterSubmit = noop, auth: { user } } = this.props
    const isNotPlatformAdmin = user && user.role !== USER_ROLE.platformAdmin

    if (order.status !== 'OfflinePaidAwaitingConfirm' || isNotPlatformAdmin) {
      return null
    }

    return (
      <div className={styles.auditPayment}>
        <Button
          type="primary"
          htmlType="button"
          className="button primary audit-payment-button"
          onClick={::this.handleAuditPayment}>
          审核线下付款
        </Button>
        <div className="desc">
          <Icon type="info-circle-o" className="icon"/>
          有一笔线下账款支付，请审核
        </div>
        <AuditPaymentModal ref="auditPaymentModal" afterSubmit={afterSubmit}/>
      </div>
    )
  }
}

export default cssModules(AuditPaymentButton, styles, { allowMultiple: true });