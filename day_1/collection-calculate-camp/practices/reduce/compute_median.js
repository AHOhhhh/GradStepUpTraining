'use strict';

function compute_median(collection) {
  //在这里写入代码
  let array = collection.sort((a, b) => a - b);
  let length = array.length;
  if (length % 2 == 0)
    return (array[length / 2] + array[length / 2 - 1]) / 2;
  else
    return array[Math.floor(length / 2)];
}

module.exports = compute_median;


