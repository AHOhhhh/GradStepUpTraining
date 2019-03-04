import test from 'ava'
import React from 'react'
import { shallow } from 'enzyme'
import OrderLogistic from './index'

test('<OrderLogistic /> Render With Empty Data', t => {
  const props = {}
  const Component = shallow(<OrderLogistic {...props} />)
  t.is(Component.find('span').text(), '暂无物流信息')
})

test.skip('<OrderLogistic /> Render With Logistic Info', t => {
  const order = {
    logisticsStatus: [
      {
        logisticsStatusInfo: '订单已送达[xxx货运站]',
        updateInfoTime: '2017-11-21T10:00:00+08'
      }
    ]
  }
  const Component = shallow(<OrderLogistic order={order} />)
  const list = Component.find('ul li')
  t.is(list.length, 1)
  const info = list.find('span')
  t.is(info.at(0).text(), '2017-11-21 10:00:00')
  t.is(info.at(1).text(), '订单已送达[xxx货运站]')
})