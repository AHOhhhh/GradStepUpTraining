import React, {Component} from 'react'
import {Form, DatePicker} from 'antd'
import cssModules from 'react-css-modules'
import {noop, isEmpty} from 'lodash'
import moment from 'moment'

import OrderAndVendorSelect from './OrderAndVendorSelect'
import EnterpriseNameComplete from './EnterpriseNameComplete'
import styles from './index.module.scss'

const {RangePicker} = DatePicker
const FormItem = Form.Item

const dateFormat = 'YYYY-MM-DD';

class SearchCondition extends Component {
  state = {
    values: [],
    completeValue: ''
  }

  handleChange(params) {
    let values = [moment(params.dateFrom, dateFormat), moment(params.dateTo, dateFormat)]

    if (params.dateFrom === undefined) {
      values = this.state.values
    } else if (params.dateFrom === '') {
      values = []
    }

    this.setState({
      values
    }, () => {
      const condition = Object.assign({}, this.state.condition, params)
      const {onChange = noop} = this.props
      this.setState({condition}, () => {
        onChange(condition)
      })
    })
  }

  clear() {
    this.setState({
      values: [],
      completeValue: '',
      condition: {}
    })
    this.refs.select.clearSelect()
  }

  cancelValues() {
    this.setState({
      values: [],
      completeValue: '',
      condition: {}
    }, () => {
      const {onClear = noop} = this.props
      onClear()
    })
    this.refs.select.clearSelect()
  }

  handleChangeComplete(completeValue) {
    this.setState({
      completeValue
    })
  }

  render() {
    const styles = this.props.isShow ? {} : {display: 'none'}
    return (
      <div style={styles} styleName="search-condition">
        <Form layout="inline">
          <FormItem>
            <RangePicker
              onChange={(values) => {
                const dateFrom = !isEmpty(values) ? values[0].format(dateFormat) : ''
                const dateTo = !isEmpty(values) ? values[1].format(dateFormat) : ''
                this.handleChange({dateFrom, dateTo})
              }}
              value={this.state.values}/>
          </FormItem>
          <FormItem>
            <OrderAndVendorSelect
              onChange={(type, vendor) => {
                this.handleChange({type, vendor})
              }}
              user={this.props.user}
              ref="select"/>
          </FormItem>
          <FormItem>
            <EnterpriseNameComplete
              onSelect={(enterpriseId) => {
                if (enterpriseId) {
                  this.handleChange({enterpriseId})
                }
              }}
              changeCompleteValue={(completeValue) => this.handleChangeComplete(completeValue)}
              value={this.state.completeValue}
            />
          </FormItem>
          <FormItem>
            <a styleName="clear" onClick={::this.cancelValues}>重置条件</a>
          </FormItem>
        </Form>
      </div>
    )
  }
}

export default cssModules(SearchCondition, styles, {allowMultiple: true})
