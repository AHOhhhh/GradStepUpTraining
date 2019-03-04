import React from 'react'
import cssModules from 'react-css-modules'
import TransportArrangement from '../../components/TransportArrangement'
import styles from './index.module.scss'
import OrderLogistics from './OrderLogistic/index'

const OrderTrackingContainer = ({ order }) => {
  return (
    <div>
      <OrderLogistics order={order} />
      <div styleName="item">
        <TransportArrangement order={order}/>
      </div>
    </div>
  )
}

export default cssModules(OrderTrackingContainer, styles, {allowMultiple: true})
