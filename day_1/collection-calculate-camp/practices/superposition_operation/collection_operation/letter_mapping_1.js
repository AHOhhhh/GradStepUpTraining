'use strict';
var convert_number_to_letter = require('../../common/convert_number_to_letter');
function even_to_letter(collection) {

  //在这里写入代码
  return collection.filter(ele => ele % 2 === 0).map(ele => convert_number_to_letter(ele));
}

module.exports = even_to_letter;
