'use strict';

function split_str_to_letter_and_number(str) {
    let pattern = /^([a-z])(\W(\d+))?/;
    let array = str.match(pattern);
    return { key: array[1], count: array[3] !== undefined ? parseInt(array[3]) : 1 };
}

module.exports = split_str_to_letter_and_number;