'use strict';

function double_to_one(collection) {

  //在这里写入代码
  let result = [];
  for (const iterator of collection) {
    result = result.concat(iterator);
  }
  return result;
}

module.exports = double_to_one;
