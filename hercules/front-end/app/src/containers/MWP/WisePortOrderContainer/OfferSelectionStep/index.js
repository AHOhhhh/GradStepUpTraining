import React, {Component} from 'react'
import cssModule from 'react-css-modules'
import {connect} from 'react-redux'
import { bindActionCreators } from 'redux'
import CancelOrderButton from 'components/CancelOrderButton'
import message from 'antd/lib/message'
import {result} from 'lodash'
import * as actions from './actions'
import styles from './index.module.scss'
import FundInfo from '../../../shared/FundInfo'
import OrderDetailPanel from '../../share/components/OrderDetailPanel'
import OfferCardList from './OfferCardList'
import OfferSubmission from './OrderSubmission'
import WisePortTimeLine from '../../share/components/WisePortTimeLine'
import {renderReferenceAdBanner, renderReferenceOrderBanner} from '../../share/components/AdContainer'

const calculateTotal = (offers) => {
  return offers.reduce((prePrice, curOffer) => {
    return prePrice + curOffer.items.reduce((pre, cur) => {
      return pre + (cur.estimation || cur.exactPrice || 0)
    }, 0)
  }, 0)
}

class OfferSelectionStep extends Component {
  constructor(props) {
    super(props)
    this.state = {
      visible: false,
      totalPrice: 0,
      selectedOffers: []
    }
  }

  componentDidMount() {
    this.props.actions.getOrderOffers(this.props.order.id)
  }

  submitOffer() {
    if (this.state.selectedOffers.length === 0) return false
    const selectOfferIds = this.state.selectedOffers.map(item => item.offerId)
    const orderId = this.props.order.id
    this.props.actions.updateOrderOffers(orderId, {selectOfferIds})
      .then(() => {
        this.props.actions.getOrder(orderId)
      })
      .catch(() => {
        message.error('提交失败，请稍后再试')
      })
  }

  handleChange(selectedOffers) {
    const totalPrice = calculateTotal(selectedOffers);
    this.setState({
      totalPrice,
      selectedOffers
    })
  }

  renderCancelButton(order) {
    return (
      CancelOrderButton && <CancelOrderButton order={order}/>
    )
  }

  render() {
    const {order, offers, preview} = this.props
    return (
      <div styleName="offer-selection-step">
        { false && <CancelOrderButton order={order} /> }
        <div styleName="main-container">
          {renderReferenceAdBanner(order)}
          <OfferCardList offers={offers} order={this.props.order} onChange={::this.handleChange}/>
          <FundInfo order={order}/>
          <WisePortTimeLine operationLogs={result(order, 'operationLogs', [])}/>
          {renderReferenceOrderBanner(order)}
        </div>
        <div styleName="side-container">
          <OfferSubmission price={this.state.totalPrice} onSubmit={::this.submitOffer}/>
          <OrderDetailPanel detail={order}/>
          {!preview && this.renderCancelButton(order)}
        </div>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    offers: state.wisePortOrder.offerSelection.offers,
    operationLogs: state.wisePortOrder.order.operationLogs
  }
}

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(actions, dispatch)
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(cssModule(OfferSelectionStep, styles, {allowMultiple: true}))