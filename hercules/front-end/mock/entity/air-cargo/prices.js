const prices = {
  "airlineFee": 4000.00,
  "pickupFee": 500.00,
  "deliveryFee": 300.00,
  "total": 4800.00
}

const supportedDistricts = {
  "pickUpFee":0,
  "airlineFee":0,
  "dropOffFee":100.00,
  "total":600.00,
  "supportedPickupDistrict":[{
    "city": "北京市",
    "district": "东城区"
  }],
  "supportedDeliveryDistrict":[{
    "city": "深圳市",
    "district": "福田区"
  }]
}

module.exports = {
  getOrderPrice: () => {
    return supportedDistricts
  }
}