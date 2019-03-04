'use strict';

module.exports = function (req, res, next) {
  res.send(204, {});
  next();
};
