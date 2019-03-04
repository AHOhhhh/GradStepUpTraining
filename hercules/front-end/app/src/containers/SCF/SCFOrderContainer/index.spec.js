import React from 'react';
import test from 'ava';
import { shallow } from 'enzyme';
import sinon from 'sinon'
import chai from 'chai';
import sinonChai from 'sinon-chai';

import { BreadcrumbV2 } from 'components'
import WisePortOrderSteppers from 'components/Steppers/WisePortOrderSteppers'
import {ORDER_STATUS} from '../constants/steps'
import {SCFOrderContainer} from './index'
import OrderDetailPanel from './components/OrderDetailPanel'
import WisePortTimeLine from '../../MWP/share/components/WisePortTimeLine'
import OrderAction from './components/OrderAction'
import OrderLoadPanel from './components/OrderLoadPanel'

chai.use(sinonChai);

const getSCFOrderById = sinon.spy()
const order = {status: ORDER_STATUS.orderAccepted, uploadFileUrl: 'test.com'}
const defaultProps = {
  getSCFOrderById,
  params: {orderId: 1},
  order,
  preview: false
}

const wrapper = props => shallow(<SCFOrderContainer {...defaultProps} {...props} />);

test('should render SCFOrderContainer', t => {
  t.is(wrapper().find(BreadcrumbV2).length, 1)
  t.is(wrapper().find(WisePortOrderSteppers).length, 1)
  t.is(wrapper().find(WisePortTimeLine).length, 1)
  t.is(wrapper().find(OrderDetailPanel).length, 1)
  t.is(wrapper().find(OrderAction).length, 1)
  t.is(wrapper().find(OrderLoadPanel).length, 0)
});

test('should render next step button for accepted order', t => {
  const nextLinkBtn = wrapper().find('button')
  t.is(nextLinkBtn.length, 1)
  t.is(nextLinkBtn.text(), '下一步')
})

test('should not render next step button for other order', t => {
  const props = {
    order: {status: ORDER_STATUS.submitted}
  }
  const nextLink = wrapper(props).find('button')
  t.is(nextLink.length, 0)
})

test('should render closed panel for closed order', t => {
  const props = { order: {status: ORDER_STATUS.closed} }
  t.is(wrapper(props).find(OrderLoadPanel).length, 1)
})
