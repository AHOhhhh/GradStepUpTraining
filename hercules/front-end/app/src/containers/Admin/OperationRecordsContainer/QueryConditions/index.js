import React, {Component} from 'react' // eslint-disable-line
import {Form, Input} from 'antd'
import cssModules from 'react-css-modules'
import DatePicker from 'antd/lib/date-picker'
import styles from './index.module.scss'

const {RangePicker} = DatePicker

const FormItem = Form.Item

class QueryConditions extends Component {
  resetFields() {
    this.props.form.resetFields()
  }

  handleSearch = () => {
    this.props.form.validateFields((err, value) => {
      if (!err) {
        this.props.handleSearch(value.searchValue)
      }
    })
  }

  render() {
    const {getFieldDecorator} = this.props.form

    return (<div styleName="query-conditions">
      <Form layout="inline">
        <FormItem>
          {getFieldDecorator('dates', {
            initialValue: []
          })(
            <RangePicker
              styleName="date-range" onChange={::this.props.selectDate}/>
          )}
        </FormItem>
        <FormItem>
          {getFieldDecorator('searchValue')(
            <Input.Search
              styleName="search-box"
              placeholder={this.props.placeholder}
              onSearch={::this.handleSearch}
            />
          )}
        </FormItem>
      </Form>
    </div>)
  }
}

export default Form.create()(cssModules(QueryConditions, styles))

