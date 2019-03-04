class TodoMainItem extends React.Component {
  constructor(props) {
    super(props);
    this.state = { editing: false };
    this.updateEditState = this.updateEditState.bind(this);
  }
  updateEditState(editing) {
    this.setState({ editing: editing });
  }
  render() {
    let todo = this.props.todo;
    let mode = this.props.mode;
    let hidden = false;
    if ((mode === 'active' && todo.completed) || (mode === 'completed' && !todo.completed)) {
      hidden = true;
    }

    return (
      <li className={this.state.editing ? "editing" : (hidden ? "hidden" : (todo.completed ? "completed" : ""))} id={todo.id}>
        <div className="view">
          <input className="toggle" type="checkbox" checked={todo.completed} onChange={(event) => { this.props.changeState(event.target.parentNode.parentNode.id, event.target.checked) }} />
          <label onClick={(event) => {
            this.updateEditState(true);
            event.target.parentNode.parentNode.children[1].value = event.target.innerText;
          }}>{todo.text}</label>
          <button className="destroy" onClick={(event) => {
            this.props.deleteTodo(event.target.parentNode.parentNode.id);
          }}></button>
        </div>
        <input className="edit"  onKeyDown={(event) => {
          if (event.keyCode === 13) {
            this.props.changeTodoText(event.target.parentNode.id, event.target.value.trim());
            this.updateEditState(false);
          }
        }} autoFocus />
      </li>
    );
  }
}