var split_str_to_and_number = require("../../common/split_str_to_letter_and_number");

function count_same_elements(collection) {
  //在这里写入代码
  return collection.reduce((result, element) => {
    let format_entry = split_str_to_and_number(element);
    let entry = result.find(value => value.name === format_entry.key);
    if (entry)
      entry.summary += format_entry.count;
    else
      result.push({name:format_entry.key,summary:format_entry.count});
    return result;
  }, []);
}

module.exports = count_same_elements;
