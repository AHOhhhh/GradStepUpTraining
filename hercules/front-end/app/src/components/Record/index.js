import React from 'react'
import { map, isNumber } from 'lodash'
import moment from 'moment'
import styles from './styles/record.module.scss'

const formatDate = (str) => {
  const thisMoment = isNumber(str) ? moment.unix(str) : moment(str)
  return thisMoment.format('YYYY.MM.DD HH:mm:ss')
}

const recordMapper = ({operatorName, operationName, createdAt}, index) => (
  <li key={index}>
    <span>{formatDate(createdAt)}</span>
    <span>{operationName}</span>
    <span>
      {'办理人：' + operatorName}
    </span>
  </li>
)

const Record = ({data}) => (
  <div className={styles.wrapper}>
    <ul className={styles.record_wrapper}>
      {map(data, recordMapper)}
    </ul>
  </div>
)

export default Record;
