import React from 'react'
import cssModules from 'react-css-modules'
import styles from './index.module.scss'

import {serviceTypeMap} from '../../../../share/constants'

const EnterpriseInfo = ({offer}) => {
  return (
    <div styleName="title">
      <div styleName="company-info">
        <span styleName="company-name">{offer.companyName}</span>
        <span styleName="offer-types">
          {
            offer.services.map((type, index) => {

              const i = index
              const hasNext = (offer.services.length - 1) > index
              const styleName = serviceTypeMap[type].styleName + ' offer-type-label'
              const text = serviceTypeMap[type].text
              return (
                <span key={i}>
                  <span styleName={styleName}>{text}</span>
                  {hasNext ? <span styleName="plus">+</span> : null}
                </span>
              )
            })
          }
        </span>

      </div>
      <div styleName="company-tel">
        <span styleName="telephone">{offer.companyTelphone || '--'}</span>
      </div>
    </div>
  )
};

export default cssModules(EnterpriseInfo, styles, {allowMultiple: true})