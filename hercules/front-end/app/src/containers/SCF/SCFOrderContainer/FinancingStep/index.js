import React from 'react'
import cssModules from 'react-css-modules'
import { connect } from 'react-redux';
import {isEmpty, get} from 'lodash'
import { getSCFOrderById } from 'actions';
import { ORDER_STATUS } from '../../constants/steps';
import OfferInfo from './OfferInfo'
import financerImg from '../../../shared/assets/financer.svg'
import styles from './index.module.scss'

export const FinancingStep = ({order, getSCFOrderById}) => {
  const currentOffer = isEmpty(order.scfFinancierOffer) ? {} : Object.assign({}, order.scfFinancierOffer, {serviceType: order.serviceType})

  const renderLoadingPage = () => (
    <div>
      <p styleName="message">资金方接洽中，请耐心等待...</p>
      <p styleName="tip"><a onClick={() => { getSCFOrderById(order.id) }}>刷新</a>，查看接洽结果</p>
    </div>)

  const renderCompanyName = () => {
    const financierName = get(order, 'financierName', null) || get(order, 'scfFinancierOffer.financierName', null)
    return (
      <p styleName="offer">
        <img src={financerImg} alt="financer-img"/>
        <span styleName="label">资金方</span>
        {financierName && <span>{financierName}</span>}
      </p>)
  }

  const renderApplyContainer = () => {
    const creditGrantingUrl = get(order, 'creditGrantingUrl', null)
    const currentOffer = {
      financierName: get(order, 'financierName'),
      serviceType: get(order, 'serviceType')
    }

    return (
      <div>
        {renderCompanyName()}
        <OfferInfo offer={currentOffer} />
        {creditGrantingUrl && (
          <div styleName="apply-container">
            <a href={creditGrantingUrl} target="_blank" rel="noopener noreferrer">申请融资授信</a>
          </div>)}
      </div>)
  }

  const renderOffer = () => (
    <div>
      {renderCompanyName()}
      <OfferInfo offer={currentOffer} />
    </div>)

  const StatusMap = [{
    status: ORDER_STATUS.waitForFinancier,
    render: renderLoadingPage
  }, {
    status: ORDER_STATUS.waitForFinancierOffer,
    render: renderApplyContainer
  }, {
    status: ORDER_STATUS.closed,
    render: renderOffer
  }]

  return (
    <div styleName="container">
      <h2>融资授信方案</h2>
      {StatusMap.find(item => item.status === order.status).render()}
    </div>)
}

export default connect(null, {getSCFOrderById})(cssModules(FinancingStep, styles, {allowMultiple: true}))
