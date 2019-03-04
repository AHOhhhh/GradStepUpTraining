import request from 'superagent';
import { getAllProductsAction, getFormatAction } from '../reducers/reducerActionTypes';
export const loadAllProducts = () => {
  return dispatch => {
    request.get('/products').end((err, res) => {
      dispatch(getFormatAction(getAllProductsAction, res.body));
    });
  }
}

export const addOrderItem = (orderId, addOrderItemRequest, name, showNotice) => {
  return dispatch => {
    request.post(`/orders/${orderId}/orderitems`)
      .set('Content-Type', 'application/json')
      .send(addOrderItemRequest).end((err, res) => {
        if (res.statusCode === 201) {
          showNotice(name);
        }
      });
  }
}