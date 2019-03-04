import './App.css';
import React from 'react';
import { Layout } from 'antd';
import { BrowserRouter } from 'react-router-dom';
import Header from './components/Header';
import Content from './components/Content';
const App = () => {
  return (
    <BrowserRouter>
      <Layout>
        <Header />
        <Content />
      </Layout>
    </BrowserRouter>
  );
}

export default App;
