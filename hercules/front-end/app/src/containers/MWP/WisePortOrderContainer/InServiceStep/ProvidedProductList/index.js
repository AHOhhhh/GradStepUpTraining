import React from 'react'
import Table from 'antd/lib/table'
import cssModule from 'react-css-modules'
import moment from 'moment'
import * as contants from '../constants'

import WISE_PORT_SERVICE_TYPE from '../../../share/constants/serviceType.constant'

import styles from './index.module.scss'

const columns = [
  {
    title: '运单号',
    dataIndex: 'waybillId',
    key: 'waybillId',
  },
  {
    title: '货物',
    dataIndex: 'good',
    key: 'good',
    render: (good) => good.name
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    key: 'createTime',
    render: (text) => {
      return moment(text).isValid ? moment(text).format('YYYY-MM-DD HH:mm:ss') : '-'
    }
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    key: 'updateTime',
    render: (text) => {
      return moment(text).isValid ? moment(text).format('YYYY-MM-DD HH:mm:ss') : '-'
    }
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status',
    render: (status) => contants.billWayStatusMaps[status] || status
  }
];

const ProvidedProductList = ({type, dataArray}) => {
  return (
    <div styleName="provided-product-list">
      <div styleName={'tag-provided-type ' + type}>{WISE_PORT_SERVICE_TYPE[type].text}</div>
      <Table
        dataSource={dataArray} columns={columns}
        pagination={false}/>
    </div>
  )
}

export default cssModule(ProvidedProductList, styles, {allowMultiple: true})