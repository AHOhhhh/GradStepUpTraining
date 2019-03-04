import React from 'react'
import {Timeline} from 'antd';
import moment from 'moment'
import cssModule from 'react-css-modules'
import {isNumber} from 'lodash';
import styles from './index.module.scss';

const formatDate = (str) => {
  const thisMoment = isNumber(str) ? moment.unix(str) : moment(str)
  return thisMoment.format('YYYY-MM-DD HH:mm:ss')
}

const WisePortTimeLine = ({operationLogs = []}) => {
  return (
    <div styleName="operation-document">
      <h2 styleName="operation-title">操作记录</h2>
      <div styleName="time-line">
        <Timeline>
          {operationLogs.map((doc, index) => {
            const clazz = (index === 0) ? 'current-content' : 'content'
            const color = (index === 0) ? 'white' : ''
            return (
              <Timeline.Item color={color} key={index}>
                <span className={styles[clazz]} styleName="created-time">{formatDate(doc.createdAt)}</span>
                <span className={styles[clazz]} styleName="detail">{doc.operationName}</span>
                <span
                  className={styles[clazz]} styleName="created-user">办理人：{doc.operatorName}</span>
              </Timeline.Item>
            )
          })}
        </Timeline>
      </div>
    </div>
  )
}

export default cssModule(WisePortTimeLine, styles, {allowMultiple: true})

