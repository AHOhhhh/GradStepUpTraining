'use strict';

function collect_max_number(collection) {
  //在这里写入代码
  return collection.reduce((max_number, ele) => max_number > ele ? max_number : ele);
}

module.exports = collect_max_number;
