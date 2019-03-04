import {Tabs} from 'antd'
import React from 'react'
import {size} from 'lodash'
import cssModules from 'react-css-modules'
import {connect} from 'react-redux'
import {USER_ROLE} from 'constants'
import {bindActionCreators} from 'redux'
import * as action from './actions'
import styles from './index.module.scss'
import messageIcon from './assets/message.png'
import {MESSAGE_SIZE} from './constants'
import MessageList from './MessageList'

const TabPane = Tabs.TabPane;

const type = {
  notification: 'notification',
  logistics: 'logistics'
}

class Message extends React.Component {

  state = {
    notificationPageSize: 3,
    logisticsPageSize: 3
  }

  componentDidMount() {
    const isEnterpriseUser = this.props.user && this.props.user.role === USER_ROLE.enterpriseUser
    if (isEnterpriseUser) {
      this.props.actions.getNotification(type.notification, this.state.notificationPageSize)
        .then((data) => {
          this.setState({
            notificationPageSize: size(data.content),
          })
        })

      this.props.actions.getNotification(type.logistics, this.state.logisticsPageSize)
        .then((data) => {
          this.setState({
            logisticsPageSize: size(data.content)
          })
        })
    }
  }

  handleShowMore(type) {
    const requestSize = size(this.props[type].content) + 3
    this.props.actions.getNotification(type, requestSize)
      .then(data => {
        const valueObj = {...this.state}
        valueObj[MESSAGE_SIZE[type]] = size(data.content)
        this.setState(valueObj)
      })
  }

  render() {
    return (
      <div styleName="message">
        <h1 styleName="message-head">
          <img styleName="message-icon" src={messageIcon}/>
          <span>消息</span>
        </h1>
        <Tabs defaultActiveKey=".$notification">
          <TabPane tab="通知提醒" key="notification">
            <MessageList
              type="notification"
              currentSize={this.state.notificationPageSize}
              data={this.props.notification}
              handleShowMore={::this.handleShowMore}/>
          </TabPane>
          <TabPane tab="物流状态" key="logistics">
            <MessageList
              type="logistics"
              currentSize={this.state.logisticsPageSize}
              data={this.props.logistics}
              handleShowMore={::this.handleShowMore}
            />
          </TabPane>
        </Tabs>
      </div>
    )
  }
}

export default connect(
  state => ({
    user: state.auth.user,
    notification: state.auth.notification,
    logistics: state.auth.logistics
  }),
  dispatch => ({
    actions: bindActionCreators(action, dispatch)
  })
)(cssModules(Message, styles))
