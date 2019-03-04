'use strict';
function one_add_next_multiply_three(collection) {
  let result = [];
  for (let index = 0; index < collection.length - 1; index++) {
    result.push((collection[index] + collection[index + 1]) * 3);
  }
  return result;
}
module.exports = one_add_next_multiply_three;
