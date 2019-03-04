'use strict'
const orders = require('entity/wms/orders')

module.exports = function (req, res, next) {
  if (req.params.id) {
    res.send(200, orders.getOrderByStatus('Submitted'))
  }
  else if (req.params.page) {
    res.send(200, orders.gerOrderbyPage(req.params.status))
  }
  else {
    res.send(200, orders.getOrdersbyUserId())
  }
  next()
}