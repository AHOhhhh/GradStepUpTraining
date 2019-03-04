const airports = [
  {
    "city": "安康",
    "name": "AnKang",
    "airportId": "AKA",
    "airportName": "安康",
    "pickup": true,
    "delivery": true,
    "abroad": false,
    "createdAt": 1513747449,
    "updatedAt": 1513747449,
    "createdBy": "708ccc12-bd0c-3ba2-8754-c3e39a5dd62a",
    "updatedBy": "708ccc12-bd0c-3ba2-8754-c3e39a5dd62a"
  },
  {
    "city": "成都",
    "name": "ChengDu",
    "airportId": "CTU",
    "airportName": "成都",
    "pickup": true,
    "delivery": true,
    "abroad": false,
    "createdAt": 1513747450,
    "updatedAt": 1513747450,
    "createdBy": "708ccc12-bd0c-3ba2-8754-c3e39a5dd62a",
    "updatedBy": "708ccc12-bd0c-3ba2-8754-c3e39a5dd62a"
  },
  {
    "city": "爱丁堡",
    "name": "AiDingBao",
    "airportId": "AAT",
    "airportName": "爱丁堡",
    "pickup": false,
    "delivery": false,
    "abroad": true,
    "createdAt": 1513682766,
    "updatedAt": 1513683487,
    "createdBy": "708ccc12-bd0c-3ba2-8754-c3e39a5dd62a",
    "updatedBy": "708ccc12-bd0c-3ba2-8754-c3e39a5dd62a"
  },
  {
    "city": "阿比让",
    "name": "abirang",
    "airportId": "ABJ",
    "airportName": "阿比让",
    "pickup": false,
    "delivery": false,
    "abroad": true,
    "createdAt": 1513682766,
    "updatedAt": 1513682766,
    "createdBy": "708ccc12-bd0c-3ba2-8754-c3e39a5dd62a",
    "updatedBy": "708ccc12-bd0c-3ba2-8754-c3e39a5dd62a"
  },
  {
    "city": "阿布贾",
    "name": "abujia",
    "airportId": "ABV",
    "airportName": "阿布贾",
    "pickup": false,
    "delivery": false,
    "abroad": true,
    "createdAt": 1513682766,
    "updatedAt": 1513682766,
    "createdBy": "708ccc12-bd0c-3ba2-8754-c3e39a5dd62a",
    "updatedBy": "708ccc12-bd0c-3ba2-8754-c3e39a5dd62a"
  },
  {
    "city": "阿克拉",
    "name": "akela",
    "airportId": "ACC",
    "airportName": "阿克拉",
    "pickup": false,
    "delivery": false,
    "abroad": true,
    "createdAt": 1513682767,
    "updatedAt": 1513682767,
    "createdBy": "708ccc12-bd0c-3ba2-8754-c3e39a5dd62a",
    "updatedBy": "708ccc12-bd0c-3ba2-8754-c3e39a5dd62a",
  }
];

module.exports = {
  getAllAirports: function () {
    return airports;
  }
};