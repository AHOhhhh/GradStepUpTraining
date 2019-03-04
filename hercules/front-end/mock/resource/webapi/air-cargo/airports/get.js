'use strict';
const airports = require('entity/air-cargo/airports');

module.exports = function (req, res, next) {
  res.send(200, airports.getAllAirports());

  next();
};
