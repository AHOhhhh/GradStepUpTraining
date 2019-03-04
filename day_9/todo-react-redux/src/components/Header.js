import React, { Component } from 'react';
import { connect } from 'react-redux';
import { addTodo, formatAction } from '../redux/actionTypes';
class Header extends Component {

  render() {
    return (
      <header className="header">
        <h1>todos</h1>
        <input className="new-todo" placeholder="What needs to be done?" onKeyDown={(event) => {
          if (event.keyCode === 13) {
            this.props.handleSubmit(event.target.value.trim());
            event.target.value = '';
          }
        }} autoFocus />
      </header>
    );
  }
}

const mapStateToProps = state => ({});
const mapDispatchToProps = dispatch => ({
  handleSubmit: (value) => {
    let data = { id: new Date().toLocaleString(), text: value, completed: false };
    dispatch(formatAction(addTodo, data));
  }
});

const HeaderWithData = connect(mapStateToProps, mapDispatchToProps)(Header);
export default HeaderWithData;
