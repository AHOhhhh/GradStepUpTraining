'use strict'

const ORDER_SCHEMA = {
  "name": "雷凌",
  "address": "陕西省西安市雁塔区天谷",
  "cellphone": "",
  "telephone": "15266668888",
  "status": "Submitted",
  "id": "wms0308128439620963",
  "userId": "user-id",
  "userRole": "EnterpriseAdmin",
  "enterpriseId": "enterprise-id",
  "createdAt": "2017-11-15T02:27:23.962Z",
  "updatedAt": "2017-11-15T02:27:23.962Z",
  "createdBy": "user-id",
  "updatedBy": "user-id",
  "version": 0,
  "deleted": false
}

//
const ORDERS = [
  {
    "name": "雷凌123",
    "address": "陕西省",
    "cellphone": "15266668888",
    "telephone": "029-12345678",
    "chargingRules": [
      {
        "quantityFrom": 0,
        "quantityTo": 100,
        "unitPrice": 0.28,
      },
      {
        "quantityFrom": 100,
        "quantityTo": 200,
        "unitPrice": 0.15,
      }
    ],
    "currency": "CNY",
    "effectiveFrom": "2017-11-11T03:44:59Z",
    "effectiveTo": "2017-11-16T03:44:59Z",
    "approvedPrice": 1234,
    "status": "Audited",
    "type": "Open",
    "priceRange": {
      "min": 1,
      "max": 2
    },
    "serviceIntro": "sevice introduction",
    "createdAt": "2017-11-20T08:51:28Z",
    "updatedAt": "2017-11-20T08:51:47.348Z",
    "createdBy": "user-id",
    "updatedBy": "user-id",
    "id": "wms0313678884870963",
    "userId": "user-id",
    "userRole": "EnterpriseUser",
    "enterpriseId": "user-id",
    "orderChangeRecords": [
      {
        "id": "9ca638dd-b95e-4300-8e89-574ecea6a62d",
        "orderId": "wms0313678884870963",
        "oldStatus": "Submitted",
        "newStatus": "Audited",
        "detail": null,
        "createdAt": "2017-11-20T08:51:47.340Z",
        "updatedAt": "2017-11-20T08:51:47.340Z",
        "createdBy": "user-id",
        "updatedBy": "user-id"
      }
    ]
  },
  {
    "name": "雷凌123",
    "address": "陕西省",
    "cellphone": "15266668888",
    "telephone": "029-12345678",
    "chargingRules": [
      {
        "quantityFrom": 0,
        "quantityTo": 100,
        "unitPrice": 0.28,
      },
      {
        "quantityFrom": 100,
        "quantityTo": 200,
        "unitPrice": 0.15,
      }
    ],
    "currency": "CNY",
    "effectiveFrom": "2017-11-11T03:44:59Z",
    "effectiveTo": "2017-11-16T03:44:59Z",
    "approvedPrice": 1234,
    "status": "Submitted",
    "type": "Open",
    "priceRange": {
      "min": 1,
      "max": 2
    },
    "serviceIntro": "sevice introduction",
    "createdAt": "2017-11-20T08:51:28Z",
    "updatedAt": "2017-11-20T08:51:47.348Z",
    "createdBy": "user-id",
    "updatedBy": "user-id",
    "id": "wms0313678884870963",
    "userId": "user-id",
    "userRole": "EnterpriseUser",
    "enterpriseId": "user-id",
    "orderChangeRecords": [
      {
        "id": "9ca638dd-b95e-4300-8e89-574ecea6a62d",
        "orderId": "wms0313678884870963",
        "oldStatus": "",
        "newStatus": "Submitted",
        "detail": null,
        "createdAt": "2017-11-20T08:51:47.340Z",
        "updatedAt": "2017-11-20T08:51:47.340Z",
        "createdBy": "user-id",
        "updatedBy": "user-id"
      }
    ]
  },
  {
    "name": "雷凌123",
    "address": "陕西省",
    "cellphone": "15266668888",
    "telephone": "029-12345678",
    "chargingRules": [
      {
        "quantityFrom": 0,
        "quantityTo": 100,
        "unitPrice": 0.28,
      },
      {
        "quantityFrom": 100,
        "quantityTo": 200,
        "unitPrice": 0.15,
      }
    ],
    "currency": "CNY",
    "effectiveFrom": "2017-11-11T03:44:59Z",
    "effectiveTo": "2017-11-16T03:44:59Z",
    "approvedPrice": 1234,
    "status": "Paid",
    "type": "Open",
    "priceRange": {
      "min": 1,
      "max": 2
    },
    "serviceIntro": "sevice introduction",
    "createdAt": "2017-11-20T08:51:28Z",
    "updatedAt": "2017-11-20T08:51:47.348Z",
    "createdBy": "user-id",
    "updatedBy": "user-id",
    "id": "wms0313678884870963",
    "userId": "user-id",
    "userRole": "EnterpriseUser",
    "enterpriseId": "user-id",
    "orderChangeRecords": [
      {
        "id": "asdfasfasf",
        "orderId": "wms0313678884870963",
        "oldStatus": "Submitted",
        "newStatus": "Audited",
        "detail": null,
        "createdAt": "2017-11-20T08:51:47.340Z",
        "updatedAt": "2017-11-20T08:51:47.340Z",
        "createdBy": "user-id",
        "updatedBy": "user-id"
      },
      {
        "id": "jjjjSDFASD",
        "orderId": "wms0313678884870963",
        "oldStatus": "Audited",
        "newStatus": "Paid",
        "detail": null,
        "createdAt": "2017-11-20T08:51:47.340Z",
        "updatedAt": "2017-11-20T08:51:47.340Z",
        "createdBy": "user-id",
        "updatedBy": "user-id"
      }
    ]
  },
  {
    "name": "雷凌123",
    "address": "陕西省",
    "cellphone": "15266668888",
    "telephone": "029-12345678",
    "chargingRules": [
      {
        "quantityFrom": 0,
        "quantityTo": 100,
        "unitPrice": 0.28,
      },
      {
        "quantityFrom": 100,
        "quantityTo": 200,
        "unitPrice": 0.15,
      }
    ],
    "currency": "CNY",
    "effectiveFrom": "2017-11-11T03:44:59Z",
    "effectiveTo": "2017-11-16T03:44:59Z",
    "approvedPrice": 1234,
    "status": "Closed",
    "type": "Open",
    "priceRange": {
      "min": 1,
      "max": 2
    },
    "serviceIntro": "sevice introduction",
    "createdAt": "2017-11-20T08:51:28Z",
    "updatedAt": "2017-11-20T08:51:47.348Z",
    "createdBy": "user-id",
    "updatedBy": "user-id",
    "id": "wms0313678884870963",
    "userId": "user-id",
    "userRole": "EnterpriseUser",
    "enterpriseId": "user-id",
    "orderChangeRecords": [
      {
        "id": "asdfasfasf",
        "orderId": "wms0313678884870963",
        "oldStatus": "Submitted",
        "newStatus": "Audited",
        "detail": null,
        "createdAt": "2017-11-20T08:51:47.340Z",
        "updatedAt": "2017-11-20T08:51:47.340Z",
        "createdBy": "user-id",
        "updatedBy": "user-id"
      },
      {
        "id": "jjjjSDFASD",
        "orderId": "wms0313678884870963",
        "oldStatus": "Audited",
        "newStatus": "Paid",
        "detail": null,
        "createdAt": "2017-11-20T08:51:47.340Z",
        "updatedAt": "2017-11-20T08:51:47.340Z",
        "createdBy": "user-id",
        "updatedBy": "user-id"
      },
      {
        "id": "jjjjSDFASDdfsfs",
        "orderId": "wms0313678884870963",
        "oldStatus": "Paid",
        "newStatus": "Closed",
        "detail": null,
        "createdAt": "2017-11-20T08:51:47.340Z",
        "updatedAt": "2017-11-20T08:51:47.340Z",
        "createdBy": "user-id",
        "updatedBy": "user-id"
      }
    ]
  },
  {
    "name": "雷凌123",
    "address": "陕西省",
    "cellphone": "15266668888",
    "telephone": "029-12345678",
    "chargingRules": [
      {
        "quantityFrom": 0,
        "quantityTo": 100,
        "unitPrice": 0.28,
      },
      {
        "quantityFrom": 100,
        "quantityTo": 200,
        "unitPrice": 0.15,
      }
    ],
    "currency": "CNY",
    "effectiveFrom": "2017-11-11T03:44:59Z",
    "effectiveTo": "2017-11-16T03:44:59Z",
    "approvedPrice": 1234,
    "status": "Opened",
    "type": "Open",
    "priceRange": {
      "min": 1,
      "max": 2
    },
    "serviceIntro": "sevice introduction",
    "createdAt": "2017-11-20T08:51:28Z",
    "updatedAt": "2017-11-20T08:51:47.348Z",
    "createdBy": "user-id",
    "updatedBy": "user-id",
    "id": "wms0313678884870963",
    "userId": "user-id",
    "userRole": "EnterpriseUser",
    "enterpriseId": "user-id",
    "orderChangeRecords": []
  }
]

const ordersByPageSumitted = {
  "content": [
    {
      "name": "雷凌-进行中",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 123,
      "currency": "CNY",
      "status": "Submitted",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-待审核",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 222,
      "currency": "CNY",
      "status": "Submitted",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Submitted",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    }
  ],
  "last": false,
  "totalPages": 1,
  "totalElements": 3,
  "size": 1,
  "number": 0,
  "sort": [
    {
      "direction": "DESC",
      "property": "createdAt",
      "ignoreCase": false,
      "nullHandling": "NATIVE",
      "ascending": false,
      "descending": true
    }
  ],
  "first": true,
  "numberOfElements": 1
}

const ordersByPageOngoing = {
  "content": [
    {
      "name": "雷凌-进行中",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 123,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-待审核",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 222,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Paid",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    }
  ],
  "last": false,
  "totalPages": 3,
  "totalElements": 44,
  "size": 1,
  "number": 0,
  "sort": [
    {
      "direction": "DESC",
      "property": "createdAt",
      "ignoreCase": false,
      "nullHandling": "NATIVE",
      "ascending": false,
      "descending": true
    }
  ],
  "first": true,
  "numberOfElements": 1
}

const ordersByPageClosed = {
  "content": [
    {
      "name": "雷凌-进行中",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 123,
      "currency": "CNY",
      "status": "Closed",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-待审核",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 222,
      "currency": "CNY",
      "status": "Closed",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    },
    {
      "name": "雷凌-已完成",
      "address": "陕西省西安市雁塔区天谷八路与云水二路十字环普产业科技园E栋3103",
      "cellphone": "15266668888",
      "telephone": "029-12345678",
      "chargingRules": null,
      "effectiveFrom": null,
      "effectiveTo": null,
      "approvedPrice": 233,
      "currency": "CNY",
      "status": "Closed",
      "type": "Open",
      "createdAt": "2017-11-20T03:07:01Z",
      "updatedAt": "2017-11-20T03:08:58Z",
      "createdBy": "user-id",
      "updatedBy": "user-id",
      "id": "wms0313472208610963",
      "userId": "user-id",
      "userRole": "EnterpriseUser",
      "enterpriseId": "user-id",
      "orderChangeRecords": []
    }
  ],
  "last": false,
  "totalPages": 1,
  "totalElements": 3,
  "size": 1,
  "number": 0,
  "sort": [
    {
      "direction": "DESC",
      "property": "createdAt",
      "ignoreCase": false,
      "nullHandling": "NATIVE",
      "ascending": false,
      "descending": true
    }
  ],
  "first": true,
  "numberOfElements": 1
}


module.exports = {
  getOrderByStatus: function (status) {
    for (let order of ORDERS) {
      if (order.status === status) {
        return order
      }
    }
  },
  getOrdersbyUserId: function () {
    return [];
  },
  gerOrderbyPage: function (status) {
    switch (status) {
      case 'Submitted':
        return ordersByPageSumitted
      case 'ONGOING':
        return ordersByPageOngoing
      default:
        return ordersByPageClosed
    }

  }
}