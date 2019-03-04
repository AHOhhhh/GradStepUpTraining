import { getAllProductsAction } from './reducerActionTypes';

const initState = [];
const products = (state = initState, action) => {
  let { type, data } = action;
  switch (type) {
    case getAllProductsAction:
      return data;
    default:
      return state;
  }

}
export default products;