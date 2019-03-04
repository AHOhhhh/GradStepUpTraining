'use strict';

function amount_even(collection) {

  //在这里写入代码
  return collection.filter(ele => ele % 2 === 0).reduce((sum, val) => sum + val, 0);
}

module.exports = amount_even;
