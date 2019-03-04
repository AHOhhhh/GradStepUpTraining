import React from 'react'
import cssModules from 'react-css-modules'
import styles from './index.module.scss'
import noContentIcon from '../../assets/noContent.png'

const NoContent = () => {
  return (
    <div styleName="no-content-container">
      <img src={noContentIcon} styleName="no-content"/>
      <span>暂无数据</span>
    </div>
  )
}

export default cssModules(NoContent, styles)
