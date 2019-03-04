'use strict';

function compute_average(collection) {
  //在这里写入代码
  let sum = collection.reduce((calculate, ele) => calculate + ele);
  return sum / collection.length;
}

module.exports = compute_average;

