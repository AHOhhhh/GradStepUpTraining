'use strict';

var get_integer_interval = require('./get_integer_interval.js');
var convert_integer_to_letter=require('../common/convert_number_to_letter');
function get_letter_interval_2(number_a, number_b) {
  //在这里写入代码
  let numberArr = get_integer_interval(number_a, number_b);
  return numberArr.map(val => convert_integer_to_letter(val))
}

module.exports = get_letter_interval_2;

