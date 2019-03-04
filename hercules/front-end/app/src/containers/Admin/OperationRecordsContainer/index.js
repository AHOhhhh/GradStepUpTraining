import React, {Component, createElement} from 'react' // eslint-disable-line
import {connect} from 'react-redux'
import {isEmpty, includes} from 'lodash'

import cssModules from 'react-css-modules'
import Tabs from 'antd/lib/tabs'
import Row from 'antd/lib/row'
import Col from 'antd/lib/col'

import message from 'antd/lib/message'

import LeftNavigation from 'components/LeftNavigation'

import {getPlatformOperations, getOrderOperations} from 'actions'
import {formatDate} from 'utils/format'
import {USER_ROLE} from 'constants'

import PlatformManagement from './PlatformManagement';
import PlatformOrders from './PlatformOrders'
import QueryConditions from './QueryConditions'
import styles from './index.module.scss'

const TabPane = Tabs.TabPane
const PLATFORM_MANAGEMENT = 'PLATFORM_MANAGEMENT'
const PLATFORM_ORDERS = 'PLATFORM_ORDERS'

class OperationRecordsContainer extends Component { // eslint-disable-line
  constructor(props) {
    super(props)
    this.state = {
      pageSize: 10,
      selectedTab: 'platform',
      loading: false,
      current: 1,
      searchValue: '',
      fromDate: '',
      toDate: '',
    }
  }

  componentWillMount() {
    this.getOperationsByPage({
      role: USER_ROLE.platformAdmin,
      page: 0,
      size: this.state.pageSize,
      fromDate: this.state.fromDate,
      toDate: this.state.toDate,
    })
  }

  componentWillUnMount() {
    this.resetStates()
  }

  resetStates() {
    this.setState({
      pageSize: 10,
      selectedTab: 'platform',
      loading: false,
      current: 1,
      searchValue: '',
      fromDate: '',
      toDate: '',
    })
  }

  getOperationsByPage(pageInfo) {
    this.setState({loading: true, current: pageInfo.page + 1})
    if (this.state.selectedTab === 'order') {
      this.props.getOrderOperations(Object.assign({}, pageInfo, {orderId: this.state.searchValue}))
        .then(() => this.setState({loading: false}))
        .catch(() => {
          message.error('查询失败，请稍后再试')
          this.setState({loading: false})
        })
    } else {
      this.props.getPlatformOperations(Object.assign({}, pageInfo, {enterpriseName: this.state.searchValue}))
        .then(() => this.setState({loading: false}))
        .catch(() => {
          message.error('查询失败，请稍后再试')
          this.setState({loading: false})
        })
    }
  }

  selectDate(values) {
    const fromDate = !isEmpty(values) ? formatDate(values[0]) : ''
    const toDate = !isEmpty(values) ? formatDate(values[1]) : ''
    this.setState({fromDate, toDate}, () => {
      this.handleChangePage(this.state.current)
    })
  }

  changePageSize(pageNum, pageSize) {
    this.setState({current: pageNum, pageSize}, () => {
      this.handleChangePage(this.state.current)
    })
  }

  generatePaginationConfig(data) {
    return {
      showSizeChanger: true,
      total: data.totalElements,
      pageSize: this.state.pageSize,
      current: this.state.current,
      onChange: ::this.handleChangePage,
      onShowSizeChange: ::this.changePageSize
    }
  }

  renderOperationsTable(operationData, component) {
    return createElement(component, {
      operations: operationData.content,
      loading: this.state.loading,
      pagination: this.generatePaginationConfig(operationData)
    })
  }

  handleChangePage(current) {
    this.getOperationsByPage({
      role: USER_ROLE.platformAdmin,
      page: current - 1,
      size: this.state.pageSize,
      fromDate: this.state.fromDate,
      toDate: this.state.toDate
    })
  }

  changeTab(key) {
    this.resetStates()
    this.refs.queryConditions.resetFields()
    if (includes(key, PLATFORM_MANAGEMENT)) {
      this.setState({selectedTab: 'platform'}, () => {
        this.handleChangePage(this.state.current)
      })
    } else if (includes(key, PLATFORM_ORDERS)) {
      this.setState({selectedTab: 'order'}, () => {
        this.handleChangePage(this.state.current)
      })
    }
  }

  handleSearch(value) {
    this.setState({searchValue: value}, () => {
      this.handleChangePage(this.state.current)
    })
  }

  render() {
    const platformOperationData = this.props.platformOperationsInfo || {}
    const orderOperationData = this.props.orderOperationsInfo || {}

    return (<div className="admin-container" styleName="operation-records-container">
      <Row>
        <Col span={4}>
          <LeftNavigation
            location={this.props.location.pathname}/>
        </Col>
        <Col span={20}>
          <div styleName="card-container">
            <h2>操作记录</h2>
            <Tabs type="card" onTabClick={::this.changeTab}>
              <TabPane tab="平台管理" key={PLATFORM_MANAGEMENT}>
                <div styleName="operations-table">
                  {this.renderOperationsTable(platformOperationData, PlatformManagement)}
                </div>
              </TabPane>
              <TabPane tab="平台订单" key={PLATFORM_ORDERS}>
                <div styleName="operations-table">
                  {this.renderOperationsTable(orderOperationData, PlatformOrders)}
                </div>
              </TabPane>
            </Tabs>
            <QueryConditions
              ref="queryConditions" placeholder={this.state.selectedTab === 'order' ? '输入订单号' : '输入企业名称'}
              selectDate={::this.selectDate} handleSearch={::this.handleSearch}/>
          </div>
        </Col>
      </Row>
    </div>)
  }
}

export default connect(
  state => ({
    user: state.auth.user,
    platformOperationsInfo: state.admin.platformOperations,
    orderOperationsInfo: state.admin.orderOperations
  }),
  {getPlatformOperations, getOrderOperations})(cssModules(OperationRecordsContainer, styles))