'use strict';

const user = require('entity/user/user')

module.exports = function (req, res, next) {
  res.send(200, [user]);
  next();
};
