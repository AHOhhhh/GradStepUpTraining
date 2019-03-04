import { combineReducers } from 'redux';
import products from './products';
import orderItems from './orderItems';
const reducers = combineReducers({
  products,
  orderItems
});

export default reducers;