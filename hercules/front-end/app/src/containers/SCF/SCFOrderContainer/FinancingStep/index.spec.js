import React from 'react';
import test from 'ava';
import { shallow } from 'enzyme';
import chai, {expect} from 'chai';
import sinon from 'sinon';
import sinonChai from 'sinon-chai';

import {ORDER_STATUS} from '../../constants/steps'
import {FinancingStep} from './index'
import OfferInfo from './OfferInfo'

chai.use(sinonChai);

const getSCFOrderById = sinon.spy()
const order = {
  id: 1,
  financierName: 'company',
  creditGrantingUrl: 'www.test.com',
  scfFinancierOffer: {financierName: 'company'}}

const wrapper = (status = ORDER_STATUS.waitForFinancier) => {
  const props = {
    getSCFOrderById,
    order: {...order, status}
  }
  return shallow(<FinancingStep {...props} />)
}

test('should render FinancingStep with h2', t => {
  t.is(wrapper().find('h2').first().text(), '融资授信方案')
})

test('should render FinancingStep for waitForFinancier order', t => {
  t.is(wrapper().find('p').length, 2)
  t.is(wrapper().find('p').at(0).text(), '资金方接洽中，请耐心等待...')
  t.is(wrapper().find('p').at(1).text(), '刷新，查看接洽结果')
});

test('should render a link can click to refresh for waitForFinancier order', t => {
  wrapper().find('a').simulate('click');
  expect(getSCFOrderById).to.have.been.calledWith(order.id);
  t.pass();
});

test('should render FinancingStep for waitForFinancierOffer order', t => {
  const component = wrapper(ORDER_STATUS.waitForFinancierOffer)
  t.is(component.find('p').at(0).text(), `资金方${order.financierName}`)
  t.is(component.find({href: order.creditGrantingUrl}).length, 1)
  t.is(component.find({href: order.creditGrantingUrl}).at(0).text(), '申请融资授信')
});

test('should render FinancingStep for closed order', t => {
  const component = wrapper(ORDER_STATUS.closed)
  t.is(component.find('p').at(0).text(), `资金方${order.scfFinancierOffer.financierName}`)
  t.is(component.find(OfferInfo).length, 1)
});
