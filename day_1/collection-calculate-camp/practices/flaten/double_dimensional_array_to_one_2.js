'use strict';

var double_to_one_1 = require('./double_dimensional_array_to_one_1');
var choose_no_repeat_number = require("../filter/choose_no_repeat_number")
function double_to_one(collection) {

  //在这里写入代码
  let result = double_to_one_1(collection);
  return choose_no_repeat_number(result);

}

module.exports = double_to_one;
