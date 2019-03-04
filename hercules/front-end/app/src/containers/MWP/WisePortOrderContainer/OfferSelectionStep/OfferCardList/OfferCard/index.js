import React from 'react'
import {Card, Checkbox} from 'antd'
import cssModule from 'react-css-modules'

import styles from './index.module.scss'
import CompanyInfo from '../CompanyInfo'
import OfferInfo from '../OfferInfo'
import OfferDetail from '../OfferDetail'

const OfferCard = ({offer, onChange, checked, cardBorderStyle}) => {
  return (
    <div styleName="offer-card">
      <div className={styles[cardBorderStyle]}>
        <Card>
          <table styleName="offer-card__table">
            <tbody>
              <tr>
                <td style={{width: '8%'}}>
                  <div styleName="offer-card__checkbox">
                    <Checkbox
                      checked={checked}
                      onChange={(e) => {
                        onChange(e.target.checked)
                      }}
                  />
                  </div>
                </td>
                <td>
                  <CompanyInfo offer={offer}/>
                  <OfferInfo offer={offer}/>
                </td>
                <td style={{width: '5%'}}/>
                <td style={{width: '24%', verticalAlign: 'bottom'}} className={styles.line}>
                  <OfferDetail items={offer.items}/>
                </td>
              </tr>
            </tbody>
          </table>
        </Card>
      </div>
    </div>
  )
}

export default cssModule(OfferCard, styles, {allowMultiple: true})