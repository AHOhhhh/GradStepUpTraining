'use strict';

function get_union(collection_a, collection_b) {
  //在这里写入代码
  return collection_b.reduce((arr, val) => {
    if (arr.indexOf(val) === -1)
      arr.push(val);
    return arr;
  }, collection_a);
}

module.exports = get_union;

