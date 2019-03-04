'use strict';
const orders = require('entity/air-cargo/orders');

module.exports = function (req, res, next) {
  const order = orders.getOrderById()
  res.send(200, [
    {
      ...order,
      id: '30483154563441'
    },
    {
      ...order,
      id: '30482665787092'
    }
  ]);
  next();
};
