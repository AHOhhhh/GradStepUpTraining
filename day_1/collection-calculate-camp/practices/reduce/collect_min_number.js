'use strict';

function collect_min_number(collection) {
  //在这里写入代码
  return collection.reduce((min_number, ele) => min_number < ele ? min_number : ele);

}

module.exports = collect_min_number;

