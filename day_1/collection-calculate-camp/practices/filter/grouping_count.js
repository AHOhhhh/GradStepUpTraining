'use strict';

function grouping_count(collection) {

  //在这里写入代码
  return collection.reduce((result, ele) => {
    let str = ele.toString();
    if (result.hasOwnProperty(str)) {
      result[str]++;
    }
    else {
      result[str] = 1;
    }
    return result;
  }, {})
}

module.exports = grouping_count;
