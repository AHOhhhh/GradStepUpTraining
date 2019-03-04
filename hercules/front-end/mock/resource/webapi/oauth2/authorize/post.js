'use strict';

module.exports = function (req, res, next) {
  res.send(201, {
    url: 'https://www.zhihu.com/'
  });
  next();
};
