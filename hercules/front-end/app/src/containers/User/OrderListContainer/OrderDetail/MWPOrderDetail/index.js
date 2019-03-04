import React from 'react';
import _ from 'lodash'

import cssModules from 'react-css-modules';
import regionCode from 'components/RegionSelector/regionCode.json'

import styles from './index.module.scss';

import GoodList from '../../../../shared/GoodList/index'

const formatAddress = (contact) => {
  if (!contact) return '---'

  const countryItem = regionCode.find((item) => {
    return item.code === contact.countryCode
  }) || {country: ''}

  const address = countryItem.country + _.values(_.pick(contact, 'province', 'city', 'district', 'address')).join('');
  return address
}

const formatPhone = (contact) => {
  const resultArr = [].concat(contact.mobile).concat(contact.phone)
  return _.join(_.compact(resultArr), '/')
}

const MWPOrderDetail = (props) => (
  <div styleName="mwp-order-detail">
    {props.isReferenced && <i styleName="refer-icon"/>}
    <section styleName="basis-info">
      <section styleName="labeled-text">
        <span styleName="label">申报口岸：</span>
        <div styleName="secondary">{props.portName}</div>
      </section>

      <section styleName="labeled-text">
        <span styleName="label">监管方式：</span>
        <div styleName="secondary">{props.supervisionName}</div>
      </section>

      <section styleName="labeled-text">
        <span styleName="label">需求时效：</span>
        <div styleName="secondary">{props.orderValidationTime}</div>
      </section>
    </section>
    <section styleName="contact-info">
      <section styleName="labeled-text">
        <span styleName="label">联系方式：</span>
        <div styleName="secondary">
          <div>{props.name}</div>
          <div>{formatAddress(props)}</div>
          <div>{formatPhone(props)}</div>
        </div>
      </section>
    </section>
    <section styleName="goods-info">
      <section styleName="labeled-text">
        <span styleName="label">货品描述：</span>
        <div styleName="secondary">
          <GoodList goods={props.goods}/>
        </div>
      </section>
    </section>
  </div>
)

export default cssModules(MWPOrderDetail, styles, {allowMultiple: true});
