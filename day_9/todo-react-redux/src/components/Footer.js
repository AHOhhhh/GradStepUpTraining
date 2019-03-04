import React, { Component } from 'react';
import {connect} from 'react-redux';
import {modifyDisplayMode,deleteAllCompletedTodos,formatAction} from '../redux/actionTypes';
class Footer extends Component {
  render() {
    return (
      <footer className="footer">
        <span className="todo-count">
          <strong>{this.props.count}</strong> item left</span>
        <ul className="filters">
          <li>
            <a className={this.props.display === "All" ? "selected" : ""} href="#/" onClick={() => {
              this.props.handleModifyDisplayMode('All');
            }}>All</a>
          </li>

          <li>
            <a className={this.props.display === "Active" ? "selected" : ""} href="#/active" onClick={() => {
              this.props.handleModifyDisplayMode('Active');
            }}>Active</a>
          </li>
          <li>
            <a className={this.props.display === "Completed" ? "selected" : ""} href="#/completed" onClick={() => {
              this.props.handleModifyDisplayMode('Completed');
            }} >Completed</a>
          </li>
        </ul>
        <button className="clear-completed" onClick={() => {
          this.props.handleDeleteAllCompletedTodos();
        }}>Clear completed</button>
      </footer>);
  }
}

const mapStateToProps=state=>({
  count:state.todos.filter(todo=>!todo.completed).length,
  display:state.display});
const mapDispatchToProps=dispatch=>({
  handleModifyDisplayMode: mode=>dispatch(formatAction(modifyDisplayMode,mode)),
  handleDeleteAllCompletedTodos:()=>dispatch(formatAction(deleteAllCompletedTodos,''))
});
const FooterWithData=connect(mapStateToProps,mapDispatchToProps)(Footer);

export default FooterWithData;