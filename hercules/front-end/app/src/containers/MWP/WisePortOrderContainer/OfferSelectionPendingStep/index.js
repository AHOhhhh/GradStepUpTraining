import React from 'react'
import cssModule from 'react-css-modules'
import {result} from 'lodash'
import CancelOrderButton from 'components/CancelOrderButton'

import styles from './index.module.scss'

import FundInfo from '../../../shared/FundInfo'
import OrderDetailPanel from '../../share/components/OrderDetailPanel'
import OrderDetailStatus from '../../../shared/OrderDetailStatus'
import WisePortTimeLine from '../../share/components/WisePortTimeLine'
import Loading from './Loading'
import {renderReferenceAdBanner, renderReferenceOrderBanner} from '../../share/components/AdContainer'

function renderReferenceAd(order, preview) {
  if (preview) {
    return null
  }
  return renderReferenceAdBanner(order)
}

function renderReferenceOrder(order, preview) {
  if (preview) {
    return null
  }
  return renderReferenceOrderBanner(order)
}

function renderOrderDetailStatus(preview, order, data, current) {
  if (preview) {
    return <OrderDetailStatus order={order} data={data} current={current}/>
  }
  return null
}

function renderCancelButton(order) {
  return (
    CancelOrderButton && <CancelOrderButton order={order} />
  )
}

const OfferSelectionPendingStep = ({order, preview, data, current}) => {
  return (
    <div styleName="offer-selection-step">
      <div styleName="main-container">
        {renderReferenceAd(order, preview)}
        <Loading preview={preview}/>
        <FundInfo order={order}/>
        <WisePortTimeLine operationLogs={result(order, 'operationLogs', [])}/>
        {renderReferenceOrder(order, preview)}
      </div>
      <div styleName="side-container">
        {renderOrderDetailStatus(preview, order, data, current)}
        <OrderDetailPanel detail={order} preview={preview}/>
        {!preview && renderCancelButton(order)}
      </div>
    </div>)
}

export default cssModule(OfferSelectionPendingStep, styles, {allowMultiple: true})
