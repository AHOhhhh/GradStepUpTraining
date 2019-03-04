import React from 'react'
import Table from 'antd/lib/table'
import moment from 'moment';
import {isEmpty} from 'lodash'
import cssModules from 'react-css-modules'
import {formatPrice, formatRateRange} from 'utils'

import styles from './index.module.scss'

const OfferInfo = ({ offer }) => {
  const dataSource = isEmpty(offer) ? [] : [offer]
  const columns = [{
    key: 'serviceType',
    dataIndex: 'serviceType',
    title: '推荐方案',
    width: 200
  }, {
    key: 'credit',
    dataIndex: 'credit',
    title: '授信额度（元）',
    render: text => (text ? formatPrice(text) : '-')
  }, {
    key: 'rate',
    dataIndex: 'rate',
    title: '利率（%）',
    render: (text, record) => formatRateRange(record)
  }, {
    key: 'expirationDate',
    dataIndex: 'expirationDate',
    title: '有效期',
    render: text => (text ? moment(text).format('YYYY-MM-DD') : '-')
  }, {
    key: 'serviceDescription',
    dataIndex: 'serviceDescription',
    title: '方案描述',
    render: text => (text || '-'),
    width: 200
  }];
  return (
    <Table
      styleName="offer-info"
      rowKey="offerId"
      dataSource={dataSource}
      columns={columns}
      pagination={false}
    />)
}

export default cssModules(OfferInfo, styles)
