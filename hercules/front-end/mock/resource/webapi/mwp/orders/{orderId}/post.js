'use strict';

const order = require('entity/wisePort/order');

module.exports = function(req, res, next) {

  res.send(200, { ...order, status: 'Cancelled' });
  next();
};
