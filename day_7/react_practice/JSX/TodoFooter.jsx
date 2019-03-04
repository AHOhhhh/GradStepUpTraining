class TodoFooter extends React.Component {
  constructor(props) {
    super(props);
  }
  render() {
    return (
      <footer className="footer">
        <span className="todo-count">
          <strong>{this.props.itemCount}</strong> item left</span>
        <ul className="filters">
          <li>
            <a className={this.props.mode === "all" ? "selected" : ""} href="#/" onClick={(event) => {
              this.props.changeMode('all');
            }}>All</a>
          </li>
        
          <li>
            <a className={this.props.mode === "active" ? "selected" : ""} href="#/active" onClick={(event) => {
              this.props.changeMode('active');
            }}>Active</a>
          </li>
          <li>
            <a className={this.props.mode === "completed" ? "selected" : ""} href="#/completed" onClick={(event) => {
              this.props.changeMode('completed');
            }} >Completed</a>
          </li>
        </ul>
        <button className="clear-completed" onClick={(event) => {
          this.props.deleleAll();
        }}>Clear completed</button>
      </footer>);
  }
}

