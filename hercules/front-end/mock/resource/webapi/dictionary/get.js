const declarePort = require('entity/dictionary/declarePort')
const priceUnit = require('entity/dictionary/priceUnit')
const productName = require('entity/dictionary/productName')
const supervision = require('entity/dictionary/supervision')
const transportType = require('entity/dictionary/transportType')

const dictionary = {
  productName: productName,
  priceUnit: priceUnit,
  ports: declarePort,
  supervisionType: supervision,
  transportType: transportType
}

module.exports = function (req, res, next) {

  const {code} = req.params
  res.send(200, dictionary[code]);
  next();
};
