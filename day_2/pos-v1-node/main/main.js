var database = require('./datbase');
module.exports = function main(inputs) {
  let items = getItems(inputs);
  let quotation = calculateItems(items);
  let printStr = getPrintQuotation(quotation);
  console.log(printStr);
};


function formatInput(input) {
  const result = input.split('-');
  return { barcode: result[0], count: result.length === 1 ? 1 : parseInt(result[1]) };
}

function getItems(inputs) {
  let items = inputs.reduce((array, input) => {
    const format = formatInput(input);
    let item = array.find(formatInput => formatInput.barcode === format.barcode);
    if (item) {
      item.count += format.count;
    } else {
      array.push(format);
    }
    return array;
  }, []);
  return items;
}

function calculateItems(items) {
  const allItems = database.loadAllItems();
  const saleItems = database.loadPromotions()[0].barcodes;
  let quotation = [];
  items.forEach(item => {
    let itemInfo = allItems.find(itemInfo => item.barcode === itemInfo.barcode);
    let quotationItem = {
      name: itemInfo.name,
      count: item.count,
      saleCount: 0,
      unit: itemInfo.unit,
      price: itemInfo.price
    };
    if (saleItems.includes(item.barcode)) {
      quotationItem.saleCount = Math.floor(quotationItem.count / 3);
    }
    quotation.push(quotationItem);
  });
  return quotation;
}

function getPrintQuotation(quotation) {
  let [printString, sumPrice, reducePrice] = [`***<没钱赚商店>购物清单***\n`, 0, 0];
  quotation.forEach(item => {
    let itemPrice = (item.count - item.saleCount) * item.price;
    sumPrice += itemPrice;
    reducePrice += item.saleCount * item.price;
    printString += `名称：${item.name}，数量：${item.count}${item.unit}，单价：${item.price.toFixed(2)}(元)，小计：${itemPrice.toFixed(2)}(元)\n`;
  });
  printString += `----------------------\n`;

  let reduceQuotaion = quotation.filter(item => item.saleCount !== 0);
  if (reduceQuotaion.length !== 0) {
    printString += `挥泪赠送商品：\n`;
    reduceQuotaion.forEach(item => {
      printString += `名称：${item.name}，数量：${item.saleCount}${item.unit}\n`;
    });
    printString += `----------------------\n`;
  }

  printString += `总计：${sumPrice.toFixed(2)}(元)\n` + `节省：${reducePrice.toFixed(2)}(元)\n` + `**********************`;
  return printString;
}

