import React from 'react';
import TodoList from './TodoList';
import { shallow } from 'enzyme';
import '../testSetup'

it('should return 4 when given items', () => {
  const item = [
    { id: 1, text: 'todo text1', completed: false },
    { id: 2, text: 'todo text2', completed: false },
    { id: 3, text: 'todo text3', completed: false },
    { id: 4, text: 'todo text4', completed: false },
  ];
  const component = shallow(<TodoList todos={item} />);

  expect(component.find('ul').children().length).toBe(4);
});
