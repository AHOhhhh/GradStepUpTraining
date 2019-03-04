'use strict';
var convert_number_to_letter = require('../../common/convert_number_to_letter');

function average_to_letter(collection) {

  //在这里写入代码
  let sum = collection.reduce((calculate, ele) => calculate + ele);
  let average = Math.ceil(sum / collection.length);
  return convert_number_to_letter(average);

}

module.exports = average_to_letter;

