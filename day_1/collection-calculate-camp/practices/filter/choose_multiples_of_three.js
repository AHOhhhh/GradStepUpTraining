'use strict';

function choose_multiples_of_three(collection) {

  //在这里写入代码
  return collection.filter(ele=>ele%3==0);
}

module.exports = choose_multiples_of_three;
