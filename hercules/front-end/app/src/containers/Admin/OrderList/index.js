import React, {Component, PropTypes, createElement} from 'react' // eslint-disable-line
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import _ from 'lodash'
import currencyFormatter from 'currency-formatter'
import moment from 'moment'
import LeftNavigation from 'components/LeftNavigation'
import * as action from 'actions'
import Row from 'antd/lib/row'
import Col from 'antd/lib/col'
import message from 'antd/lib/message'

import {orderStatusMap} from 'constants/order'
import styles from './index.module.scss'
import EditOrderModal from '../EditOrderModal'
import AuditPaymentModal from '../share/components/AuditPaymentModal'
import ReFundModal from '../../shared/FundInfo/RefundModal'
import SearchBar from './SearchBar'
import OrdersTable from './OrdersTable'

@connect(
  state => ({
    orders: state.admin.orders,
    auth: state.auth
  }),
  dispatch => ({
    actions: bindActionCreators(action, dispatch)
  })
)

class OrderList extends Component { // eslint-disable-line
  state = {
    pageSize: 10,
    loading: false,
    current: 1,
    visibleAuditPaymentModal: false,
    expandedRowKeys: []
  }

  static propTypes = {
    actions: PropTypes.object.isRequired,
    orders: PropTypes.object
  }

  componentWillMount() {
    this.getOrderListByPage({
      page: 0,
      size: this.state.pageSize,
      enterpriseId: this.props.auth.enterpriseId
    })
  }

  getOrderListByPage = (pageInfo) => {
    this.setState({loading: true, current: pageInfo.page + 1, expandedRowKeys: []})
    this.props.actions.getOrders(pageInfo)
      .then(() => this.setState({loading: false}))
      .catch(() => {
        message.error('查询失败，请稍后再试')
        this.setState({loading: false})
      })
  }

  getOrderListByOrderId = (orderId) => {
    this.setState({loading: true, expandedRowKeys: []})
    this.props.actions.adminGetOrderById(orderId)
      .then(() => this.setState({loading: false}))
      .catch((err) => {
        if (err.status === 404) {
          message.error('该订单号不存在')
        } else {
          message.error('查询失败，请稍后再试')
        }
        this.setState({loading: false})
      })
  }

  handleSearchByOrderId = (orderId) => {
    if (_.isEmpty(orderId)) {
      const {current} = this.state
      this.getOrderListByPage({
        page: current,
        size: this.state.pageSize,
        enterpriseId: this.props.auth.enterpriseId
      })
    } else {
      this.getOrderListByOrderId(orderId)
    }
  }

  refreshOrderList = () => {
    const params = {...this.searchCondition}
    this.props.actions.getOrders(params);
  }

  changePage(current) {
    this.getOrderListByPage({
      ...this.searchCondition,
      page: current - 1,
      size: this.state.pageSize,
    })
  }

  popUpOrderEdit(record) {
    this.refs.editOrderModal.showModal(record)
  }

  popUpRefund(record) {
    this.RefundModal.open(record)
  }

  pupUpAuditPayment(record) {
    const modal = this.refs.auditPaymentModal.getWrappedInstance()
    modal.showModal(record)
  }

  toDisplayOrder(order, key) {
    const displayOrder = _.clone(order)

    displayOrder.updatedAt = moment(order.updatedAt).format('YYYY-MM-DD HH:mm:ss')
    displayOrder.key = key
    displayOrder.contact = `${_.get(order, 'contact.name')} ${_.get(order, 'contact.cellphone')}`
    displayOrder.types = _.isEmpty(order.orderSubTypes) ? '' : order.orderSubTypes.join(',')
    displayOrder.status = this.mapOrderStatus(order)
    displayOrder.effectiveFrom = moment(order.effectiveFrom)
    displayOrder.effectiveTo = moment(order.effectiveTo)
    displayOrder.totalPrice = order.totalPrice ? currencyFormatter.format(order.totalPrice, {code: order.currency}) : '面议'

    return displayOrder
  }

  mapOrderStatus(order) {
    if (order.orderType === 'wms' && order.type === 'Recharge' && order.status === 'Submitted') {
      return '待支付'
    }

    return orderStatusMap[order.orderType][order.status]
  }

  filterOrderListByStatus() {
    const orders = _.get(this.props, 'orders.content', [])
    return _.map(orders, this.toDisplayOrder.bind(this))
  }

  refreshPaymentStatus(record) {
    this.setState({loading: true})
    const params = {...this.searchCondition}
    this.props.actions.refreshStatus(record, params)
      .then(() => this.setState({loading: false}))
      .catch(() => {
        message.error('刷新失败，请稍后再试')
        this.setState({loading: false})
      })
  }

  changePageSize(pageNum, pageSize) {
    this.setState({current: pageNum, pageSize}, () => {
      this.changePage(this.state.current)
    })
  }

  ordersTable(orderData) {
    return createElement(OrdersTable, {
      orderData,
      showSizeChanger: true,
      pageSize: this.state.pageSize,
      auth: this.props.auth,
      orders: this.props.orders,
      loading: this.state.loading,
      current: this.state.current,
      popUpOrderEdit: ::this.popUpOrderEdit,
      pupUpAuditPayment: ::this.pupUpAuditPayment,
      refreshPaymentStatus: ::this.refreshPaymentStatus,
      changePage: ::this.changePage,
      onShowSizeChange: ::this.changePageSize,
      expandedRowKeys: this.state.expandedRowKeys,
      onExpandedRowsChange: this.onExpandedRowsChange,
      popUpRefund: ::this.popUpRefund
    })
  }

  conditionSearch(params) {
    this.searchCondition = params
    this.getOrderListByPage({
      page: 0,
      size: this.state.pageSize,
      enterpriseId: this.props.auth.enterpriseId,
      ...params
    })
  }

  onExpandedRowsChange = (expandedRowKeys) => {
    this.setState({
      expandedRowKeys
    })
  }

  render() {
    const orderData = this.filterOrderListByStatus()

    return (
      <div className={'admin-container ' + styles['admin-orders']}>
        <Row>
          <Col span={4}>
            <LeftNavigation
              location={this.props.location.pathname}/>
          </Col>
          <Col span={20}>
            <div className="card-container">
              <h2>订单管理</h2>
              <SearchBar
                ref="searchBar"
                user={this.props.auth.user}
                onConditionSearch={::this.conditionSearch}
                onOrderIdSearch={this.handleSearchByOrderId}
              />
              {this.ordersTable(orderData)}
            </div>
          </Col>
        </Row>
        <EditOrderModal ref="editOrderModal" onCloseCallback={::this.refreshOrderList}/>
        <AuditPaymentModal ref="auditPaymentModal" afterSubmit={::this.refreshOrderList}/>
        <ReFundModal
          getOrderTransaction={::this.refreshOrderList}
          ref={(ref) => {
            this.RefundModal = ref
          }}
        />
      </div>
    )
  }
}

export default OrderList
