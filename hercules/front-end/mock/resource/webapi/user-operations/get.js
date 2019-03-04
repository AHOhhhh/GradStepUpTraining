'use strict'
const operations = require('entity/user-operations/operations')

module.exports = function (req, res, next) {
  res.send(200, operations.getOperationsByPage(req.params))
  next()
}