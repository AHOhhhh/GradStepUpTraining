import React, {Component, PropTypes, createElement} from 'react'
import {Link} from 'react-router'
import {connect} from 'react-redux'
import _ from 'lodash'
import cssModules from 'react-css-modules'
import {Tabs, Table, Row, Col, Select, Checkbox, Tooltip, Modal, message} from 'antd'
import moment from 'moment'

import {getOrders, updateOrderToCancelled} from 'actions'
import LeftNavigation from 'components/LeftNavigation'
import {typeNameMap, serviceTypeMap, orderStatusMap} from 'constants/order'
import styles from './index.module.scss'
import {WMSOrderDetail, MWPOrderDetail, SCFOrderDetail, ACGOrderDetail} from './OrderDetail'
import {
  ALL_STATUS,
  SUBMITTED_STATUS,
  ONGOING_STATUS,
  CLOSED_STATUS,
  CANCELLED_STATUS,
  PAGE_SIZE
} from './constants'
import {formatOrderList, canCancel} from './utils/listUtils'
import ModalFooterButtons from '../../shared/ModalFooterButtons'

const TabPane = Tabs.TabPane

const formatDate = (str) => {
  const thisMoment = _.isNumber(str) ? moment.unix(str) : moment(str)
  return thisMoment.format('YYYY-MM-DD HH:mm:ss')
}

const rowRendererMap = {
  mwp: MWPOrderDetail,
  wms: WMSOrderDetail,
  scf: SCFOrderDetail,
  acg: ACGOrderDetail
}

const getTagStyleName = (type) => {
  switch (serviceTypeMap[type]) {
    case serviceTypeMap.PickUp:
      return 'tag tag-pick-up'
    case serviceTypeMap.DropOff:
      return 'tag tag-drop-off'
    default:
      return 'tag'
  }
}

const renderTypeColumn = (type, index) =>
  (<span key={index} className={getTagStyleName(type)}>{serviceTypeMap[type] || type}</span>)

const renderOrderTypes = ({orderType, orderSubTypes, serviceType}) => {
  const serviceTypes = _.isEmpty(serviceType) ? [] : [serviceType]
  const types = orderType === 'scf' ? serviceTypes : orderSubTypes
  return _.isEmpty(types) ? <div>-</div> : <div>{_.map(types, renderTypeColumn)}</div>
}

const mapStateToProps = state => ({
  orders: state.admin.orders,
  auth: state.auth
})

@connect(mapStateToProps, {getOrders, updateOrderToCancelled})
class OrderListContainer extends Component {
  static propTypes = {
    getOrders: PropTypes.func.isRequired,
    orders: PropTypes.object
  }

  state = {
    status: ALL_STATUS,
    pageNum: 0,
    pageSize: PAGE_SIZE,
    orderType: 'acg',
    showPaymentOnly: false,
    expandedRowKeys: [],
    loading: false,
    displayCancelModal: false,
    orderData: {},
    disabledTabs: ''
  };

  componentWillMount() {
    this.getOrderListByPage({
      page: 0,
      size: this.state.pageSize,
      enterpriseId: this.props.auth.enterpriseId
    })
  }

  static getOrderDetailsLink(record) {
    return `/${record.orderType}/orders/${record.id}`
  }

  static typeToName(type, record) {
    if (type === 'acg') {
      const departureAirportName = _.get(record, 'shippingInfo.departure.airportName', '')
      const arrivalAirportName = _.get(record, 'shippingInfo.arrival.airportName', '')
      return `${typeNameMap[type]}（${departureAirportName} - ${arrivalAirportName}）`
    }
    return typeNameMap[type];
  }

  static expandedRowRender(record) {
    return createElement(rowRendererMap[record.orderType], record)
  }

  onExpandedRowsChange = (rows) => {
    this.setState({expandedRowKeys: rows})
  }

  getOrderListByPage(pageInfo) {
    if (!_.isEmpty(this.state.orderType)) {
      pageInfo.type = this.state.orderType
    }

    if (!_.isEmpty(this.state.status)) {
      pageInfo.status = this.state.status
    }

    if (_.isNull(pageInfo.page)) {
      pageInfo.page = _.get(this.state, 'pageNum')
    }

    pageInfo.waitForPay = this.state.showPaymentOnly

    this.setState({loading: true})
    const loading = false;
    this.props.getOrders(pageInfo)
      .then(() => this.setState({loading, disabledTabs: ''}))
      .catch(() => this.setState({loading, disabledTabs: ''}))
  }

  changeTab(key) {
    this.setState({
      status: key,
      showPaymentOnly: false,
      pageNum: 0,
      expandedRowKeys: [],
      disabledTabs: true
    }, () => {
      this.getOrderListByPage({
        page: 0,
        size: this.state.pageSize,
        enterpriseId: this.props.auth.enterpriseId
      })
    })
  }

  changePage(pageNumber) {
    this.setState({pageNum: pageNumber - 1}, () => {
      this.getOrderListByPage({
        page: pageNumber - 1,
        size: this.state.pageSize,
        enterpriseId: this.props.auth.enterpriseId
      })
    })
  }

  selectOrderType(value) {
    this.setState({orderType: value}, () => {
      this.getOrderListByPage({
        page: 0,
        size: this.state.pageSize,
        enterpriseId: this.props.auth.enterpriseId
      })
    })
  }

  toggleWaitForPaymentOnly() {
    this.setState({showPaymentOnly: !this.state.showPaymentOnly}, () => {
      this.getOrderListByPage({
        page: 0,
        size: this.state.pageSize,
        enterpriseId: this.props.auth.enterpriseId
      })
    })
  }

  renderIdColumn = (id, record) => {
    const {expandedRowKeys} = this.state
    const columnIsExpanded = _.includes(expandedRowKeys, id)
    const referenceIcon = record && record.isReferenced && !columnIsExpanded
      ? <Tooltip title="关联订单"><i className="refer-icon"/></Tooltip>
      : null
    return <div><Tooltip title={id}><p className="order-id">{id}</p></Tooltip>{referenceIcon}</div>
  }

  closeModal = () => {
    this.setState({
      displayCancelModal: false,
      orderData: {}
    })
  }

  displayCancelModal = (order) => {
    this.setState({
      displayCancelModal: true,
      orderData: order
    })
  }

  cancelOrder = () => {
    this.props.updateOrderToCancelled(this.state.orderData, {cancelReason: 'ManualCancellation'})
      .then(this.getOrderListByPage.bind(this, {enterpriseId: this.props.auth.enterpriseId}))
      .then(() => this.setState({
        displayCancelModal: false,
        orderData: {}
      }))
      .catch(() => message.error('订单取消失败！'))

  }

  advancedOrdersTable = (orderData, showPaymentOnlyToggle = false) => (
    <div>
      <div>
        <Select
          defaultValue={this.state.orderType}
          value={this.state.orderType}
          style={{width: 200, marginTop: 10, marginBottom: 10}}
          onChange={this.selectOrderType.bind(this)}>
          <Select.Option value="acg">航空货运</Select.Option>
          <Select.Option value="wms">WMS</Select.Option>
        </Select>
        {
          showPaymentOnlyToggle && <label htmlFor="html-for" className="label">
            <Checkbox
              defaultChecked={this.state.showPaymentOnly}
              checked={this.state.showPaymentOnly}
              onChange={this.toggleWaitForPaymentOnly.bind(this)}>只看待支付</Checkbox>
          </label>
        }
      </div>
      {this.ordersTable(orderData)}
    </div>)

  changePageSize(pageNum, pageSize) {
    this.setState({pageNum, pageSize}, () => {
      this.getOrderListByPage({
        page: this.state.pageNum - 1,
        size: this.state.pageSize,
        enterpriseId: this.props.auth.enterpriseId
      })
    })
  }

  ordersTable(orderData) {
    const {orders: {totalElements}} = this.props
    const {expandedRowKeys, loading, pageSize} = this.state

    const columns = [
      {
        title: '订单号',
        dataIndex: 'id',
        key: 'id',
        render: (id, record) => this.renderIdColumn(id, record)
      },
      {
        title: '产品名称',
        dataIndex: 'orderType',
        key: 'orderType',
        render: (type, record) =>
          <span>{OrderListContainer.typeToName(type, record)}</span>
      },
      {
        title: '服务类型',
        dataIndex: 'orderSubTypes',
        key: 'orderSubTypes',
        width: 200,
        render: (text, record) => renderOrderTypes(record)
      },
      {
        title: '下单时间',
        dataIndex: 'createdAt',
        key: 'createdAt',
        render: createdAt => <span>{formatDate(createdAt)}</span>
      },
      {
        title: '状态',
        dataIndex: 'status',
        key: 'status',
        render: (status, record) => {
          if (record.orderType === 'wms' && record.type === 'Recharge' && status === 'Submitted') {
            status = 'Audited'
          }

          return _.get(orderStatusMap, [record.orderType, status])
        }
      },
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
        render: (record) =>
          <span className="operation">
            <Link
              target="_blank" to={() => OrderListContainer.getOrderDetailsLink(record)}
              className="red-button">查看</Link>
            <a
              className={`cancel-button left-margin ${!canCancel(record) ? 'hide' : ''}`}
              onClick={() => this.displayCancelModal(record)}
            >取消订单</a>
          </span>
      }
    ]

    return (
      <Table
        rowKey={record => record.id}
        columns={columns}
        loading={loading}
        dataSource={orderData}
        expandedRowRender={OrderListContainer.expandedRowRender}
        expandedRowKeys={expandedRowKeys}
        onExpandedRowsChange={this.onExpandedRowsChange}
        pagination={{
          showSizeChanger: true,
          pageSize,
          total: totalElements,
          onChange: ::this.changePage,
          onShowSizeChange: ::this.changePageSize
        }}
      />
    )
  }

  render() {
    const {orders: {content: orders}} = this.props
    const orderData = formatOrderList(orders)

    return (
      <div className={'user-container ' + styles['enterprise-user-orders']}>
        <Row>
          <Col span={4}>
            <LeftNavigation
              location={this.props.location.pathname}/>
          </Col>
          <Col span={20}>
            <div className="card-container">
              <h2>订单管理</h2>
              <Tabs onChange={this.changeTab.bind(this)} type="card">
                <TabPane
                  tab="全部订单"
                  disabled={this.state.disabledTabs}
                  key={ALL_STATUS}>{this.advancedOrdersTable(orderData.all, true)}</TabPane>
                <TabPane
                  tab="待确认"
                  disabled={this.state.disabledTabs}
                  key={SUBMITTED_STATUS}>{this.advancedOrdersTable(orderData.submitted, true)}</TabPane>
                <TabPane
                  tab="服务中" disabled={this.state.disabledTabs}
                  key={ONGOING_STATUS}>{this.advancedOrdersTable(orderData.onGoing, true)}</TabPane>
                <TabPane
                  tab="已完成" disabled={this.state.disabledTabs}
                  key={CLOSED_STATUS}>{this.advancedOrdersTable(orderData.closed)}</TabPane>
                <TabPane
                  tab="已取消" disabled={this.state.disabledTabs}
                  key={CANCELLED_STATUS}>{this.advancedOrdersTable(orderData.cancelled)}</TabPane>
              </Tabs>
            </div>
          </Col>
        </Row>
        <Modal
          title="取消订单"
          visible={this.state.displayCancelModal}
          onCancel={::this.closeModal}
          onOk={::this.cancelOrder}
          maskClosable={false}
          width={642}
          wrapClassName="cancel-order-modal"
          footer={<ModalFooterButtons
            okText="确定" cancelText="取消" onOk={::this.cancelOrder} onCancel={::this.closeModal}/>}
        >
          <p className="cancel-info">确定取消该笔<span>{typeNameMap[this.state.orderData.orderType]}</span>订单吗？订单取消后，将不可再恢复。
          </p>
          <p className="cancel-order-id">（订单号：{this.state.orderData.id}）</p>
        </Modal>
      </div>
    )
  }
}

export default cssModules(OrderListContainer, styles, {allowMultiple: true})
