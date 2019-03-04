import React from 'react'
import {Menu} from 'antd'
import {
  ALL_STATUS, CANCELLED_STATUS, CLOSED_STATUS, ONGOING_STATUS,
  SUBMITTED_STATUS
} from 'containers/Admin/OrderList/constants';

const OrderMenus = ({onClick, selectedKey}) => {
  return (<Menu
    onClick={(e) => {
      const value = e.key
      onClick(value)
    }}
    selectedKeys={[selectedKey]}
    mode="horizontal"
  >
    <Menu.Item key={ALL_STATUS}>全部订单</Menu.Item>
    <Menu.Item key={SUBMITTED_STATUS}>待确认</Menu.Item>
    <Menu.Item key={ONGOING_STATUS}>服务中</Menu.Item>
    <Menu.Item key={CLOSED_STATUS}>已完成</Menu.Item>
    <Menu.Item key={CANCELLED_STATUS}>已取消</Menu.Item>
  </Menu>)
}

export default OrderMenus

