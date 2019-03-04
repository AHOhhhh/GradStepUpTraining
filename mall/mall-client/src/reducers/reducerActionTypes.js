export const getAllProductsAction = "GET_ALL_PRODUCTS";
export const getOrderAction="GET_ORDER";
export const updateOrderItemAction="UPDATE_ORDERITEM";
export const deleteOrderItemAction="DELETE_ORDERITEM";
export function getFormatAction(type, data) {
  return { type, data };
}