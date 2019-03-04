import React from 'react';
import { Layout, Menu, Icon } from 'antd';
import { NavLink } from 'react-router-dom';
const { Header } = Layout;

const PageHeader = () => {
  return (
    <Header style={{ position: 'fixed', zIndex: 1, width: '100%' }}>
      <Menu
        theme="dark"
        mode="horizontal"
        defaultSelectedKeys={window.location.href.indexOf('cart') === -1 ? ["1"] : ["2"]}
        style={{ lineHeight: '64px' }}>
        <Menu.Item key="1">
          <NavLink exact to="/"  >
            <Icon type="home" />商城
          </NavLink>
        </Menu.Item>
        <Menu.Item key="2"><NavLink exact to="/cart">
          <Icon type="shopping-cart" />订单
      </NavLink>
        </Menu.Item>
        <Menu.Item key="3"><NavLink exact to="/login">
          <Icon type="shopping-cart" />登录
      </NavLink>
        </Menu.Item>
      </Menu>
    </Header>
  );
}
export default PageHeader;