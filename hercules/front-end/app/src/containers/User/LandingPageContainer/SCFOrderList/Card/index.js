import React from 'react';
import cssModules from 'react-css-modules';
import { formatDate, formatPrice, formatRateRange } from 'utils';
import financierImg from '../../../../shared/assets/financer.svg'
import styles from './index.module.scss'

const getRandomCardBg = () => (Math.round(Math.random()) ? 'card-bg1' : 'card-bg2')

export const Card = ({data: {remainingQuota, currency, scfFinancierOffer, balanceOfFinancing}}) => {
  const {serviceType, credit, currency: offerCurrency, expirationDate, applyLoanUrl, financierName} = scfFinancierOffer
  return (
    <div styleName={`card ${getRandomCardBg()}`}>
      <h3>{serviceType}</h3>
      <p styleName="enterprise-name">
        <img src={financierImg} alt="financier"/>
        <span>资金方：{financierName}</span>
      </p>
      <div styleName="first-row">
        <div styleName="col">
          <p>授信额度（元）</p>
          <p>{credit ? formatPrice(credit, offerCurrency) : '-'}</p>
        </div>
        <div styleName="col">
          <p>利率</p>
          <p>{formatRateRange(scfFinancierOffer)}</p>
        </div>
      </div>
      <div>
        <div styleName="col">
          <p>可贷额度（元）</p>
          <p>{remainingQuota ? formatPrice(remainingQuota, currency) : '-'}</p>
        </div>
        <div styleName="col">
          <p>融资余额（元）</p>
          <p>{balanceOfFinancing ? formatPrice(balanceOfFinancing, currency) : '-'}</p>
        </div>
      </div>
      <div styleName="card-footer">
        <div styleName="left">
          <p>有效期</p>
          <p>{expirationDate ? formatDate(expirationDate) : '-'}</p>
        </div>
        {applyLoanUrl && (<div styleName="right">
          <a href={applyLoanUrl} target="_blank" rel="noreferrer noopener">申请融资</a>
        </div>)}
      </div>
    </div>)
}

export default cssModules(Card, styles, {allowMultiple: true})
