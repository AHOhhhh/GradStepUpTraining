'use strict';

module.exports = function(req, res, next) {
  res.send(202);
  next();
};
