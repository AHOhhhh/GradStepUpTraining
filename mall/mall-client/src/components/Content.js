import React from 'react';
import { Layout } from 'antd';
import {Route} from 'react-router-dom';
import ProductContent from './ProductContent';
import OrderContent from './OrderContent';
import LoginContent from './Login';
const { Content } = Layout;
const PageContent = () => {
  return (
    <Content style={{ padding: '30px', marginTop: 64 }}>
    <Route exact path='/' component={ProductContent} />
    <Route exact path='/cart' component={OrderContent} />
    <Route exact path='/login' component={LoginContent} />
    </Content>
  );
}

export default PageContent;