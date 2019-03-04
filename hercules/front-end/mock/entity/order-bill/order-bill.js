/* eslint-disable quote-props,quotes */
const generateOrderBills = () => {
  const orderBills = []
  for (let i = 1; i < 21; i++) {
    const orderBill = {
      "id": i,
      "orderId": i,
      "orderType": "acg",
      "vendor": "vendor",
      "payMethod": "线上",
      "payChannel": "平台",
      "productCharge": 0,
      "serviceCharge": 0,
      "commissionCharge": 0,
      "productChargeSettled": false,
      "serviceChargeSettled": false,
      "commissionChargeSettled": false
    }
    orderBills.push(orderBill)
  }
  return orderBills
}
module.exports = {
  "content": generateOrderBills(),
  "last": false,
  "totalElements": 21,
  "totalPages": 2,
  "size": 20,
  "number": 0,
  "sort": [{
    "direction": "DESC",
    "property": "createdAt",
    "ignoreCase": false,
    "nullHandling": "NATIVE",
    "ascending": false,
    "descending": true
  }],
  "first": true,
  "numberOfElements": 20
}