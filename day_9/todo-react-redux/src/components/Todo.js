import React, { Component } from 'react';
import { connect } from 'react-redux';
import { modifyTodoState, modifyTodoText, deleteTodo, formatAction } from '../redux/actionTypes';
class Todo extends Component {
  constructor(props) {
    super(props);
    this.state = { editing: false, tempText: this.props.todo.text };
    this.updateEditState = this.updateEditState.bind(this);
  }
  updateEditState(editing) {
    this.setState({ editing: editing });
  }
  updateEditText(text) {
    this.setState({ tempText: text });
  }
  render() {
    let className = '';
    if ((this.props.display === 'Active' && this.props.todo.completed) || (this.props.display === 'Completed' && !this.props.todo.completed)) {
      className += "hidden"
    }
    if (this.state.editing) {
      className += " editing";
    }
    if (this.props.todo.completed) {
      className += " completed";
    }

    return (
      <li className={className}>
        <div className="view">
          <input className="toggle" type="checkbox" checked={this.props.todo.completed} onClick={() => { this.props.handleModifyTodoState(this.props.todo.id) }} />
          <label onDoubleClick={() => {
            this.updateEditState(true);
          }}>{this.props.todo.text}</label>
          <button className="destroy" onClick={() => {
            this.props.handleDeleteTodo(this.props.todo.id);
          }}></button>
        </div>
        <input className="edit" value={this.state.tempText} onChange={(event) => {
          this.updateEditText(event.target.value.trim());
        }} onKeyDown={(event) => {
          if (event.keyCode === 13) {
            this.props.handleModifyTodoText(this.props.todo.id, this.state.tempText);
            this.updateEditState(false);
          }
        }} autoFocus />
      </li>
    );
  }
}

const mapStateToProps = state => ({
  display: state.display
});
const mapDispatchToProps = dispatch => ({
  handleModifyTodoState: id => dispatch(formatAction(modifyTodoState, id)),
  handleModifyTodoText: (id, text) => dispatch(formatAction(modifyTodoText, { id, text })),
  handleDeleteTodo: id => dispatch(formatAction(deleteTodo, id))
});
const TodoWithData = connect(mapStateToProps, mapDispatchToProps)(Todo);

export default TodoWithData;