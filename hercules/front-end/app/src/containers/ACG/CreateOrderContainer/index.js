import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import {get, isEmpty} from 'lodash'
import {
  getContacts,
  getAirports,
  getAcgOrderPrice,
  createAcgOrder,
  getRelatedOrder,
  clearAcgOrderCreatedInfo,
  getOrderDetail as getMwpOrderDetail
} from 'actions'
import {connect} from 'react-redux'
import {browserHistory} from 'react-router'
import message from 'antd/lib/message'
import {Section, ProductList} from 'components'

import Contact from '../../../containers/shared/Contact'
import ShippingInfo from './shippingInfo';
import OrderPriceModal from './OrderPriceModal'
import CalcOrderPriceFailureModal from './CalcOrderPriceFailureModal'
import SupportedDistrictsModal from './SupportedDistrictsModal'
import styles from './index.module.scss'
import {calcPriceMapper, createOrderMapper} from './utils/dataMapper'
import {hasErrorInForm} from './utils/validators'
import {validateCreateForm} from './actionCreators/errors'

class CreateOrderContainer extends Component {
  state = {
    priceModalVisible: false,
    availableDistrictsModalVisible: false,
    calcPriceFailureModalVisible: false
  }

  componentDidMount() {
    const {getAirports, getRelatedOrder, clearAcgOrderCreatedInfo, getMwpOrderDetail} = this.props
    const referenceOrderId = this.getWisePortOrderId()
    clearAcgOrderCreatedInfo()
    getAirports()
    getRelatedOrder(get(this.props, 'location.query', {}))
    if (referenceOrderId) {
      getMwpOrderDetail(referenceOrderId)
    }
  }

  getWisePortOrderId = () => get(this.props, 'location.query.mwpOrderId', null)

  handleSubmit() {
    const params = this.getSpecialParams()
    this.props.createAcgOrder(createOrderMapper({...this.props, ...params}, this.getWisePortOrderId()))
  }

  goBack() {
    history.back()
  }

  showSupportedDistrictsModal = () => {
    this.setState({supportedDistrictsModalVisible: !this.state.supportedDistrictsModalVisible})
  }

  showPriceModal = () => {
    this.setState({priceModalVisible: !this.state.priceModalVisible})
  }

  showCalcPriceFailureModal = () => {
    this.setState({calcPriceFailureModalVisible: !this.state.calcPriceFailureModalVisible})
  }

  getSpecialParams() {
    const shippingInfo = this.shippingInfo.getWrappedInstance()
    return {
      acgPrimaryNum: get(shippingInfo, 'acgPrimaryNum.refs.input.value'),
      agentCode: get(shippingInfo, 'agentCode.refs.input.value')
    }
  }

  confirmOrder = () => {
    const fromMwpOrder = !!this.getWisePortOrderId()
    if (fromMwpOrder && this.props.mwpOrder.referenceOrderId) {
      // alert('该关务服务订单已有关联的航空货运订单')
      browserHistory.push(`/acg/orders/${this.props.mwpOrder.referenceOrderId}`)
    } else if (!isEmpty(this.props.contact)) {
      const hasError = hasErrorInForm(this.props)
      this.props.validateCreateForm(hasError)
      if (!hasError) {
        const params = this.getSpecialParams()
        this.props.getAcgOrderPrice(calcPriceMapper(this.props, params)).then(() => {

          const orderPrice = this.props.orderPrice
          const hasPickUpFee = this.props.shippingInfo.departure.isPickUpOrDropOff && (orderPrice.pickUpFee === 0)
          const hasDropOffFee = this.props.shippingInfo.arrival.isPickUpOrDropOff && (orderPrice.dropOffFee === 0)
          const isCalcPriceFailed = orderPrice.details && orderPrice.details.response && orderPrice.details.response.code === -1

          if (hasPickUpFee || hasDropOffFee) {
            this.showSupportedDistrictsModal()
          } else if (!isEmpty(orderPrice)) {
            if (isCalcPriceFailed) {
              this.showCalcPriceFailureModal()
            } else {
              this.showPriceModal()
            }
          }
        })
      }
    } else {
      message.error('请添加联系人')
    }
  }

  render() {
    const fromMwpOrder = !!this.getWisePortOrderId()
    const {isValidating} = this.props
    const productListProps = {
      fromOtherOrder: fromMwpOrder,
      isValidating
    }

    return (
      <div styleName="create-order" className="container">
        <h1 styleName="title">填写订单信息</h1>

        <Section title="货品列表" hideCrossLine={true}>
          <ProductList {...productListProps} />
        </Section>

        <Section title="航运信息" hideCrossLine={true}>
          <ShippingInfo
            isValidating={isValidating} ref={item => {
              this.shippingInfo = item
            }}/>
        </Section>

        <div styleName="content-wrapper">
          <Contact/>
        </div>

        <div className="operate" styleName="operate">
          <button
            type="button" styleName="button" className="button" onClick={::this.goBack}>取消
          </button>
          <button type="submit" styleName="button primary" className="button primary" onClick={::this.confirmOrder}>确认订单
          </button>
        </div>

        <OrderPriceModal
          title={'订单价格'}
          handleSubmit={::this.handleSubmit}
          onCancel={::this.showPriceModal}
          visible={this.state.priceModalVisible}
          width="400px"
          orderPrice={this.props.orderPrice}
        />

        <CalcOrderPriceFailureModal
          onCancel={::this.showCalcPriceFailureModal}
          visible={this.state.calcPriceFailureModalVisible}
          width="400px"
        />

        <SupportedDistrictsModal
          onCancel={::this.showSupportedDistrictsModal}
          visible={this.state.supportedDistrictsModalVisible}
          width="580px"
          orderPrice={this.props.orderPrice}
          shippingInfo={this.props.shippingInfo}
          contacts={this.props.contacts}
        />
      </div>
    )
  }
}

export default connect(
  state => ({
    ...state.acg.errors,
    productList: state.productList.list,
    shippingInfo: state.acg.shippingInfo,
    contact: state.contact.contact,
    contacts: state.contact.contacts,
    orderPrice: state.acg.orderPrice,
    mwpOrder: state.wisePortOrder.order
  }),
  ({
    clearAcgOrderCreatedInfo,
    createAcgOrder,
    getContacts,
    getAirports,
    getAcgOrderPrice,
    getRelatedOrder,
    validateCreateForm,
    getMwpOrderDetail
  }))(cssModules(CreateOrderContainer, styles, {allowMultiple: true}))
