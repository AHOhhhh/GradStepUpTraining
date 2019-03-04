'use strict';
var convert_number_to_letter = require('../../common/convert_number_to_letter');

function median_to_letter(collection) {

  //在这里写入代码
  collection.sort((a, b) => a - b);
  const length = collection.length;
  let median = 0;
  if (length % 2 === 0)
    median = Math.ceil((collection[length / 2] + collection[length / 2 - 1]) / 2);
  else
    median = collection[Math.floor(length / 2)];
  return convert_number_to_letter(median);
}

module.exports = median_to_letter;
