'use strict'
const orders = require('entity/wms/orders')

module.exports = function (req, res, next) {
  res.send(200, orders.getOrderByStatus('Submitted'))
  next()
}