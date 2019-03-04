import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import * as action from './actions'
import PaymentFailure from '../../shared/PaymentFailure/index'

class PaymentFailureContainer extends Component { //eslint-disable-line

  repayOrder() {
    this.props.actions.repayAirportOrder(this.props.params.orderId)
  }

  render() {
    const previousUrl = `/acg/orders/${this.props.params.orderId}`
    return <PaymentFailure previousUrl={previousUrl} repayOrder={this.repayOrder}/>
  }
}

export default connect(
  state => ({
    order: state.acg.order
  }),
  dispatch => ({
    actions: bindActionCreators(action, dispatch)
  }))(PaymentFailureContainer)