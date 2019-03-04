'use strict';

const token = require('entity/user/token');
const normalUser = require('entity/user/user');

module.exports = function (req, res, next) {
  res.setHeader("authorization", token);
  res.send(201, normalUser);
  next();
};
