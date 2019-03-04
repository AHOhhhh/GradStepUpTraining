'use strict';
var rank_desc = function (collection) {
  return collection.sort((a, b) => a - b);

};

module.exports = rank_desc;
