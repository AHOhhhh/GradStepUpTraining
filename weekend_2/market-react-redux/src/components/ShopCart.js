import React from 'react';
import { connect } from 'react-redux';
import { Table } from 'reactstrap';
import ShopCartItem from './ShopCartItem';
import { regularUpdateItemAction, modifyItemAction, getAction } from '../reducers/actionTypes';
const ShopCart = ({ shopCart, handleRegularUpdateCount, handleModifyCount }) => {
  return (<Table size="sm" hover className="table" >
    <thead>
      <tr>
        <th>#</th>
        <th>名字</th>
        <th>单价</th>
        <th>数量</th>
        <th>合计</th>
      </tr>
    </thead>
    <tbody>
      {shopCart.map(cartItem => <ShopCartItem {...cartItem} key={cartItem.barcode} handleRegularUpdateCount={handleRegularUpdateCount} handleModifyCount={handleModifyCount} />)}
    </tbody>
  </Table>);
}

const mapStateToProps = ({ shopCart }) => ({
  shopCart
});
const mapDispatchToProps = dispatch => ({
  handleRegularUpdateCount: (item) => dispatch(getAction(regularUpdateItemAction, item)),
  handleModifyCount: (item) => dispatch(getAction(modifyItemAction, item))
});

export default connect(mapStateToProps, mapDispatchToProps)(ShopCart);