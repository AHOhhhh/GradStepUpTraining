import React, { Component } from 'react'
import cssModules from 'react-css-modules'
import { Form, DatePicker, Input } from 'antd'
import { isEmpty, debounce } from 'lodash'
import moment from 'moment';

import styles from './index.module.scss'

const FormItem = Form.Item
const RangePicker = DatePicker.RangePicker
const Search = Input.Search;
const dateFormat = 'YYYY-MM-DD';

const getInitState = () => ({
  dateFrom: null,
  dateTo: null,
  searchText: null
})

class SearchBar extends Component {
  state = getInitState()

  handleChange = (params) => {
    const {onSearch, combineSearch = true} = this.props
    let newState
    if (combineSearch) {
      newState = Object.assign({}, this.state, params)
    } else {
      newState = Object.assign({}, getInitState(), params)
    }

    this.setState(newState, () => {
      onSearch(newState)
    })
  }

  handleDateChange = (values) => {
    const dateFrom = (!isEmpty(values) && values[0]) ? values[0].format(dateFormat) : null
    const dateTo = (!isEmpty(values) && values[1]) ? values[1].format(dateFormat) : null
    this.handleChange({ dateFrom, dateTo })
  }

  handleSearch = debounce((searchText) => {
    this.handleChange({searchText})
  }, 500)

  render() {
    const {dateFrom, dateTo} = this.state
    const dateValues = [
      dateFrom && moment(dateFrom, dateFormat),
      dateTo && moment(dateTo, dateFormat)
    ]
    const { placeholder = '输入企业名称 / 订单号' } = this.props
    return (
      <div className={styles.fundsSearchBar}>
        <Form layout="inline">
          <FormItem className="search">
            <Search
              placeholder={placeholder}
              onChange={(e) => { this.handleSearch(e.target.value) }}
            />
          </FormItem>
          <FormItem>
            <RangePicker
              onChange={this.handleDateChange}
              value={dateValues}
            />
          </FormItem>
        </Form>
      </div>
    )
  }
}

export default cssModules(SearchBar, styles)
