'use strict';
function convert_integer_to_letter(number) {
    let letter = '';
    while (number != 0) {
        let current = number % 26;
        current = current == 0 ? 26 : current;
        letter = String.fromCharCode(96 + current) + letter;
        number = (number - current) / 26;
    }
    return letter;
}


module.exports = convert_integer_to_letter;
