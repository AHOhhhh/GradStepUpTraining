import React, { Component } from 'react';
import Todo from './Todo';

class TodoList extends Component {
  render() {
    return (
      <section className="main">
        <input id="toggle-all" className="toggle-all" type="checkbox" checked={this.props.todos.filter(item => !item.completed).length === 0} onClick={(event) => {
          this.props.toggleAll(event.target.checked);
        }} />
        <label htmlFor="toggle-all">Mark all as complete</label>
        <ul className="todo-list">
          {this.props.todos.map(todo => <Todo todo={todo} mode={this.props.mode} changeState={this.props.changeState} deleteTodo={this.props.deleteTodo} changeTodoText={this.props.changeTodoText} />)}
        </ul>
      </section>
    );
  }
}

export default TodoList;