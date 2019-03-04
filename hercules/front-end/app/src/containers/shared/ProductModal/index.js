import {Modal} from 'antd'
import React, {PropTypes} from 'react'
import ProductForm from './productForm'

export default class ProductModal extends React.Component {

  static propTypes = {
    onCloseCallback: PropTypes.func
  }

  constructor(props) {
    super(props)
    this.state = {
      visible: false,
      product: null
    }
  }

  showModal(product) {
    this.setState({
      visible: true,
      product
    })
  }

  handleCancel() {
    this.setState({
      visible: false,
      product: null
    })
  }

  render() {
    const {visible, product} = this.state
    return (
      <Modal
        width={635}
        title={product ? '编辑货品' : '添加货品'}
        visible={visible}
        onCancel={::this.handleCancel}
        maskClosable={false}
        footer={null}
      >
        <ProductForm
          onSubmitCallback={::this.handleCancel} product={this.state.product}
          addProduct={this.props.addProduct} editProduct={this.props.editProduct}/>
      </Modal>
    )
  }
}
