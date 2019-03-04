import React from 'react';
import test from 'ava';
import { shallow } from 'enzyme';
import Icon from 'antd/lib/icon'

import {LoadingPage} from './index'

test('loading state', (t) => {
  const wrapper = shallow(<LoadingPage />)
  t.is(wrapper.find(Icon).length, 0)
})
