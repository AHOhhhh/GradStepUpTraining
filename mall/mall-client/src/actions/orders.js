import request from 'superagent';
import { getOrderAction, getFormatAction, updateOrderItemAction, deleteOrderItemAction } from '../reducers/reducerActionTypes';
import { ORDER_ID } from '../config/orderIdConfig';

export const loadOrder = () => {
  return dispatch => {
    request.get(`/orders/${ORDER_ID}`).end((err, res) => {
      dispatch(getFormatAction(getOrderAction, formatOrderItem(res.body.orderItems)));
    });
  }
}

export const upateOrderItem = (orderItemId, OrderItemCount) => {
  return dispatch => {
    request.put(`/orders/${ORDER_ID}/orderitems/${orderItemId}`).query({ count: OrderItemCount })
      .end((err, res) => {
        if (res.statusCode === 204) {
          dispatch(getFormatAction(updateOrderItemAction, { orderItemId, OrderItemCount }));
        }
      })
  }
}

export const deleteOrderItem = (orderItemId) => {
  return dispatch => {
    request.delete(`/orders/${ORDER_ID}/orderitems/${orderItemId}`)
      .end((err, res) => {
        if (res.statusCode === 204) {
          dispatch(getFormatAction(deleteOrderItemAction, { orderItemId }))
        }

      })
  }
}


function formatOrderItem(orderItems) {
  return orderItems.map(orderItem => ({
    key: orderItem.key,
    name: orderItem.name,
    unit: orderItem.unit,
    price: orderItem.price,
    count: orderItem.count
  }));
}


