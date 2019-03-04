module.exports = function (req, res, next) {
  const orders = require('entity/air-cargo/orders');

  res.send(200, orders.getPaymentStatus());
  next();
}
