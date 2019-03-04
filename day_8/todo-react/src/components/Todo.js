import React, { Component } from 'react';
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
    this.setState({tempText:text});
  }
  render() {
    let todo = this.props.todo;
    let mode = this.props.mode;
    let className='';

    if ((mode === 'active' && todo.completed) || (mode === 'completed' && !todo.completed)) {
     className+="hidden"
    }
    if(this.state.editing){
      className+=" editing";
    }
    if (todo.completed) {
      className+=" completed";
    }
    return (
      <li className={className} id={todo.id}>
        <div className="view">
          <input className="toggle" type="checkbox" checked={todo.completed} onChange={() => { this.props.changeState(this.props.todo.id) }} />
          <label onDoubleClick={() => {
            this.updateEditState(true);
          }}>{todo.text}</label>
          <button className="destroy" onClick={() => {
            this.props.deleteTodo(this.props.todo.id);
          }}></button>
        </div>
        <input className="edit" value={this.state.tempText} onChange={(event) => {
            this.updateEditText(event.target.value.trim());
        }} onKeyDown={(event)=>{
          if (event.keyCode === 13) {
            this.props.changeTodoText(this.props.todo.id, this.state.tempText);
            this.updateEditState(false);
          }
        }} autoFocus />
      </li>
    );
  }
}
export default Todo;