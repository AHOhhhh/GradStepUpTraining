'use strict';

const orderLogistics = require('entity/air-cargo/orderLogistics');

module.exports = function(req, res, next) {
  res.send(200, orderLogistics);
  next();
};
