'use strict';

function hybrid_operation_to_uneven(collection) {

  //在这里写入代码
  return collection.filter(ele => ele % 2 !== 0).map(ele => ele * 3 + 5).reduce((sum, val) => sum + val, 0);
}

module.exports = hybrid_operation_to_uneven;

