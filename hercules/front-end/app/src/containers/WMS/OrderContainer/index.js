import React, { Component, createElement } from 'react'
import { bindActionCreators } from 'redux'
import { connect } from 'react-redux'

import { flowRight } from 'lodash'

import { getOrder, getWMSOrderTemplate, getEnterpriseInfo } from 'actions'

import { ORDER_TYPE } from '../share/constants/order-config'

import PriceVerificationContainer from '../PriceVerificationContainer'
import OrderDetailsContainer from '../OrderDetailsContainer'
import PaymentResultContainer from '../PaymentResultContainer'
import OrderCancelledContainer from '../OrderCancelledContainer'

const componentMap = {
  Submitted: PriceVerificationContainer,
  Audited: OrderDetailsContainer,
  OfflinePaidAwaitingConfirm: OrderDetailsContainer,
  WaitForPay: OrderDetailsContainer,
  Paid: PaymentResultContainer,
  Cancelled: OrderCancelledContainer
}

const orderStatusMapper = ({status, type}) => {
  const map = {
    Closed: 'Paid',
    Opened: 'Paid',
    Submitted: type === ORDER_TYPE.recharge ? 'Audited' : status
  }
  return map[status] || status
}

class OrderContainer extends Component {
  constructor(props) {
    super(props)
    this.state = {
      status: null
    }
  }

  componentWillMount() {
    const {actions, params} = this.props

    actions.getOrder(params.orderId)
    actions.getWMSOrderTemplate(params.orderId)
  }

  componentWillReceiveProps(newProps) {
    if (newProps.order) {
      this.setState({
        status: orderStatusMapper(newProps.order)
      })
    }
  }

  render() {
    const component = componentMap[this.state.status]
    const preview = this.props.preview
    return (
      <div>
        {component
          ? createElement(component, this.props, preview)
          : (<h2>loading</h2>)}
      </div>
    )
  }
}

const connector = connect(
  state => ({
    orderTemplate: state.wms.orderTemplate,
    order: state.wms.order,
    enterprise: state.enterpriseInfo.info,
    auth: state.auth
  }),
  dispatch => ({actions: bindActionCreators({getOrder, getWMSOrderTemplate, getEnterpriseInfo}, dispatch)})
)

const enhancer = flowRight([connector])

export default enhancer(OrderContainer)
