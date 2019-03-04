import React from 'react';
import test from 'ava';
import { shallow } from 'enzyme';
import chai, {expect} from 'chai';
import sinon from 'sinon';
import sinonChai from 'sinon-chai';

import {ORDER_STATUS} from '../../constants/steps'
import {ApproveStep} from './index'

chai.use(sinonChai);

const getSCFOrderById = sinon.spy()
const order = {id: 1}

const wrapper = (status = ORDER_STATUS.submitted) => {
  const props = {
    getSCFOrderById,
    order: {...order, status}
  }
  return shallow(<ApproveStep {...props} />)
}

test('should render ApproveStep for submitted order', t => {
  const refreshLink = wrapper().find('p').at(1).props().children[0]
  t.is(wrapper().find('img').length, 0)
  t.is(wrapper().find('p').length, 2)
  t.is(wrapper().find('p').at(0).text(), '您的订单已提交审核，请耐心等待…')
  t.is(refreshLink.props.children, '刷新')
  t.is(wrapper().find('p').at(1).text(), '刷新，查看审核状态更新')
  t.pass();
});

test('should render a link can click to refresh for submitted order', t => {
  wrapper().find('a').simulate('click');
  expect(getSCFOrderById).to.have.been.calledWith(order.id);
  t.pass();
});

test('should render ApproveStep for accepted order', t => {
  const component = wrapper(ORDER_STATUS.orderAccepted)
  t.is(component.find('img').length, 1)
  t.is(component.find('p').length, 1)
  t.is(component.find('p').at(0).text(), '您的订单已通过审核，请尽快完成订单后续操作。')
  t.pass();
});

test('should render ApproveStep for rejected order', t => {
  const component = wrapper(ORDER_STATUS.orderRejected)
  t.is(component.find('img').length, 1)
  t.is(component.find('p').length, 1)
  t.is(component.find('p').at(0).text(), '您的订单未通过审核，订单已中止，可选择重新下单。')
  t.pass();
});
