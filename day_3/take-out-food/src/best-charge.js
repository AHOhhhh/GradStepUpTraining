var loadAllItems = require('./items');
var loadPromotions = require('./promotions');
const MONEYOFFMEASURE = 30;
module.exports = function bestCharge(selectedItems) {
  let formatItems = getFormatItems(selectedItems);
  let quotataion = getQuotation(formatItems);
  return getPrint(quotataion);
}

function getFormatItems(selectedItems) {
  return selectedItems.map(item => {
    let array = item.split(' x ');
    return { id: array[0], count: parseInt(array[1]) };
  })
}

function getQuotation(formatItems) {
  const allItems = loadAllItems();
  const allHalfPricePromotions = loadPromotions()[1].items;
  return formatItems.map(formatItem => {
    let itemInfo = allItems.find(item => item.id === formatItem.id);
    formatItem.name = itemInfo.name;
    formatItem.price = itemInfo.price;
    formatItem.halfPrice = false;
    if (allHalfPricePromotions.includes(formatItem.id)) {
      formatItem.halfPrice = true;
    }
    return formatItem;
  })
}

function getPrint(quotation) {
  let print = `============= 订餐明细 =============\n`;
  let totalPrice = 0;
  quotation.forEach(item => {
    let sum = item.price * item.count;
    totalPrice += sum;
    print += `${item.name} x ${item.count} = ${sum}元\n`;
  });
  print += `-----------------------------------\n`;

  let [saleOfMoneyOff, saleOfHalfMoney] = [0, 0];

  saleOfMoneyOff = totalPrice >= MONEYOFFMEASURE ? 6 : 0;

  let halfPriceItems = quotation.filter(item => item.halfPrice);
  saleOfHalfMoney = halfPriceItems.reduce((sum, item) => sum + (item.count * item.price) / 2, 0);

  if (saleOfMoneyOff >= saleOfHalfMoney && saleOfMoneyOff !== 0) {
    print += `使用优惠:\n满30减6元，省6元\n-----------------------------------\n`;
  } else if (saleOfHalfMoney !== 0) {
    print += `使用优惠:\n指定菜品半价(${halfPriceItems.map(item => item.name).join('，')})，省${saleOfHalfMoney}元\n-----------------------------------\n`;
  }
  print += `总计：${totalPrice - (saleOfMoneyOff >= saleOfHalfMoney ? saleOfMoneyOff : saleOfHalfMoney)}元\n===================================`;
  return print;
}

