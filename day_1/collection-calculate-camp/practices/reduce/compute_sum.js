'use strict';

function calculate_elements_sum(collection) {
  //在这里写入代码
  return collection.reduce((sum, ele) => sum + ele);
}

module.exports = calculate_elements_sum;

