import React from 'react';
import test from 'ava';
import { shallow } from 'enzyme';
import { Link } from 'react-router';

import {ORDER_STATUS} from '../../../constants/steps'
import {OrderAction} from './index'

test('should not render link for closed order', t => {
  const props = {order: {status: ORDER_STATUS.closed}}
  const wrapper = shallow(<OrderAction {...props} />);
  t.is(wrapper.find(Link).length, 0)
  t.pass();
})

test('should render re-order link for rejected order', t => {
  const props = {order: {status: ORDER_STATUS.orderRejected}}
  const wrapper = shallow(<OrderAction {...props} />);
  const renderedLink = wrapper.find(Link).first()
  t.is(renderedLink.props().to, '/scf/create_order')
  t.is(renderedLink.props().target, '_self')
  t.is(renderedLink.props().children, '重新下单')
  t.pass();
});

test('should render link for accepted order', t => {
  const props = {order: {status: ORDER_STATUS.orderAccepted, ssoLoginUrl: 'test.com'}}
  const wrapper = shallow(<OrderAction {...props} />);
  const renderedLink = wrapper.find(Link).first()
  t.is(renderedLink.props().to, 'test.com')
  t.is(renderedLink.props().target, '_blank')
  t.is(renderedLink.props().children, '进入业务系统')
  t.is(renderedLink.props().rel, 'noopener noreferrer')
  t.pass();
})

test('should render businessSystem link for waitForUploadSupplementaryFiles order', t => {
  const props = {order: {status: ORDER_STATUS.waitForUploadSupplementaryFiles, ssoLoginUrl: 'test.com'}}
  const wrapper = shallow(<OrderAction {...props} />);
  const renderedLink = wrapper.find(Link).first()
  t.is(renderedLink.props().to, 'test.com')
  t.is(renderedLink.props().target, '_blank')
  t.is(renderedLink.props().children, '进入业务系统')
  t.is(renderedLink.props().rel, 'noopener noreferrer')
  t.pass();
});

test('should not render businessSystem link for waitForUploadSupplementaryFiles order without uploadFileUrl', t => {
  const props = {order: {status: ORDER_STATUS.waitForUploadSupplementaryFiles}}
  const wrapper = shallow(<OrderAction {...props} />);
  t.is(wrapper.find(Link).length, 0)
  t.pass();
});

test('should render businessSystem link for waitForSelectedFinancer order', t => {
  const props = {order: {status: ORDER_STATUS.waitForFinancier, ssoLoginUrl: 'test.com'}}
  const wrapper = shallow(<OrderAction {...props} />);
  const renderedLink = wrapper.find(Link).first()
  t.is(renderedLink.props().to, 'test.com')
  t.is(renderedLink.props().target, '_blank')
  t.is(renderedLink.props().children, '进入业务系统')
  t.is(renderedLink.props().rel, 'noopener noreferrer')
  t.pass();
});

test('should render businessSystem link for waitForFinancierOffer order', t => {
  const props = {order: {status: ORDER_STATUS.waitForFinancierOffer, ssoLoginUrl: 'test.com'}}
  const wrapper = shallow(<OrderAction {...props} />);
  const renderedLink = wrapper.find(Link).first()
  t.is(renderedLink.props().to, 'test.com')
  t.is(renderedLink.props().target, '_blank')
  t.is(renderedLink.props().children, '进入业务系统')
  t.is(renderedLink.props().rel, 'noopener noreferrer')
  t.pass();
});
