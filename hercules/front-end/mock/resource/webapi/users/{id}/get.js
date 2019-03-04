'use strict';
const user = require('entity/user/userById')

module.exports = function (req, res, next) {
  res.send(200, user.getUserById());
  next();
};
