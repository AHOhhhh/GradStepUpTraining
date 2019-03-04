import { getOrderAction, updateOrderItemAction, deleteOrderItemAction } from './reducerActionTypes';
const initState = [];
const orderItems = (state = initState, action) => {
  let { type, data } = action;
  switch (type) {
    case getOrderAction:
      return data;
    case updateOrderItemAction:
      return state.map(orderItem => {
        if (orderItem.key === data.orderItemId) {
          return Object.assign({}, orderItem, { count: data.OrderItemCount });
        }
        return orderItem;
      })
    case deleteOrderItemAction:
      return state.filter(orderItem => orderItem.key !== data.orderItemId);
    default:
      return state;
  }

}
export default orderItems;