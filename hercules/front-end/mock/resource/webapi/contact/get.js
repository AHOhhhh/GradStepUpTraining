'use strict'

const contacts = require('entity/contact/contacts')

module.exports = function (req, res, next) {
  res.send(200, contacts)
  next()
}
