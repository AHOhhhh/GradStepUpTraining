import React, { Component } from 'react'
import cssModules from 'react-css-modules'
import { connect } from 'react-redux';
import Table from 'antd/lib/table'
import Modal from 'antd/lib/modal'
import Form from 'antd/lib/form'
import { get, isNumber } from 'lodash'
import currencyFormatter from 'currency-formatter'
import {addProduct, editProduct, deleteProduct} from 'actions'
import TableOperation from './TableOperation'
import ProductModal from '../../containers/shared/ProductModal'
import styles from './index.module.scss'
import ModalFooterButtons from '../../containers/shared/ModalFooterButtons'
import { validateProducts } from '../../containers/ACG/CreateOrderContainer/utils/validators'

class ProductList extends Component {
  state = {
    showDeleteConfirm: false,
    currentProductIndex: -1
  }

  showProductModal(record, e) {
    e.preventDefault()
    this.refs.productModal.showModal(record)
    if (record) {
      this.setState({currentProductIndex: record.index})
    }
  }

  showDeleteConfirmModal(record, e) {
    e.preventDefault()
    this.setState({
      showDeleteConfirm: true,
      currentProductIndex: isNumber(record.index) ? record.index : -1
    })
  }

  deleteProduct() {
    this.props.deleteProduct(this.state.currentProductIndex)
    this.setState({currentProductIndex: null, showDeleteConfirm: false})
  }

  addProduct(values) {
    this.props.addProduct(values)
  }

  editProduct(values) {
    this.props.editProduct(values, this.state.currentProductIndex)
    this.setState({currentProductIndex: null})
  }

  hideDeleteConfirmModal(record) {
    this.setState({
      showDeleteConfirm: false,
      currentProductIndex: record.index
    })
  }

  render() {
    const {productList, fromOtherOrder, isValidating} = this.props
    const productListErrors = isValidating ? get(validateProducts(productList), 'products[0]', {}) : {}

    const columns = [{
      title: '名称',
      dataIndex: 'name',
      key: 'name',
      width: 180
    }, {
      title: '重量(千克/Kg)',
      dataIndex: 'grossWeight',
      key: 'grossWeight',
      width: 120
    }, {
      title: '体积',
      dataIndex: 'volume',
      key: 'volume',
      width: 140,
      render: (text, record) => (
        <span>{record.length}×{record.width}×{record.height}</span>
      )
    }, {
      title: '单价',
      dataIndex: 'unitPrice',
      key: 'unitPrice',
      render: (text, record) => (
        record.unitPrice ? currencyFormatter.format(record.unitPrice, {code: record.currency}) : '--'
      )
    }, {
      title: '总数',
      dataIndex: 'totalAmount',
      key: 'totalAmount'
    }, {
      title: '包装数量',
      dataIndex: 'packageAmount',
      key: 'packageAmount'
    }, {
      title: '申报要素',
      dataIndex: 'description',
      key: 'description',
      width: 180
    }, {
      title: '操作',
      key: 'action',
      width: 180,
      render: (text, record) => {
        return (
          fromOtherOrder
            ? null
            : <TableOperation
              record={record}
              showProductModal={::this.showProductModal}
              showDeleteConfirmModal={::this.showDeleteConfirmModal}
            />
        )
      }
    }]

    return (
      <div styleName="product-list">
        <div>
          <Table
            columns={columns}
            dataSource={productList}
            rowKey={(record, index) => record.hsCode || `product_${index}`}
            pagination={false}
          />
        </div>
        {!fromOtherOrder && (
          <Form.Item styleName="add-product" {...productListErrors}>
            <a onClick={this.showProductModal.bind(this, null)}>添加货品</a>
          </Form.Item>)}
        <ProductModal ref="productModal" addProduct={::this.addProduct} editProduct={::this.editProduct}/>
        <Modal
          title="删除货品"
          visible={this.state.showDeleteConfirm}
          footer={<ModalFooterButtons
            okText="确定" cancelText="取消" onOk={::this.deleteProduct} onCancel={::this.hideDeleteConfirmModal}/>}
        >
          <p styleName="confirm-text">确定要删除该货品?</p>
        </Modal>
      </div>
    )
  }
}

const mapStateToProps = state => ({
  productList: state.productList.list,
})

const mapDispatchToProps = {
  addProduct,
  editProduct,
  deleteProduct,
}

export default connect(mapStateToProps, mapDispatchToProps)(cssModules(ProductList, styles, {allowMultiple: true}))
