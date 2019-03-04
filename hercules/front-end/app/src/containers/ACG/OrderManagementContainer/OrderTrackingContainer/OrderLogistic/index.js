import React, { Component, PropTypes } from 'react'
import _ from 'lodash'
import moment from 'moment'
import { Icon } from 'antd'
import cssModules from 'react-css-modules'
import styles from './index.module.scss'

const withCssModules = (component) => cssModules(component, styles)

class OrderLogistics extends Component {
  static propTypes = {
    order: PropTypes.object.isRequired
  }
  static defaultProps = {
    order: {}
  }
  state = {
    hidden: true
  }
  setHidden(hidden) {
    this.setState({ hidden })
  }
  renderShowMore() {
    const { needShowMore = true } = this.props
    if (needShowMore) {
      const hidden = this.state.hidden
      return (
        <span styleName="show-more" onClick={() => this.setHidden(!hidden)}>
          <a>
            <span>{hidden ? '展开更多' : '收起更多'}</span>
            <Icon styleName="icon" type={hidden ? 'down' : 'up'} />
          </a>
        </span>
      )
    }
  }
  renderInfo(logisticsStatus) {
    const { warningMessage } = this.props
    if (_.isEmpty(logisticsStatus)) {
      return (
        <li styleName="empty">
          <span>{warningMessage || '暂无物流信息'}</span>
        </li>
      )
    }

    return (
      logisticsStatus.map(({updateInfoTime, logisticsStatusInfo}, index) =>
        <li key={index}>
          <span>{moment(updateInfoTime).format('YYYY-MM-DD HH:mm:ss')}</span>
          <span styleName="info">{logisticsStatusInfo}</span>
          {index === 0 && this.renderShowMore()}
        </li>
      )
    )
  }
  render() {
    const { order: { logisticsStatus = [] }, needTitle = true, needShowMore = true } = this.props
    return (
      <div styleName="order-logistics">
        <div styleName="wrapper">
          {needTitle && <h2>订单物流跟踪</h2>}
          <ul styleName={(this.state.hidden && needShowMore) ? 'hidden' : ''}>
            {this.renderInfo(logisticsStatus)}
          </ul>
        </div>
      </div>
    )
  }
}

export default withCssModules(OrderLogistics)