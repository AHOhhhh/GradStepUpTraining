import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import Icon from 'antd/lib/icon'

import cssModules from 'react-css-modules'
import styles from './index.module.scss'
import * as actions from '../../OfferSelectionStep/actions'

export class LoadingPage extends Component {
  state = {
    loading: false
  }

  refresh() {
    this.setState({
      loading: true
    })
    const orderId = this.props.order.id;
    this.props.actions.getOrder(orderId)
      .then(() => {
        this.setState({
          loading: false
        })
      })
      .catch(() => {
        this.setState({
          loading: false
        })
      })
  }


  render() {
    const preview = this.props.preview
    return (
      <div className={styles['load-container']}>
        <p className="load-info">{preview ? '已有多家服务商与客户进行接洽！' : '目前有多家服务商为您报价，请耐心等待！'}</p>
        <p className="wait-time">{preview ? '' : '报价平均时长为24小时'}</p>
        <p className="load-fresh">
          {this.state.loading ? (<Icon type="loading"/>) : (
            <div>
              <span className="fresh" onClick={::this.refresh}>刷新</span>
              <span className="page-status"> , 查看状态更新</span>
            </div>
          )}

        </p>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    order: state.wisePortOrder.order
  }
}


const mapDispatchToProps = dispatch => ({
  actions: bindActionCreators(actions, dispatch)
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(cssModules(LoadingPage, styles, {allowMultiple: true}))
