import test from 'ava';
import React from 'react';
import {render} from 'enzyme';

import TransportArrangement from './index';

let order

test.beforeEach(() => {
  order = {
    id: '101712011005014699061320',
    goods: [
      {
        name: 'TP-Link TL-WR886N 450M 无线路由器',
        size: {
          length: 1.00,
          width: 2.00,
          height: 3.00,
          unit: 'm'
        },
        weight: {
          value: 100.80,
          unit: 'kg'
        },
        price: 1500.00,
        quantity: 5,
        packageQuantity: 20,
        declarationInfo: '货物是易碎品，不能压碰'
      }
    ],
    shippingInfo: {
      departure: {
        airportName: '阿布贾',
        abroad: true,
        delivery: true
      },
      arrival: {
        airportName: '北京',
        abroad: true,
        delivery: true
      },
      pickUpAddress: {
        name: '张三',
        telephone: '021-68668432',
        cellphone: '13690012223',
        email: 'a@b.com',
        countryAbbr: 'CHN',
        country: '中国',
        provinceId: 'PEK',
        province: '北京',
        cityId: 'PEK',
        city: '北京',
        districtId: 'HD',
        district: '海淀区',
        address: '上海市静安区新闸路1124弄',
        postcode: '710000'
      },
      dropOffAddress: {
        name: '李四',
        telephone: '400-890-0505',
        cellphone: '13812012223',
        email: 'a@b.com',
        countryAbbr: 'CHN',
        country: '中国',
        provinceId: 'PEK',
        province: '北京',
        cityId: 'PEK',
        city: '北京',
        districtId: 'HD',
        district: '海淀区',
        address: '北京市海淀区王庄路清华同方科技广场D座东楼6层',
        postcode: '710000'
      },
      estimatedDeliveryTime: '2017-10-29 10:35'
    },

    contact: {
      id: 'id2',
      name: '李四',
      telephone: '400-890-0505',
      cellphone: '+86 13812012223',
      email: 'a@b.com',
      countryAbbr: 'CHN',
      country: '中国',
      provinceId: 'PEK',
      province: '北京',
      cityId: 'PEK',
      city: '北京',
      districtId: 'HD',
      district: '海淀区',
      address: '北京市海淀区王庄路清华同方科技广场D座东楼6层',
      postcode: '710000'
    },
    price: {
      airlineFee: 4000.00,
      pickUpFee: 500.00,
      dropOffFee: 300.00,
      total: 4800.00
    },
    currency: 'CNY',
    userId: 'userId',
    userRole: 'EnterpriseUser',
    enterpriseId: 'userId',
    orderChangeRecords: [
      {
        id: '4f55227c-c6a7-498d-b791-4ee8bef53715',
        orderId: '101712011005014699061320',
        oldStatus: 'Submitted',
        newStatus: 'Audited',
        detail: null,
        createdAt: '2017-12-01T05:51:29.440Z',
        createdBy: 'userId'
      }
    ],
    status: 'OrderTracking',
    createdAt: '2017-12-01T05:51:29.440Z',
    createdBy: 'userId',
    transportPlan: {
      delegateOrderId: 'H17112015123810400000003',
      pickupTelephone: '021-88790394',
      deliveryTelephone: '021-88790394',
      scheduledPickupTime: '2017-12-30T10:00:00+08:00',    // 取货时间
      scheduledTakeOffTime: '2017-12-30T12:00:00+08:00',   // 航班时间
      expectedDeliveryTime: '2017-12-30T17:00:00+08:00',  // 派送时间
      scheduledFlight: 'MU100'
    },
    logisticsStatus: [
      {
        logisticsStatusInfo: '订单已送达[xxx货运站]',
        updateInfoTime: '2017-11-21T10:00:00+08',
        updateInfoUserName: '123'
      },
      {
        logisticsStatusInfo: '订单已送达[xxx货运站]',
        updateInfoTime: '2019-11-21T10:00:00+08',
        updateInfoUserName: '123'
      },
      {
        logisticsStatusInfo: '订单已送达[xxx货运站]',
        updateInfoTime: '2020-11-21T10:00:00+08',
        updateInfoUserName: '123'
      }
    ],
    referenceOrder: {
      id: 'order-id',
      portId: '墨尔本'
    }
  }
})

test('TransportArrangement rendering without departure section', t => {
  order.shippingInfo.departure = false
  const wrapper = render(
    <TransportArrangement order={order}/>
  );

  t.is(wrapper.find('h3').length, 2);
});

test('TransportArrangement rendering without departure and arrival section', t => {
  order.shippingInfo.departure = false
  order.shippingInfo.arrival = false
  const wrapper = render(
    <TransportArrangement order={order}/>
  );

  t.is(wrapper.find('h3').length, 1);
});

test('TransportArrangement rendering with departure and arrival section', t => {
  const wrapper = render(
    <TransportArrangement order={order}/>
  );

  t.is(wrapper.find('h3').length, 3);
});
