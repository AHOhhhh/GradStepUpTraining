import React, {PropTypes} from 'react' // eslint-disable-line
import cssModules from 'react-css-modules'
import moment from 'moment'
import _ from 'lodash'
import {concatArgs} from 'utils/format'
import styles from './index.module.scss'

const formatDateTime = str => {
  return moment(str).format('YYYY-MM-DD HH:mm')
}

const TransportArrangement = ({order}) => {
  const {delegateOrderId, shippingInfo} = order
  const transportPlan = order.transportPlan || {}
  const {pickUpAddress, dropOffAddress} = shippingInfo
  const isArrivalDelivery = _.get(shippingInfo, 'arrival.delivery', false)
  const isDepartureDelivery = _.get(shippingInfo, 'departure.delivery', false)
  return (
    <div styleName="transport-arrangement">
      <h2 styleName="title">运输安排</h2>
      {isDepartureDelivery && <div styleName="section">
        <h3 styleName="section-title">始发港上门取货</h3>
        <div styleName="content">
          <div styleName="info-item">
            <div styleName="info">
              <div styleName="label">运单号：</div>
              <div styleName="text">{delegateOrderId}</div>
            </div>
          </div>
          <div styleName="info-item">
            <div styleName="info">
              <div styleName="label">联系电话：</div>
              <div styleName="text">{transportPlan.pickupTelephone}</div>
            </div>
          </div>
          <div styleName="info-item">
            <div styleName="info">
              <div styleName="label">取货时间：</div>
              <div styleName="text">{formatDateTime(transportPlan.scheduledPickupTime)}</div>
            </div>
          </div>
          <div styleName="info-item">
            <div styleName="info">
              <div styleName="label">发货人联系方式：</div>
              <div styleName="text">
                <div>{pickUpAddress.name}<span
                  styleName="phones">{concatArgs('/', pickUpAddress.cellphone, pickUpAddress.telephone)}</span></div>
                <div>{
                  concatArgs('', pickUpAddress.province, pickUpAddress.city, pickUpAddress.district, pickUpAddress.address)
                }</div>
              </div>
            </div>
          </div>
        </div>
      </div>}
      <div styleName="section">
        <h3 styleName="section-title">货运航班信息</h3>
        <div styleName="content">
          <div styleName="info-item">
            <div styleName="info">
              <div styleName="label">运单号：</div>
              <div styleName="text">{delegateOrderId}</div>
            </div>
          </div>
          <div styleName="info-item">
            <div styleName="info">
              <div styleName="label">航班号：</div>
              <div styleName="text">{transportPlan.scheduledFlight}</div>
            </div>
          </div>
          <div styleName="info-item">
            <div styleName="info">
              <div styleName="label">航班时间：</div>
              <div styleName="text">{transportPlan.scheduledTakeOffTime ? formatDateTime(transportPlan.scheduledTakeOffTime) : ''}</div>
            </div>
          </div>
        </div>
      </div>
      {isArrivalDelivery && <div styleName="section">
        <h3 styleName="section-title">到达港机场派送</h3>
        <div styleName="content">
          <div styleName="info-item">
            <div styleName="info">
              <div styleName="label">运单号：</div>
              <div styleName="text">{delegateOrderId}</div>
            </div>
          </div>
          <div styleName="info-item">
            <div styleName="info">
              <div styleName="label">联系电话：</div>
              <div styleName="text">{transportPlan.deliveryTelephone}</div>
            </div>
          </div>
          <div styleName="info-item">
            <div styleName="info">
              <div styleName="label">派送时间：</div>
              <div styleName="text">{formatDateTime(transportPlan.expectedDeliveryTime)}</div>
            </div>
          </div>
          <div styleName="info-item">
            <div styleName="info">
              <div styleName="label">收货人联系方式：</div>
              <div styleName="text">
                <div>{dropOffAddress.name}<span
                  styleName="phones">{concatArgs('/', dropOffAddress.cellphone, dropOffAddress.telephone)}</span></div>
                <div>{
                  concatArgs('', dropOffAddress.province, dropOffAddress.city, dropOffAddress.district, dropOffAddress.address)
                }</div>
              </div>
            </div>
          </div>
        </div>
      </div>}
    </div>
  )
}

TransportArrangement.propTypes = {
  order: PropTypes.object
}

export default cssModules(TransportArrangement, styles, {allowMultiple: true})