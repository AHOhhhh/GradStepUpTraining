import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import {map} from 'lodash'
import {Row, Col} from 'antd'

import styles from './index.module.scss'

class GoodList extends Component {
  constructor(props) {
    super(props)
    this.state = {
      isDisplayAll: false
    }
  }

  generateGoodsList = (goods) => {
    return (<ol className={styles.lineItems}>{
      map(goods, (good, index) => {
        return (<li key={index} className={this.state.isDisplayAll || (index === 0) ? styles.lineItem : styles.hiddenItem}>
          <Row>
            <Col span={3}><span className={styles.goodIcon}/></Col>
            <Col span={21} className={styles.goodName}>{good.name}</Col>
          </Row>
        </li>)
      })
    }</ol>)
  }

  changeGoodList = () => {
    this.setState({
      isDisplayAll: !this.state.isDisplayAll
    })
  }

  render() {
    const goods = this.props.goods || []
    return (<div className={styles.goodList}>
      {this.generateGoodsList(goods)}
      <span className={goods.length <= 1 ? styles.hiddenStatus : styles.status} onClick={this.changeGoodList}>{this.state.isDisplayAll ? '收起全部' : '展开全部'}</span>
    </div>)
  }
}

export default cssModules(GoodList, styles, {allowMultiple: true});