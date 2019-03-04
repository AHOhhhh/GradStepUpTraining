import React, { Component } from 'react';
import { connect } from 'react-redux';
import Table from './EditableTable';
import { loadOrder, upateOrderItem, deleteOrderItem } from '../actions/orders';

class OrderContent extends Component {

  componentDidMount() {
    let { loadOrderHandle } = this.props;
    loadOrderHandle();
  }

  render() {
    let { orderItems, updataOrderItemHandle,deleteOrderItemHandle } = this.props;
    return (
      <div style={{ background: '#fff', padding: 24, minHeight: 600 }}>
        <Table dataSource={orderItems} updataOrderItemHandle={updataOrderItemHandle} deleteOrderItemHandle={deleteOrderItemHandle} >
        </Table>
      </div>
    );
  }

};


const mapStateToProps = ({ orderItems }) => ({
  orderItems
});

const mapDispatchToProps = dispatch => ({
  loadOrderHandle: () => { dispatch(loadOrder()) },
  updataOrderItemHandle: (orderItemId, orderItemCount) => { dispatch(upateOrderItem(orderItemId, orderItemCount)) },
  deleteOrderItemHandle: (orderItemId) => { dispatch(deleteOrderItem(orderItemId)) }
});
export default connect(mapStateToProps, mapDispatchToProps)(OrderContent);