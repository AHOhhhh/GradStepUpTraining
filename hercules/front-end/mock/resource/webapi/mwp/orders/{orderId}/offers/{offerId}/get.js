const data = require('entity/wisePort/offer.js')

module.exports = function (req, res, next) {
  res.send(200, data);
  next();
};