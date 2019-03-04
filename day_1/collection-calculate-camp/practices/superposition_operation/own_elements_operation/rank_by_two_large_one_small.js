'use strict';
function rank_by_two_large_one_small(collection){
  //这里写代码。。。
  collection.sort((a,b)=>a-b);
  for(let index=0;index<Math.floor(collection.length/3);index++){
    let temp=collection[3*index];
    collection.splice(index*3,1);
    collection.splice(index*3+2,0,temp);
  }
  return collection;

}
module.exports = rank_by_two_large_one_small;
