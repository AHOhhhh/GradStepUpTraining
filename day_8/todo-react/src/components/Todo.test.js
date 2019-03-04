import React from 'react';
import Todo from './Todo';
import { shallow } from 'enzyme';
import '../testSetup'

it('should show the text when given a todo props', () => {
  const item = { id: 1, text: 'todo text', completed: false };
  const component = shallow(<Todo todo={item} />);

  expect(component.find('label').text()).toBe('todo text');
  expect(component.find('li').hasClass('completed')).toBe(false);
});

// it('should invoke handleClick when click destroy button', () => {
//   const mock = jest.fn();
 
//   const item = { id: 1, text: 'todo text', completed: false };
//   const wrapper = shallow(<Todo todo={item} deleteTodo={mock} />);
//   wrapper.find('button').simulate('click');
//   expect(mock).toHaveBeenCalled();
// });


it('should class contain editing when double click input.', function () {
  const item = { id: 1, text: 'todo text', completed: false };
  const component = shallow(<Todo todo={item} />);
  expect(component.find("li").hasClass('editing')).toBe(false);

  component.setState({ editing: true });
  expect(component.find("li").hasClass('editing')).toBe(true);

});