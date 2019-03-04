'use strict';
var even_group_calculate_average = function (collection) {

    let even = collection.filter((element, index) => (index + 1) % 2 === 0 && element % 2 === 0);
    if (even.length === 0)
        return [0];

    let result = even.reduce((array, element) => {
        const length = element.toString().length;
        if (array[length - 1] === undefined)
            array[length - 1] = [element];
        else
            array[length - 1].push(element);
        return array;
    }, []).filter(array => array !== undefined);

    return result.map(array => array.reduce((sum, val) => sum + val) / array.length);

};
module.exports = even_group_calculate_average;
