import React from 'react'
import cssModules from 'react-css-modules'
import {chunk} from 'lodash'

import styles from './index.module.scss'
import Attachments from './Attachments'

function renderTableRow(items) {
  return items.map((data, i) => {
    const key = i
    return (
      <tr key={key} styleName="info-row">
        <td styleName="prop-name-col">{data[0].propName + '：'}</td>
        <td styleName="value-col"> {data[0].value}</td>
        <td styleName="prop-name-col">{data[1] ? data[1].propName + '：' : ''}</td>
        <td styleName="value-col"> {data[1] ? data[1].value : ''}</td>
      </tr>
    )
  })
}

const renderTableGroup = (info) => {

  const renderData = info.map((block) => {
    return Object.assign({}, block, {
      items: chunk(block.items, 2)
    })
  })
  return renderData.map((data, index) => {
    const key = index
    return (
      <tbody key={key}>
        <tr styleName="title-row">
          <td colSpan="4">{data.title}</td>
        </tr>
        {renderTableRow(data.items)}
      </tbody>
    )
  }
  )
}


const InfoTableGroup = ({data, enterpriseInfo, attachments}) => {
  return (
    <div styleName="info-table-group">
      <table styleName="table-info">
        {renderTableGroup(data)}
      </table>
      {attachments ? <Attachments enterpriseInfo={enterpriseInfo}/> : ''}
    </div>
  )
}

export default cssModules(InfoTableGroup, styles, {allowMultiple: true})