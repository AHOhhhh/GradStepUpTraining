import React from 'react'
import cssModules from 'react-css-modules'
import { connect } from 'react-redux';
import { getSCFOrderById } from 'actions';
import styles from './index.module.scss'
import { ORDER_STATUS } from '../../constants/steps';
import orderFinishedSVG from '../../../shared/assets/order-finished.svg'
import orderRejectedSVG from '../../../shared/assets/order-rejected.svg'

const StatusMap = [{
  status: ORDER_STATUS.submitted,
  title: '您的订单已提交审核，请耐心等待…',
  canRefresh: true
}, {
  status: ORDER_STATUS.orderAccepted,
  title: '您的订单已通过审核，请尽快完成订单后续操作。',
  icon: orderFinishedSVG
}, {
  status: ORDER_STATUS.orderRejected,
  title: '您的订单未通过审核，订单已中止，可选择重新下单。',
  icon: orderRejectedSVG
}]

export const ApproveStep = ({order: {status, id}, getSCFOrderById}) => {
  const { icon = null, title, canRefresh = false } = StatusMap.filter(item => item.status === status)[0]
  return (
    <div styleName="container">
      <div styleName="message">
        {icon && <img src={icon} alt="orderStatus" />}
        <p>{title}</p>
      </div>
      {canRefresh && <p styleName="tip"><a onClick={() => { getSCFOrderById(id) }}>刷新</a>，查看审核状态更新</p>}
    </div>)
}

export default connect(null, {getSCFOrderById})(cssModules(ApproveStep, styles, {allowMultiple: true}))
