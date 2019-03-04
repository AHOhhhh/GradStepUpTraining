const order = {
  "delegateOrderId": "5a26a5d2c42aa00001332fe5",
  "counterPartyName": "12",
  "credit": 12,
  "dueDate": "2017-12-27",
  "description": "1212",
  "requireAmount": 1212,
  "currency": "CNY",
  "expectRate": 12,
  "canPublic": true,
  "contacts": [
    {
      "id": null,
      "name": null,
      "cellphone": null,
      "telephone": null,
      "email": null,
      "country": null,
      "countryAbbr": null,
      "province": null,
      "provinceId": null,
      "city": null,
      "cityId": null,
      "district": null,
      "districtId": null,
      "address": null,
      "postcode": null
    }
  ],
  "offers": [{
    "companyName": "海惠保理有限责任公司",
    "recommendedSolution": "应收账款融资",
    "creditLine": 1000000,
    "borrowingRate": 12.12,
    "validationTime": '2017-12-15',
    "solutionDescription": "介绍你随便"
  }],

  "contractFiles": [
    {"name": "aaa", id: "12312"},
    {"name": "bbb", id: "12312"},
    {"name": "ccc", id: "12312"}
  ],

  "uploadFileUrl": "https://www.baidu.com",
  "confidentialAgreementSignUrl": "https://www.baidu.com",
  "id": "121712052157384888648860",
  "userId": "48673d4b-b28e-40bd-b1d9-b168c0e2bcd7",
  "userRole": "EnterpriseUser",
  "enterpriseId": "b822717a-882b-403c-8e2f-cb013c4c3820",
  "status": "WaitForUploadContract",
  "orderChangeRecords": [ ],
  "orderType": "scf",
  "orderSubType": "scf",
  "percent": 75
}

module.exports = function (req, res, next) {
  res.send(200, order);
  next();
}
