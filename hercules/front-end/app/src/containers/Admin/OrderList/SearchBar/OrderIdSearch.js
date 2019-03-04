import React, {Component} from 'react'
import {Form, Input, Icon} from 'antd'
import cssModules from 'react-css-modules'
import { debounce, noop } from 'lodash';

import styles from './index.module.scss'

const FormItem = Form.Item
class OrderSearch extends Component {
  state = {
    showSearchCondition: false
  }

  toggleSearchCondition = () => {
    const showSearchCondition = !this.state.showSearchCondition
    const {onToggle = noop} = this.props
    this.setState({showSearchCondition}, () => {
      onToggle(showSearchCondition)
    })
  }

  handleOrderIdSearch = debounce((value) => {
    this.props.onSearch(value)
  }, 500)

  render() {
    const iconType = this.state.showSearchCondition ? 'up' : 'down'
    return (
      <div className={styles['search-condition-toggler']}>
        <Form layout="inline">
          <FormItem>
            <Input.Search
              placeholder="输入订单号"
              onSearch={(value) => { this.handleOrderIdSearch(value) }}
              onChange={(e) => { this.handleOrderIdSearch(e.target.value) }}
            />
          </FormItem>
          <FormItem>
            <span className={styles['search-condition-toggle']} onClick={::this.toggleSearchCondition}>
              筛选条件
            </span>
            <span>
              <Icon type={iconType}/>
            </span>
          </FormItem>
        </Form>
      </div>
    )
  }

}

export default cssModules(OrderSearch)

