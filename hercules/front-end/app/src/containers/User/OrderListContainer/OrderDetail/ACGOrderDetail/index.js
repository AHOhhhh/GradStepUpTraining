import React from 'react';
import cssModules from 'react-css-modules';
import _ from 'lodash'

import styles from './index.module.scss';
import GoodList from '../../../../shared/GoodList/index'
import {concatArgs} from '../../../../../utils/format'

const renderAddress = (data) => {
  const displayData = _.isEmpty(data.name) ? {name: '--'} : data
  const displayAddress = concatArgs('', data.country, data.province, data.city, data.district, data.address)
  return (
    <div styleName="secondary">
      <p>{displayData.name} {displayData.cellphone || displayData.telephone}</p>
      <p>{displayAddress}</p>
    </div>
  )
}

const ACGOrderDetail = (props) => (
  <div styleName="acg-order-detail">
    {props.isReferenced && <i styleName="refer-icon"/>}
    <section styleName="sender-info">
      <section styleName="labeled-text">
        <span styleName="short-label">发货人：</span>
        {renderAddress(_.get(props, 'shippingInfo.pickUpAddress', {}))}
      </section>
    </section>

    <section styleName="receiver-info">
      <section styleName="labeled-text">
        <span styleName="short-label">收货人：</span>
        {renderAddress(_.get(props, 'shippingInfo.dropOffAddress', {}))}
      </section>
    </section>

    <section styleName="basis-info">
      <section styleName="labeled-text">
        <span styleName="long-label">航运时间：</span>
        <div styleName="secondary">{_.get(props, 'shippingInfo.estimatedDeliveryTime')}</div>
      </section>
    </section>

    <section styleName="block">
      <section styleName="labeled-text">
        <span styleName="long-label">货品描述：</span>
        <div styleName="secondary">
          <GoodList goods={props.goods}/>
        </div>
      </section>
    </section>
  </div>
)

export default cssModules(ACGOrderDetail, styles, {allowMultiple: true});
