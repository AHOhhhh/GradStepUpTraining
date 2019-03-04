import React, { Component } from 'react';
import { Row } from 'antd';
import { connect } from 'react-redux';
import { loadAllProducts, addOrderItem } from '../actions/products';
import Product from './Product';


class ProductContent extends Component {


  componentWillMount() {
    let { loadAllProductsHandle } = this.props;
    loadAllProductsHandle();
  }

  render() {
    let { products, addOrderItemHandle } = this.props;
    return (
      <div style={{ background: '#fff', padding: 24, minHeight: 600 }}>
        <Row gutter={16}>
          {products.map(product => <Product {...product} key={product.id}  addOrderItemHandle={addOrderItemHandle} />)}
        </Row>
      </div>
    );
  }

};


const mapStateToProps = ({ products }) => ({
  products
});

const mapDispatchToProps = dispatch => ({
  loadAllProductsHandle: () => { dispatch(loadAllProducts()) },
  addOrderItemHandle: (orderId, addOrderItemRequest, name, showNotice) => { dispatch(addOrderItem(orderId, addOrderItemRequest, name, showNotice)) }
});


export default connect(mapStateToProps, mapDispatchToProps)(ProductContent);
