'use strict';

function compare_collections(collection_a, collection_b) {
  //在这里写入代码
  for (let index = 0; index < collection_a.length; index++) {
    if (collection_a[index] !== collection_b[index])
      return false;
  }
  return true;
}

module.exports = compare_collections;


