import React from 'react'
import cssModules from 'react-css-modules'

import styles from './index.module.scss'

const RHSActionPanel = (props) => (
  <div styleName="action-panel">
    {props.children}
  </div>
)

export default cssModules(RHSActionPanel, styles)