import React, { Component } from 'react';

class Header extends    Component {

  render() {
    return (
      <header className="header">
        <h1>todos</h1>
        <input className="new-todo" placeholder="What needs to be done?" onKeyDown={(event) => {
          if (event.keyCode === 13) {
            this.props.addTodo({ id: new Date().toLocaleString(), text: event.target.value.trim(), completed: false });
            event.target.value = '';
          }
        }} autoFocus />
      </header>
    );
  }
}
export default Header;
