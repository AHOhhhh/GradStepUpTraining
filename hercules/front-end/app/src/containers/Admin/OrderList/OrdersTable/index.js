import React, {createElement} from 'react';
import {includes} from 'lodash'
import {Link} from 'react-router'
import {Table} from 'antd'
import {USER_ROLE} from 'constants'
import {typeNameMap, typeVendorMap, orderStatusMap} from 'constants/order'
import {ENABLE_ORDER_REFUND_STATUS} from '../constants'
import {formatDateTime} from '../../../../utils/format'
import {WMSOrderDetail, MWPOrderDetail, SCFOrderDetail, ACGOrderDetail} from '../OrderDetail'

const rowRendererMap = {
  mwp: MWPOrderDetail,
  wms: WMSOrderDetail,
  scf: SCFOrderDetail,
  acg: ACGOrderDetail
}

const isDisplayEdit = (record) => {
  return record.status === orderStatusMap.wms.Submitted && record.orderType === 'wms' && includes(['Renew', 'Open'], record.type)
}

const typeToName = (type) => {
  return typeNameMap[type]
}

const getOrderDetailsLink = (record) => {
  return `/admin/${record.orderType}/orders/${record.id}`
}

const expandedRowRender = (record) => {
  return createElement(rowRendererMap[record.orderType], record)
}

const OrdersTable = (props) => {
  const {showSizeChanger, orderData, popUpOrderEdit, popUpRefund, pupUpAuditPayment, refreshPaymentStatus, pageSize, changePage, onShowSizeChange, loading, orders, current, auth: {user = {}}, expandedRowKeys, onExpandedRowsChange} = props
  const isPlatformAdmin = user.role === USER_ROLE.platformAdmin

  const columns = [
    {title: '订单号', dataIndex: 'id', key: 'id'},
    {
      title: '产品名称',
      dataIndex: 'orderType',
      key: 'orderType',
      render: (type) =>
        <span>{typeToName(type)}</span>
    },
    {
      title: '服务商',
      dataIndex: 'vendor',
      key: 'serviceProvider',
      render: (vendor) =>
        <span>{typeVendorMap[vendor]}</span>
    },
    {title: '客户名称', dataIndex: 'enterpriseName', key: 'enterpriseName'},
    {
      title: '下单时间',
      dataIndex: 'createdAt',
      key: 'createdAt',
      render: (createdAt) =>
        <span>{formatDateTime(createdAt)}</span>
    },
    {title: '订单状态', dataIndex: 'status', key: 'status'},
    {
      title: '退款状态',
      dataIndex: 'refundStatus',
      key: 'refundStatus',
      render: (refundStatus) => <span>{refundStatus ? '已退款' : '--'}</span>
    },
    {
      title: '操作',
      dataIndex: '',
      key: 'x',
      render: (record) => {
        const isEnableEdit = isPlatformAdmin && isDisplayEdit(record)
        const isEnableRefresh = record.status === '待支付'
        const isEnableAuditPayment = isPlatformAdmin && (record.status === '已支付待确认')
        const isEnableOrderRefund = record.orderType !== 'scf' && (isPlatformAdmin && (includes(ENABLE_ORDER_REFUND_STATUS, record.status) || (record.status === '已取消' && record.refundStatus)))

        return (
          <span className="operation">
            <Link target="_blank" to={() => getOrderDetailsLink(record)} className="red-button">查看</Link>
            {isEnableEdit && (
              <a
                className="red-button left-margin"
                onClick={() => popUpOrderEdit(record)}>编辑</a>
            )}
            {isEnableRefresh && (
              <a
                className="red-button left-small-margin"
                onClick={() => refreshPaymentStatus(record)}>刷新支付状态</a>
            )}
            {isEnableAuditPayment && (
              <a
                className="red-button left-margin"
                onClick={() => pupUpAuditPayment(record)}>支付审核</a>
            )}
            {isEnableOrderRefund && (
              <a
                className="red-button left-margin"
                onClick={() => popUpRefund(record)}>订单退款</a>
            )}
          </span>
        )
      }
    }
  ]

  return (
    <div>
      <Table
        rowKey={record => record.id}
        columns={columns}
        loading={loading}
        dataSource={orderData}
        expandedRowRender={expandedRowRender}
        expandedRowKeys={expandedRowKeys}
        pagination={{
          showSizeChanger,
          pageSize,
          total: orders.totalElements,
          onChange: changePage,
          onShowSizeChange,
          current
        }}
        onExpandedRowsChange={onExpandedRowsChange}
      />
    </div>
  )
}

export default OrdersTable