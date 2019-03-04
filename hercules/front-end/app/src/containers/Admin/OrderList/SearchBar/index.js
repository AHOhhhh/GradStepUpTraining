import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import {noop, isEqual} from 'lodash'
import {omitBy} from 'lodash/fp'
import {ALL_STATUS} from "containers/Admin/OrderList/constants"; // eslint-disable-line

import OrderIdSearch from './OrderIdSearch'
import OrderMenus from './OrderMenus'
import SearchCondition from './SearchCondition'
import styles from './index.module.scss'

class SearchBar extends Component {
  state = {
    showSearchCondition: false,
    searchParams: {
      status: ALL_STATUS,
    }
  }

  doConditionSearch(queryParams) {
    const rejectParamAll = omitBy((value) => (value.toLowerCase() === 'all'))
    const rejectEmptyValue = omitBy((value) => (!value))
    const params = rejectParamAll(rejectEmptyValue(queryParams))
    const {onConditionSearch = noop} = this.props
    onConditionSearch(params)
  }

  doOrderIdSearch(orderId) {
    const {onOrderIdSearch = noop} = this.props
    this.setState({searchParams: {status: ALL_STATUS}})
    this.refs.searchCondition.clear()
    onOrderIdSearch(orderId)
  }

  handleStatusMenuClick(status) {
    const searchParams = Object.assign({}, this.state.searchParams, {status})
    if (isEqual(searchParams, this.state.searchParams)) {
      return
    }
    this.setState({searchParams}, () => {
      this.doConditionSearch(searchParams)
    })
  }

  toggleSearchCondition() {
    const showSearchCondition = !this.state.showSearchCondition
    this.setState({showSearchCondition})
  }

  handleSearchConditionChange(condition) {
    const searchParams = Object.assign({}, this.state.searchParams, condition)
    if (isEqual(searchParams, this.state.searchParams)) {
      return
    }
    this.setState({searchParams}, () => {
      this.doConditionSearch(searchParams)
    })
  }

  handleClearCondition() {
    const status = this.state.searchParams.status
    this.setState({
      searchParams: {status}
    }, () => {
      this.doConditionSearch({status})
    })
  }

  render() {
    return (
      <div>
        <OrderIdSearch onSearch={::this.doOrderIdSearch} onToggle={::this.toggleSearchCondition}/>
        <OrderMenus selectedKey={this.state.searchParams.status} onClick={::this.handleStatusMenuClick}/>
        <SearchCondition
          ref="searchCondition"
          user={this.props.user}
          onClear={::this.handleClearCondition}
          isShow={this.state.showSearchCondition}
          onChange={(values) => {
            this.handleSearchConditionChange(values)
          }}
        />
      </div>
    )
  }
}

export default cssModules(SearchBar, styles, {allowMultiple: true})