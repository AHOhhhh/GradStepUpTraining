import React, {Component} from 'react' // eslint-disable-line
import Table from 'antd/lib/table'
import {formatDateTime} from 'utils/format'
import {USER_ROLE_NAME_MAP} from 'constants'
import {typeVendorMap} from 'constants/order'


const formatIndex = (index) => {
  return (index <= 9) ? `0${index}` : index
}

const PlatformOrders = (props) => { // eslint-disable-line

  const {operations, loading, pagination} = props

  const columns = [
    {
      title: '编号',
      dataIndex: 'index',
      key: 'index',
      render: (index) =>
        <span>{formatIndex(index)}</span>
    },
    {
      title: '操作时间',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (createdAt) =>
        <span>{formatDateTime(createdAt)}</span>
    },
    {
      title: '订单号',
      dataIndex: 'orderId',
      key: 'orderId',
    },
    {
      title: '角色',
      dataIndex: 'operatorRole',
      key: 'operatorRole',
      render: (operatorRole) =>
        <span>{USER_ROLE_NAME_MAP[operatorRole]}</span>
    },
    {
      title: '服务商',
      dataIndex: 'vendor',
      key: 'vendor',
      render: (vendor) =>
        <span>{typeVendorMap[vendor]}</span>
    },
    {
      title: '操作员',
      dataIndex: 'operatorName',
      key: 'operatorName',
    },
    {
      title: '操作记录',
      dataIndex: 'operationName',
      key: 'operationName',
    },
  ]

  return (<Table
    rowKey={record => record.index}
    columns={columns}
    loading={loading}
    dataSource={operations}
    pagination={pagination}
  />)
}

export default PlatformOrders