import React from 'react';
import { Row} from 'reactstrap';
import { connect } from 'react-redux';
import ShopItem from './ShopItem';
import {addItemAction,getAction} from '../reducers/actionTypes';
const ShopCart = ({ shopItems,handleAddItem}) => {
  console.log('shopcart');
  return (
    <Row>
    {shopItems.map(item=><ShopItem key={item.barcode}   {...item} handleAddItem={handleAddItem}/>)}
    </Row>
  );
}

const mapStateToProps = ({ shopItems }) => ({
  shopItems
});

const mapDispatchToProps = dispatch => ({
handleAddItem:(item)=>dispatch(getAction(addItemAction,item))
});

export default connect(mapStateToProps, mapDispatchToProps)(ShopCart);