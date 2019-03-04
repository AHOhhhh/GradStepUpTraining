'use strict';
var calculate_average = function (collection) {

    let sum = 0;
    let count = 0;
    for (let index = 0; index < collection.length; index++) {
        if ((index + 1) % 2 === 0) {
            sum += collection[index];
            count++;
        }
    }
    return sum / count;
};
module.exports = calculate_average;
