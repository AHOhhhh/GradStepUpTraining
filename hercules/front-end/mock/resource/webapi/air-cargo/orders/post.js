'use strict';
const orders = require('entity/air-cargo/orders');

module.exports = function (req, res, next) {
  res.send(200, orders.getOrders());
  next();
};
