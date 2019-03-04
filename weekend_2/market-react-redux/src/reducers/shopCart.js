import { addItemAction, regularUpdateItemAction, modifyItemAction } from './actionTypes';
const initShopCart = [
  {
    barcode: 'ITEM000000',
    name: '可口可乐',
    price: 3.00,
    count: 1
  },
  {
    barcode: 'ITEM000001',
    name: '雪碧',
    unit: '瓶',
    price: 3.00,
    count: 2
  }
];

const shopCart = (state = initShopCart, action) => {
  let { type, data } = action;
  switch (type) {
    case addItemAction:
      return addItem(state, data);
    case regularUpdateItemAction:
      return regularUpdateItem(state, data);
    case modifyItemAction:
      return modifyItem(state, data);
    default:
      return state;
  }
}

const addItem = (state, newItem) => {
  let newState = state.map(item => Object.assign({}, {...item}));
  let entry = newState.find(item => item.barcode === newItem.barcode);
  if (entry) {
    entry.count += parseInt( newItem.count);
  } else {
    newState.push(newItem);
  }
  return newState;
}

const regularUpdateItem = (state, newItem) => {
  let newState = state.map(item => Object.assign({}, item));
  let entry = newState.find(item => item.barcode === newItem.barcode);
  if (entry) {
    entry.count += parseInt(newItem.count)  ;
  }
  return newState.filter(item => item.count > 0);
}

const modifyItem = (state, newItem) => {
  let newState = state.map(item => Object.assign({}, item));
  let entry = newState.find(item => item.barcode === newItem.barcode);
  if (entry) {
    entry.count = parseInt( newItem.count);
  }
  return newState.filter(item => item.count > 0);
}
export default shopCart;