import loadAllItems from '../database/shopStoreItems';
const initShopItems = loadAllItems();

const shopItems = (state = initShopItems, action) => {
  return state;
}
export default shopItems;