'use strict';
var is_exist_element = function (collection, element) {
    return collection.filter((value, index) => index % 2 === 0).includes(element);
};
module.exports = is_exist_element;
