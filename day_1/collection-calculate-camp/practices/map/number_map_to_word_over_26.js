'use strict';
var convert_number_to_letter = require('../common/convert_number_to_letter');
var number_map_to_word_over_26 = function (collection) {
  return collection.map(ele => convert_number_to_letter(ele));
};

module.exports = number_map_to_word_over_26;
