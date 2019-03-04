import React, { Component } from 'react';
import Header from './Header';
import TodoList from './TodoList';
import Footer from './Footer';
import './App.css';
class App extends Component {
  constructor(props) {
    super(props);
    this.state = { todos: [], display: 'all' };
    this.addTodo = this.addTodo.bind(this);
    this.changeTodoState = this.changeTodoState.bind(this);
    this.deleteTodo = this.deleteTodo.bind(this);
    this.toggleAllTodos = this.toggleAllTodos.bind(this);
    this.deleleAllCompleteTodos = this.deleleAllCompleteTodos.bind(this);
    this.changeDisplayMode = this.changeDisplayMode.bind(this);
    this.changeTodoText = this.changeTodoText.bind(this);
  } 

  addTodo(todo) {
    if (todo.text === '') {
      return
    }
    this.setState({ todos: this.state.todos.concat(todo) });
  }
  changeTodoState(id) {
     let todo= this.state.todos.find(item => item.id === id);
     todo.completed = !todo.completed;
    this.setState(this.state);
  }
  changeTodoText(id, text) {
    this.state.todos.find(item => item.id === id).text = text;
    this.setState(this.state);
  }
  deleteTodo(id) {
    this.state.todos.splice(this.state.todos.findIndex(todo => todo.id === id), 1);
    this.setState(this.state);
  }
  deleleAllCompleteTodos() {
    this.setState({todos:this.state.todos.filter(item => !item.completed)});
  }
  toggleAllTodos(completed) {
    this.state.todos.forEach(todo => {
      todo.completed = completed;
    });
    this.setState(this.state);
  }
  changeDisplayMode(mode) {
    this.setState({ display: mode })
  }
  render() {
    return (
      <section className="todoapp">
        <Header addTodo={this.addTodo} />
        <TodoList todos={this.state.todos} mode={this.state.display} changeState={this.changeTodoState} toggleAll={this.toggleAllTodos} deleteTodo={this.deleteTodo} changeTodoText={this.changeTodoText} />
        <Footer itemCount={this.state.todos.filter(item => !item.completed).length} deleleAll={this.deleleAllCompleteTodos} mode={this.state.display} changeMode={this.changeDisplayMode} />
      </section>
    );
  }
}

export default App;
