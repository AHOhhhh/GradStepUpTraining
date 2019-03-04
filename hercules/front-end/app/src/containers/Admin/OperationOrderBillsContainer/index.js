import React, { Component } from 'react'
import { connect } from 'react-redux'
import { Row, Col, Table, Button, message } from 'antd/lib'
import httpClient from 'utils/http'
import { get, isEmpty } from 'lodash'
import { formatDateTime, convertCharge } from 'utils/format'
import cssModules from 'react-css-modules'
import LeftNavigation from 'components/LeftNavigation'
import moment from 'moment'
import OrderBillSearchBar from '../share/SearchBar'
import { getOrderBills, getOrderBill, importExcel } from './actions'
import styles from './index.module.scss'
import OrderBillUploader from './OrderBillUploader'

@connect(
  ({ orderBill }) => ({ orderBill }),
  { getOrderBills, getOrderBill, importExcel }
)
class OperationOrderBillsContainer extends Component {

  state = {
    condition: {
      size: 10,
      page: 0
    }
  }

  componentDidMount() {
    this.search()
  }

  search() {
    const condition = this.state.condition
    const orderId = get(condition, 'searchText')
    const { getOrderBill, getOrderBills } = this.props
    if (isEmpty(orderId)) {
      getOrderBills(condition)
    } else {
      getOrderBill(orderId)
    }
  }

  onShowSizeChange(currentPagePosition, size) {
    this.pageChangeHandler(currentPagePosition, size)
  }

  pageChangeHandler(currentPagePosition, size) {
    const condition = this.state.condition
    this.setState(
      {
        condition: {
          ...condition,
          page: currentPagePosition - 1,
          size
        }
      },
      () => this.search()
    )
  }

  conditionChangeHandler(newCondition) {
    this.setState(
      {
        condition: {
          ...newCondition,
          page: 0
        }
      },
      () => this.search()
    )
  }

  renderCharge(type) {
    return (charge, orderBill) => (
      <div>
        <p>{orderBill[type]}</p>
        <p>{convertCharge(charge, orderBill.currency)}</p>
      </div>
    )
  }

  renderTable() {
    const { orderBill = {} } = this.props
    const { loading } = orderBill
    const dataSource = get(orderBill, 'content', [])
    const total = get(orderBill, 'totalElements', 0)
    const condition = this.state.condition
    const pageSize = condition.size
    const current = condition.page + 1
    const columns = [
      {title: '订单号', dataIndex: 'orderId', key: 'orderId'},
      {
        title: '创建时间',
        dataIndex: 'createdAt',
        key: 'createdAt',
        render: createdAt => formatDateTime(createdAt)
      },
      {title: '产品类型', dataIndex: 'orderType', key: 'orderType'},
      {title: '服务商', dataIndex: 'vendor', key: 'vendor'},
      {title: '支付方式', dataIndex: 'payMethod', key: 'payMethod'},
      {title: '支付对象', dataIndex: 'payChannel', key: 'payChannel'},
      {
        title: '产品费／金额',
        dataIndex: 'productCharge',
        key: 'productCharge',
        render: this.renderCharge('productChargeStatus')
      },
      {
        title: '服务费／金额',
        dataIndex: 'serviceCharge',
        key: 'serviceCharge',
        render: this.renderCharge('serviceChargeStatus')
      },
      {
        title: '手续费／金额',
        dataIndex: 'commissionCharge',
        key: 'commissionCharge',
        render: this.renderCharge('commissionChargeStatus')
      },
    ]

    return (
      <div className={styles.orderBillTable}>
        <Table
          rowKey={record => record.id}
          columns={columns}
          loading={loading}
          current={current}
          dataSource={dataSource}
          pagination={{pageSize, total, onChange: ::this.pageChangeHandler, showSizeChanger: true, onShowSizeChange: ::this.onShowSizeChange}}
        />
      </div>
    )
  }

  exportExcel() {
    const condition = this.state.condition
    httpClient.get('/order-bill/excel', {
      responseType: 'blob',
      params: { ...condition, orderId: condition.searchText }
    }).then(content => {
      const blob = new Blob([content.data], {type: 'application/vnd.ms-excel' });
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = `对账单_${moment().format('YYYY-MM-DD HH:mm:ss')}.xlsx`
      document.body.appendChild(link);
      link.click();
    }).catch(() => message.info('导出失败！'))
  }

  renderButtonGroup() {
    return (
      <div className={styles.btnGroup}>
        <OrderBillUploader action={this.props.importExcel} />
        <Button className={styles.btn} onClick={::this.exportExcel}>
          导出
        </Button>
      </div>
    )
  }

  render() {
    return (
      <div className="user-container">
        <Row>
          <Col span={4}>
            <LeftNavigation
              location={this.props.location.pathname}/>
          </Col>
          <Col span={20}>
            <div className="right-container">
              <h2>对账单</h2>
              <OrderBillSearchBar placeholder="输入订单号" onSearch={::this.conditionChangeHandler} combineSearch={false} />
              {this.renderButtonGroup()}
              {this.renderTable()}
            </div>
          </Col>
        </Row>
      </div>
    )
  }
}

export default cssModules(OperationOrderBillsContainer, styles)