import React, { Component } from 'react';
import { Provider } from 'react-redux';
import Header from './Header';
import TodoList from './TodoList';
import Footer from './Footer';
import store from '../redux/store';
import './App.css';
class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <section className="todoapp">
          <Header />
          <TodoList />
          <Footer />
        </section>
      </Provider>
    );
  }
}

export default App;
