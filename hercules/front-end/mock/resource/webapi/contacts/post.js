'use strict';

const token = require('entity/user/token');
const contacts = require('entity/contact/contacts');

module.exports = function (req, res, next) {
  res.setHeader("authorization", token);
  res.setHeader("Location", "contacts/" + contacts[9].id);
  res.send(201);
  next();
};
