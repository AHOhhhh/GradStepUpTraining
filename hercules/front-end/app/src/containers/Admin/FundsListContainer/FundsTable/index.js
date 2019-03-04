import React from 'react';
import {Table} from 'antd'
import cssModules from 'react-css-modules'
import {USER_ROLE} from 'constants'
import { formatDateTime, convertCharge } from 'utils/format'
import {typeNameMap, typeServiceProviderMap, transactionTypeMap} from '../../OrderList/constants'
import styles from './index.module.scss'

const FundsTable = (props) => {
  const {showSizeChanger, list, pageSize, changePage, onShowSizeChange, loading, total, current, user} = props

  const columns = [
    {title: '流水单号', dataIndex: 'transactionId', key: 'transactionId'},
    {title: '企业名称', dataIndex: 'enterpriseName', key: 'enterpriseName'},
    {title: '订单号', dataIndex: 'orderId', key: 'orderId'},
    {
      title: '产品类型',
      dataIndex: 'orderType',
      key: 'orderType',
      render: (type) =>
        <span>{typeNameMap[type]}</span>
    },
    {
      title: '交易类型',
      dataIndex: 'transactionType',
      key: 'transactionType',
      render: (type) =>
        <span>{transactionTypeMap[type]}</span>
    },
    {
      title: '交易时间',
      dataIndex: 'paidTime',
      key: 'paidTime',
      render: (time) =>
        <span>{time ? formatDateTime(time) : '-'}</span>
    },
    {
      title: '金额',
      dataIndex: 'amount',
      key: 'amount',
      className: 'amount',
      render: (amount, record) =>
        <span>
          {`${record.transactionType === 'Expense' ? '+' : '-'}${convertCharge(amount, record.currency)}`}
        </span>
    }
  ]

  if (USER_ROLE.platformAdmin === user.role) {
    columns.splice(4, 0, {
      title: '服务商',
      dataIndex: 'vendor',
      key: 'vendor',
      render: (vendor) =>
        <span>{typeServiceProviderMap[vendor]}</span>
    })
  }
  return (
    <div styleName="funds-table">
      <Table
        rowKey={record => record.id}
        columns={columns}
        loading={loading}
        current={current}
        dataSource={list}
        pagination={{showSizeChanger, pageSize, total, onChange: changePage, onShowSizeChange, current: current + 1}}
      />
    </div>
  )
}

export default cssModules(FundsTable, styles, {allowMultiple: true})
