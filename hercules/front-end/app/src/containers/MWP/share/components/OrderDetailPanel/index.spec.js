import test from 'ava';

import React from 'react';
import {shallow} from 'enzyme';

import {formatContact, renderSection} from './index'

test('format contact', t => {
  const contact = {
    name: '雷凌',
    country: '中国',
    countryCode: 'CN',
    district: '雁塔区',
    province: '陕西',
    city: '西安',
    address: '天谷八路街道环普科技产业园E栋',
    mobile: '15266668888',
    phone: '029-83456677'
  }
  const str = formatContact(contact)
  t.deepEqual(str, <div>
    <p>雷凌</p>
    <p>中国大陆陕西西安雁塔区天谷八路街道环普科技产业园E栋</p>
    <p>15266668888/029-83456677</p>
  </div>)
});

test('format contact without mobile', t => {
  const contact = {
    name: '雷凌',
    country: '中国',
    countryCode: 'CN',
    district: '雁塔区',
    province: '陕西',
    city: '西安',
    address: '天谷八路街道环普科技产业园E栋',
    phone: '029-83456677'
  }
  const str = formatContact(contact)
  t.deepEqual(str, <div>
    <p>雷凌</p>
    <p>中国大陆陕西西安雁塔区天谷八路街道环普科技产业园E栋</p>
    <p>029-83456677</p>
  </div>)
});

test('format contact without telephone', t => {
  const contact = {
    name: '雷凌',
    country: '中国',
    countryCode: 'CN',
    district: '雁塔区',
    province: '陕西',
    city: '西安',
    address: '天谷八路街道环普科技产业园E栋',
    mobile: '15266668888'
  }
  const str = formatContact(contact)
  t.deepEqual(str, <div>
    <p>雷凌</p>
    <p>中国大陆陕西西安雁塔区天谷八路街道环普科技产业园E栋</p>
    <p>15266668888</p>
  </div>)
});

test('render section', t => {
  const wrapper = shallow(
    renderSection('hello', 'world')
  );

  t.is(wrapper.find('p').at(0).text(), 'hello：')
  t.is(wrapper.find('p').at(1).text(), 'world')
})