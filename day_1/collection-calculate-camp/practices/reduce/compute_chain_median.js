'use strict';

function compute_chain_median(collection) {
  //在这里写入代码
  const array = collection.split('->').map(ele => parseInt(ele, 10)).sort((a, b) => a - b);
  const length = array.length;
  if (length % 2 === 0)
    return (array[length / 2] + array[length / 2 - 1]) / 2;

  return array[length / 2];
}

module.exports = compute_chain_median;
