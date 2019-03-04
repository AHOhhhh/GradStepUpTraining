'use strict';

const data = require('entity/user/enterprises');

module.exports = function (req, res, next) {
  // res.sendStatus(404);
  res.send(200, data.getEnterpriseInfoByStatus('AuthorizationInProcess'));
  next();
};
