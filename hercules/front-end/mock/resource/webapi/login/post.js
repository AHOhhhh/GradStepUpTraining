'use strict';
const token = require('entity/user/token');

module.exports = function (req, res, next) {
  res.setHeader("authorization", token);
  res.send(200, {});
  next();
};
