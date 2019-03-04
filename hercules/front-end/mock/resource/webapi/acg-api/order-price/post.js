module.exports = function (req, res, next) {
  const prices = require('entity/air-cargo/prices');

  res.send(200, prices.getOrderPrice());
  next();
}
