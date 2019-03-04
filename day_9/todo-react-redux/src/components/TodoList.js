import React, { Component } from 'react';
import { connect } from 'react-redux';
import { modifyAllTodoState, formatAction } from '../redux/actionTypes';
import Todo from './Todo';

class TodoList extends Component {
  render() {
    return (
      <section className="main">
        <input id="toggle-all" className="toggle-all" type="checkbox" checked={this.props.todos.filter(item => !item.completed).length === 0} onClick={(event) => {
          this.props.handleToggleAll(event.target.checked);
        }} />
        <label htmlFor="toggle-all">Mark all as complete</label>
        <ul className="todo-list">
          {this.props.todos.map(todo => <Todo todo={todo} />)}
        </ul>
      </section>
    );
  }
}


const mapStateToProps = state => ({
  todos: state.todos
});
const mapDispatchToProps = dispatch => ({
  handleToggleAll: (completed) => dispatch(formatAction(modifyAllTodoState, completed))
});

const TodoListWithData = connect(mapStateToProps, mapDispatchToProps)(TodoList);

export default TodoListWithData;