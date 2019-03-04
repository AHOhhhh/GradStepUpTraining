'use strict';

function average_uneven(collection) {

  //在这里写入代码
  let odd = collection.filter(ele => ele % 2 !== 0);
  return odd.reduce((calculate, val) => calculate + val, 0) / odd.length;
}

module.exports = average_uneven;
