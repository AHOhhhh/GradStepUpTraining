import test from 'ava';
import React from 'react';
import { shallow } from 'enzyme';

import Header from './index';

test.skip('Header rendering without name', t => {
  const wrapper = shallow(
    <Header />
  );

  const loginLink = wrapper.find('Link').at(0);
  t.is(loginLink.prop('to'), '/login');
  t.is(loginLink.prop('children'), '登录');

  const signUpLink = wrapper.find('Link').at(1);
  t.is(signUpLink.prop('to'), '/signup');
  t.is(signUpLink.prop('children'), '注册');

});

test.skip('Header rendering with name', t => {
  const props = { user: { username: 'Juntao Qiu'} }
  const wrapper = shallow(
    <Header {...props} />
  )

  const nodeText = wrapper.find('span').at(0).text();
  t.deepEqual(nodeText, 'Juntao Qiu，欢迎回来')
});