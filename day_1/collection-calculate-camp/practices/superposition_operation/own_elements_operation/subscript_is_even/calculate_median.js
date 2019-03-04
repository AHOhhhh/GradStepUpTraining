'use strict';
var calculate_median = function (collection) {

    const even_collection = collection.filter((element, index) => (index + 1) % 2 === 0);
    const length = even_collection.length;
    if (length % 2 === 0)
        return (even_collection[length / 2] + even_collection[length / 2 - 1]) / 2;

    return even_collection[Math.floor(length / 2)];
};
module.exports = calculate_median;
