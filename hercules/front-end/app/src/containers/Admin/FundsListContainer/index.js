import React, { Component, createElement } from 'react'
import { connect } from 'react-redux'
import { Row, Col, message } from 'antd/lib'

import {getFundsOrders} from 'actions'
import LeftNavigation from 'components/LeftNavigation'
import FundsSearchBar from '../share/SearchBar'
import FundsTable from './FundsTable'

@connect(
  state => ({
    user: state.auth.user,
    fundsInfo: state.fundsInfo
  }),
  {getFundsOrders}
)
export default class FundsListContainer extends Component {
  state = {
    condition: {},
    pageSize: 10
  }

  componentDidMount() {
    this.handleConditionChange()
  }

  changePageSize(pageNum, pageSize) {
    this.setState({pageSize}, () => {
      this.handleChangePage(pageNum)
    })
  }

  renderTable() {
    const { fundsInfo: { list, page, total, loading }, user = {} } = this.props

    return createElement(FundsTable, {
      showSizeChanger: true,
      current: page,
      user,
      list,
      total,
      loading,
      pageSize: this.state.pageSize,
      changePage: this.handleChangePage,
      onShowSizeChange: ::this.changePageSize
    })
  }

  search = (params) => {
    this.props.getFundsOrders(params)
      .catch(() => {
        message.error('查询失败，请稍后再试')
      })
  }

  handleConditionChange = (params = {}) => {
    const newCondition = { ...params, page: 0 }
    this.setState({ condition: newCondition }, () => {
      this.search(newCondition)
    })
  }

  handleChangePage = (current) => {
    const { condition, pageSize } = this.state
    this.search({ ...condition, pageSize, page: current - 1 })
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
              <h2>资金流水</h2>
              <FundsSearchBar onSearch={this.handleConditionChange}/>
              {this.renderTable()}
            </div>
          </Col>
        </Row>
      </div>
    )
  }
}

