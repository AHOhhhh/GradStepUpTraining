'use strict';
var even_asc_odd_desc = function(collection){
let odds=collection.filter(value=>value%2!==0).sort((a,b)=>b-a);
let evens=collection.filter(value=>value%2===0).sort((a,b)=>a-b);
return evens.concat(odds);
};
module.exports = even_asc_odd_desc;
