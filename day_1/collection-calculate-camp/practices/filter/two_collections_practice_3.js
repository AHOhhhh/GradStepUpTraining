'use strict';

function choose_divisible_integer(collection_a, collection_b) {

  //在这里写入代码
  return collection_a.filter(ele => {
    for (const divisor of collection_b) {
      if (ele % divisor == 0) {
        return true;
      }
    }
    return false;
  });

}

module.exports = choose_divisible_integer;
