import React from 'react'
import cssModules from 'react-css-modules'
import {BasicModal} from 'components'
import { find } from 'lodash'
import styles from './index.module.scss'
import {concatArgs} from '../../../utils/format'

const getFooter = (onCancel) => {
  return (<div key="modalFooter" styleName="modal-footer">
    <button
      type="submit" className="button primary" styleName="button"
      onClick={onCancel}>确定
    </button>
  </div>)
}

const getCurrentAddress = (shippingInfo, contacts) => {
  const {departure, arrival} = shippingInfo
  const currentPickUpAddress = departure.isPickUpOrDropOff ? (find(contacts, {id: departure.addressId}) || {}) : {}
  const currentDropOffAddress = arrival.isPickUpOrDropOff ? (find(contacts, {id: arrival.addressId}) || {}) : {}

  return {currentPickUpAddress, currentDropOffAddress}
}

const getCurrentDistricts = (shippingInfo, contacts) => {
  const currentAddress = getCurrentAddress(shippingInfo, contacts)

  return concatArgs('、', currentAddress.currentPickUpAddress.district, currentAddress.currentDropOffAddress.district)
}

const getCityInfo = (address) => {
  return (address.city === '市辖区' || address.city === '县') ? address.province : address.city
}

const getCurrentCity = (shippingInfo, contacts) => {
  const currentAddress = getCurrentAddress(shippingInfo, contacts)

  return {pickUpCity: getCityInfo(currentAddress.currentPickUpAddress), dropOffCity: getCityInfo(currentAddress.currentDropOffAddress)}
}

const mapSupportedDistricts = (districts) => {
  return districts.map(district => district.district).join('、')
}

const SupportedDistrictsModal = ({onCancel, orderPrice, shippingInfo, contacts, ...rest}) => {
  return (<BasicModal
    {...rest}
    onCancel={onCancel}
    footer={getFooter(onCancel)}
  >
    <div styleName="districts-content">
      <div styleName="tips">
        <span styleName="invalid-icon"/>
        <span styleName="invalid-info">提示：您提供的<span
          styleName="selected-districts">{getCurrentDistricts(shippingInfo, contacts)}</span>暂不支持上门取货/机场派送。</span>
      </div>
      {shippingInfo.departure.isPickUpOrDropOff && <div styleName="available-districts">
        <div styleName="type">上门取货地址可选区域（{getCurrentCity(shippingInfo, contacts).pickUpCity}）：</div>
        <div styleName="districts">{mapSupportedDistricts(orderPrice.supportedPickupDistrict || [])}</div>
      </div>}
      {shippingInfo.arrival.isPickUpOrDropOff && <div styleName="available-districts">
        <div styleName="type">机场派送地址可选区域（{getCurrentCity(shippingInfo, contacts).dropOffCity}）：</div>
        <div styleName="districts">{mapSupportedDistricts(orderPrice.supportedDeliveryDistrict || [])}</div>
      </div>}
    </div>
  </BasicModal>)
}

export default cssModules(SupportedDistrictsModal, styles, {allowMultiple: true});
