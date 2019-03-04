module.exports = function (req, res, next) {
  const shippingInfo = require('entity/air-cargo/airports');

  res.send(200, shippingInfo.getAllAirports());
  next();
}
