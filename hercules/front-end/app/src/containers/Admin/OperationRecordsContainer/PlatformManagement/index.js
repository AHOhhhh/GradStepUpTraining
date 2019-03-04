import React, {Component} from 'react' // eslint-disable-line
import Table from 'antd/lib/table'
import {formatDateTime} from 'utils/format'
import {USER_ROLE_NAME_MAP} from 'constants'

const formatIndex = (index) => {
  return (index <= 9) ? `0${index}` : index
}

const PlatformManagement = (props) => { // eslint-disable-line

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
      title: '企业名称',
      dataIndex: 'enterpriseName',
      key: 'enterpriseName',
      width: 300,
    },
    {
      title: '角色',
      dataIndex: 'operatorRole',
      key: 'operatorRole',
      render: (operatorRole) =>
        <span>{USER_ROLE_NAME_MAP[operatorRole]}</span>
    },
    {
      title: '操作员',
      dataIndex: 'operatorName',
      key: 'operatorName',
    },
    {
      title: '操作记录',
      dataIndex: 'typeDescription',
      key: 'typeDescription',
    },
  ]

  return (<Table
    rowKey={record => record.id}
    columns={columns}
    loading={loading}
    dataSource={operations}
    pagination={pagination}
  />)
}

export default PlatformManagement