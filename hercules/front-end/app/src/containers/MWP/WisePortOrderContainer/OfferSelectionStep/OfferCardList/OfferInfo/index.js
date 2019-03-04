import React from 'react'
import {Icon} from 'antd'
import cssModules from 'react-css-modules'
import moment from 'moment'
import * as constants from '../../constant'

import styles from './index.module.scss'

const OfferInfo = ({offer}) => {
  const companyService = [
    {
      type: '结算方式',
      icon: 'credit-card'
    }, {
      type: '付款方式',
      icon: 'credit-card'
    }, {
      type: '报价费用',
      icon: 'red-envelope'
    }, {
      type: '报价有效期',
      icon: 'calendar'
    },
  ];
  return (
    <div styleName="offer-info">
      <table>
        <tbody>
          <tr styleName="contain-info">
            {
            companyService.map((item, index) => {
              const i = index
              if (index === 0) {
                return <td key={i}><span styleName="dollarIcon"/>{item.type}</td>
              }
              return <td key={i}><Icon type={item.icon} styleName="icon"/>{item.type}</td>
            })
          }
          </tr>
          <tr styleName="contain-result">
            <td>{constants.settlementMaps[offer.settlement] || offer.settlement}</td>
            <td>{constants.typeOfPaymentMaps[offer.typeOfPayment] || offer.typeOfPayment}</td>
            <td>{parseInt(offer.offerCost) === 1 ? '含税价' : '不含税价'}</td>
            <td>{moment(offer.offerValidationTime).format('YYYY-MM-DD')}</td>
          </tr>
        </tbody>
      </table>
    </div>
  )
};

export default cssModules(OfferInfo, styles)