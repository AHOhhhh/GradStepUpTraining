'use strict';

const orders = require('entity/air-cargo/orders');

module.exports = function(req, res, next) {
  const order = orders.getOrderById()
  order.status = 'Cancelled'
  res.send(200, order);
  next();
};
