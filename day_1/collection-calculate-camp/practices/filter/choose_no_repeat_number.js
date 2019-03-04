'use strict';

function choose_no_repeat_number(collection) {

  //在这里写入代码
  let result = [];
  collection.forEach(element => {
    if (!result.includes(element)) {
      result.push(element);
    }
  });
  return result;

}

module.exports = choose_no_repeat_number;
