import test from 'ava';

import React from 'react';
import {shallow} from 'enzyme';

import GoodList from './index'

test('render empty goods list', t => {
  const wrapper = shallow(
    <GoodList goods={[]} />
  );

  t.is(wrapper.find('ol li').length, 0)
})

test('render goods list', t => {
  const goods = [
    { name: 'TP-LINK TL-WR886N 450M无线路由器' },
    { name: 'TP-LINK TL-WR886N 460M无线路由器' },
    { name: 'TP-LINK TL-WR886N 470M无线路由器' }
  ]

  const wrapper = shallow(
    <GoodList goods={goods} />
  );

  t.is(wrapper.find('ol li').length, 3)
})