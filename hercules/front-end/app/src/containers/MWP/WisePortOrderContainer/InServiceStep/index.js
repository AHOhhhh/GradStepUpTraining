import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import cssModule from 'react-css-modules'
import {Card} from 'antd'
import {map, isEmpty, result, includes} from 'lodash'
import SSOUploadButton from 'components/SSOUploadButton'
import CancelOrderButton from 'components/CancelOrderButton'

import * as actions from './actions'
import styles from './index.module.scss'

import Loading from './Loading'
import ProvidedProductList from './ProvidedProductList'
import PriceDetailModal from './PriceDetailModal'

import WisePortTimeLine from '../../share/components/WisePortTimeLine'
import OrderDetailPanel from '../../share/components/OrderDetailPanel'
import OrderDetailStatus from '../../../shared/OrderDetailStatus'
import WISE_PORT_SERVICE_TYPE from '../../share/constants/serviceType.constant'
import {renderReferenceAdBanner, renderReferenceOrderBanner} from '../../share/components/AdContainer'

class InServiceStep extends Component {
  openPriceModal = (orderId, companyId) => {
    this.refs.priceDetailModal.openPriceModal(orderId, companyId)
  }

  componentDidMount() {
    if (this.props.order.id) {
      this.props.actions.getInServiceProductOffers(this.props.order.id)
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.order.id && nextProps.order.id !== this.props.order.id) {
      nextProps.actions.getInServiceProductOffers(nextProps.order.id)
    }
  }

  hanleRefresh = () => {
    this.props.actions.getInServiceProductOffers(this.props.order.id)
  }

  groupProductsByServiceType = products => {
    const goods = {}
    if (isEmpty(products)) return goods

    goods.clearance = this.getProductsByServiceType(products, WISE_PORT_SERVICE_TYPE.clearance.code)
    goods.inspection = this.getProductsByServiceType(products, WISE_PORT_SERVICE_TYPE.inspection.code)
    goods.agent = this.getProductsByServiceType(products, WISE_PORT_SERVICE_TYPE.agent.code)

    map(goods, groupedGoods => {
      return this.addKeyForGoods(groupedGoods)
    })

    return goods
  }

  addKeyForGoods = goods => {
    goods.forEach((good, index) => {
      good.key = index
    })
  }

  getProductsByServiceType = (products, type) => {
    return products.filter(product => {
      return includes(product.service, type)
    })
  }

  renderReferenceAd(order, preview) {
    if (preview) {
      return null
    }
    return renderReferenceAdBanner(this.props.order)
  }

  renderReferenceOrder(order, preview) {
    if (preview) {
      return null
    }
    return renderReferenceOrderBanner(order)
  }

  renderUploadButton(preview, offer) {
    if (preview) {
      return null
    }
    return (
      <div styleName="sso-button">
        <SSOUploadButton
          id={offer.delegateAssociationOrderId}
          businessLine="mwp_sub"
          buttonClass=""
          buttonStyle="upload-button"
        >上传资料</SSOUploadButton>
      </div>
    )
  }


  renderOrderDetailStatus(preview, order, data, current) {
    if (preview) {
      return <OrderDetailStatus order={order} data={data} current={current}/>
    }
    return null
  }

  renderCancelButton(order) {
    return (
      CancelOrderButton && <CancelOrderButton order={order}/>
    )
  }

  getWayBillInfo(serviceGoods) {
    const goodInfoList = []
    serviceGoods.forEach((serviceGood, index) => {
      serviceGood.goods.forEach((good, i) => {
        const service = {...serviceGood}
        delete service.goods
        goodInfoList.push(Object.assign({}, service, {good}, {key: `${index}_${i}`}))
      })
    })


    return goodInfoList
  }

  render() {
    const preview = this.props.preview
    const productsGroupByOffer = this.props.products || []
    const orderId = result(this.props.order, 'id')
    const order = this.props.order
    const offerWithGoods = productsGroupByOffer

    return (
      <div styleName="step-in-service">
        <div styleName="main-section">
          {this.renderReferenceAd(order, preview)}
          {
            offerWithGoods.length === 0 ?
              <Loading orderId={orderId}/> :
            productsGroupByOffer.map(offer => {
              return (
                <Card key={offer.companyId} style={{width: 910}}>
                  <h1 styleName="provider-name">
                    {offer.companyName}
                    <span styleName="price-detail-toogle" onClick={() => this.openPriceModal(orderId, offer.offerId)}>报价详情</span>
                    {this.renderUploadButton(preview, offer)}
                  </h1>
                  {
                    isEmpty(offer.waybills) ?
                      <div styleName="refresh-operation">
                        <div><span> 服务商即将为您服务，请先上传资料</span></div>
                        <span styleName="refresh-button" onClick={this.hanleRefresh}>刷新</span>
                        <span> , 查看状态更新</span>
                      </div> :
                      map(this.groupProductsByServiceType(offer.waybills), (serviceGoods, key) => {
                        const serviceType = key
                        return isEmpty(serviceGoods) ? null :
                        <ProvidedProductList
                          key={serviceType} dataArray={this.getWayBillInfo(serviceGoods)}
                          type={serviceType}/>
                      })
                  }
                </Card>
              )
            })
          }
          <WisePortTimeLine operationLogs={result(this.props.order, 'operationLogs', [])}/>
          {this.renderReferenceOrder(order, preview)}
        </div>
        <div styleName="right-side-bar">
          {this.renderOrderDetailStatus(preview, order, this.props.data, this.props.current)}
          <OrderDetailPanel detail={order} preview={preview}/>
          {!preview && this.renderCancelButton(order)}
        </div>
        <PriceDetailModal ref="priceDetailModal"/>
      </div>
    )
  }
}

export default connect(state => ({
  products: state.wisePortOrder.inService.products
}), dispatch => ({
  actions: bindActionCreators(actions, dispatch)
}))(cssModule(InServiceStep, styles))

