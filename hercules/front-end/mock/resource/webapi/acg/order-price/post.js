'use strict';
const prices = require('entity/air-cargo/prices');

module.exports = function (req, res, next) {
  res.send(200, prices);

  next();
};
