const data = require('entity/wisePort/product-offers')

module.exports = function (req, res, next) {
  res.send(200, data);
  next();
};
