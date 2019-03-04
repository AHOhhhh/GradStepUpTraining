'use strict';
var single_element = function (collection) {
    let evens = collection.filter((element, index) => (index + 1) % 2 === 0);
    let [result, repeat] = [[], []];
    for (const value of evens) {
        if (repeat.includes(value))
            continue;
        if (!result.includes(value)) {
            result.push(value);
        }
        else {
            result.splice(result.indexOf(value),1);
            repeat.push(value);
        }
    }
    return result;
};
module.exports = single_element;
