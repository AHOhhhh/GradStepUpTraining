import {Modal} from 'antd'

import React, {Component} from 'react'
import cssModules from 'react-css-modules'

import ReviewEnterpriseForm from './ReviewEnterpriseForm'

import styles from './index.module.scss'


class ReviewEnterpriseButton extends Component {
  
  state = {
    visible: false
  }

  showModal = () => {
    this.setState({
      visible: true,
    })
  }

  handleClose = () => {
    this.setState({
      visible: false
    })
  }
  
  render() {
    const {visible} = this.state
    return (
      <span className="review-enterprise-button-wrapper">
        <button className="button primary" onClick={this.showModal}>审核</button>
        <Modal
          width={535}
          title="审核企业资质"
          visible={visible}
          onCancel={this.handleClose}
          maskClosable={false}
          footer={null}
        >
          <div className={styles['review-enterprise-form']}>
            <ReviewEnterpriseForm onCancel={this.handleClose} enterprise={this.props.enterprise} />
          </div>
  
        </Modal>
      </span>
      
    )
  }
}

export default cssModules(ReviewEnterpriseButton, styles)