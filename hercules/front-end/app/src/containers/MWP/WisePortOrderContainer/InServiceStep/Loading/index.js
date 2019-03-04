import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import cssModules from 'react-css-modules'

import * as actions from '../actions'

import styles from './index.module.scss'


class Loading extends Component {
  
  hanleRefresh = () => {
    this.props.actions.getInServiceProductOffers(this.props.orderId)
  }
  
  render() {
    return (
      <div styleName="loading-container">
        <div styleName="hints">
          <p>服务商刚开始服务，暂未更新服务状态。</p>
        </div>
        <div styleName="operation">
          <span styleName="refresh-button" onClick={this.hanleRefresh}>刷新</span>
          <span> , 查看状态更新</span>
        </div>
      </div>
    )
  }
}

const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(actions, dispatch)
})


export default connect(null, mapDispatchToProps)(cssModules(Loading, styles, {allowMultiple: true}))
