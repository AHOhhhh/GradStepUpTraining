'use strict';

function get_integer_interval(number_a, number_b) {
  //在这里写入代码
  let result = [];
  let max = number_a > number_b ? number_a : number_b;
  let min = number_b < number_a ? number_b : number_a;

  for (let index = min; index <= max; index++) {
    result.push(index);
  }

  if (max === number_a) {
    result = result.reverse();
  }
  return result;
}

module.exports = get_integer_interval;

