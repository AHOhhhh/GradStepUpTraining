function create_updated_collection(collection_a, object_b) {
  //在这里写入代码
  return collection_a.map(ele => {
    if (object_b.value.includes(ele.key)) {
      ele.count -= Math.floor(ele.count / 3);
    }
    return ele;
  });
}

module.exports = create_updated_collection;
