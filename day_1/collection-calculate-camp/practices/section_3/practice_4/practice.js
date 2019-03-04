var count_same_elements = require('../../section_2/practice_1/practice');
var create_updated_collection_2 = require('../practice_2/practice');
function create_updated_collection(collection_a, object_b) {
  //在这里写入代码
  let result = count_same_elements(collection_a);
  return create_updated_collection_2(result,object_b);
}

module.exports = create_updated_collection;
