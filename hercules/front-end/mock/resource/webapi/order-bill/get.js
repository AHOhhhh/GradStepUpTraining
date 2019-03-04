const orderBills = require('entity/order-bill/order-bill.js');

module.exports = function (req, res, next) {
  res.send(200, orderBills);
  next();
};

