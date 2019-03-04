import { combineReducers } from 'redux';
import shopItems from './shopItems';
import shopCart from './shopCart';

const reducers = combineReducers({
  shopItems,
  shopCart
});

export default reducers;