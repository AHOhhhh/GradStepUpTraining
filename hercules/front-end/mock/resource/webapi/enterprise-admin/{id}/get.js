'use strict';

const normalUser = require('entity/user/user');

module.exports = function(req, res, next) {
  res.send(200, normalUser);
  next();
};
