import { combineReducers } from 'redux';

import { addTodo, deleteTodo, modifyTodoState, modifyTodoText, modifyAllTodoState, deleteAllCompletedTodos, modifyDisplayMode } from './actionTypes';


const initState = {
  todos: [],
  display: 'All'
}
const todosReducer = (preTodos = initState.todos, action) => {
  let { type, data } = action;
  switch (type) {
    case addTodo:
      return Object.assign([], preTodos.concat(data));
    case modifyTodoState:
      return Object.assign([], preTodos.map(todo => {
        if (todo.id === data) {
          return Object.assign({}, todo, {
            completed: !todo.completed
          })
        }
        return todo;
      }));
    case modifyTodoText:
      return Object.assign([], preTodos.map(todo => {
        if (todo.id === data.id) {
          return Object.assign({}, todo, { text: data.text });
        }
        return todo;
      }));
    case deleteTodo:
      return Object.assign([], preTodos.filter(todo => todo.id !== data));
    case modifyAllTodoState:
      return Object.assign([], preTodos.map(todo => {
        return Object.assign({}, todo, { completed: data });
      }));
    case deleteAllCompletedTodos:
      return Object.assign([], preTodos.filter(todo => !todo.completed));
    default:
      break;
  }
  return preTodos;
}

const displayReducer = (preDisplay = initState.display, action) => {

  let { type, data } = action;
  switch (type) {
    case modifyDisplayMode:
      return data;
    default:
      break;
  }
  return preDisplay;
}

const reducer = combineReducers({
  todos: todosReducer,
  display: displayReducer
});
export default reducer;