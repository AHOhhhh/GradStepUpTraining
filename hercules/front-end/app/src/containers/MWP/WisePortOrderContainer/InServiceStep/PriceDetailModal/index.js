import React, {Component} from 'react'
import cssModule from 'react-css-modules'

import Modal from 'antd/lib/modal'
import Row from 'antd/lib/row'
import Col from 'antd/lib/col'


import httpClient from '../../../../../utils/http/index'

import styles from './index.module.scss'

import OfferInfo from '../../OfferSelectionStep/OfferCardList/OfferInfo'
import OfferDetail from '../../OfferSelectionStep/OfferCardList/OfferDetail'

class PriceDetailModal extends Component {
  state = {
    visible: false,
    offerDetail: {}
  }
  
  openPriceModal = (orderId, offerId) => {
    httpClient.get(`/mwp/orders/${orderId}/offers/${offerId}`)
      .then(resp => {
        this.setState({
          visible: true,
          offerDetail: resp.data
        })
      })
  }
  
  handleClose = () => {
    this.setState({
      visible: false
    }, () => {
      this.setState({
        offerDetail: {}
      })
    })
  }
  
  render() {
    const {offerDetail} = this.state
    const {companyName, contactNumber} = offerDetail
    
    return (
      <Modal
        width={900}
        visible={this.state.visible}
        onCancel={this.handleClose}
        wrapClassName="read-only-modal"
        footer={<button type="button" className="button primary" onClick={this.handleClose}>关闭</button>}
      >
        <Row gutter={16} styleName="price-detail-modal">
          <Col span={18}>
            <h1 styleName="provider-name">{companyName}</h1>
            <h2 styleName="contact-number">{contactNumber}</h2>
            <OfferInfo offer={offerDetail}/>
          </Col>
          <Col span={6}>
            <OfferDetail items={offerDetail.items}/>
          </Col>
        </Row>
      </Modal>
    )
  }
}

export default cssModule(PriceDetailModal, styles)