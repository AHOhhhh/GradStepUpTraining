import React, {PropTypes} from 'react'
import {connect} from 'react-redux'
import {Link} from 'react-router'
import {getAirCargoOrder} from 'actions'
import styles from './index.module.scss'
import { ORDER_STATUS } from '../constants/orderStatus'

const PositionConfirmation = ({ getAirCargoOrder, order, preview }) => {
  const refresh = () => {
    getAirCargoOrder(order.id)
  }

  const renderView = () => {
    if (order.status === ORDER_STATUS.cancelled) {
      return (
        <div>
          <h2>很抱歉，由于货品或运力方面原因，舱位预定失败！</h2>
          <Link className="create-button" to={`/acg/create?acgOrderId=${order.id}`}>重新预定</Link>
        </div>
      )
    } else if (preview) {
      return (
        <div>
          <div className="title">运输安排</div>
          <p className="status">服务商正在进行安排，<span className="refresh-button" onClick={() => refresh()}>刷新</span>查看状态更新</p>
        </div>
      )
    }

    return (<div>
      <h2>工作人员正在为您确定舱位，请稍候......</h2>
      <p><span className="refresh-button" onClick={() => refresh()}>刷新</span>，查看状态更新</p>
    </div>)
  }

  return (
    <div className={styles['position-confirmation']}>
      {renderView()}
    </div>)
}

PositionConfirmation.propTypes = {
  order: PropTypes.shape({
    id: PropTypes.string
  }),
  getAirCargoOrder: PropTypes.func
}

export default connect(null, {getAirCargoOrder})(PositionConfirmation)
