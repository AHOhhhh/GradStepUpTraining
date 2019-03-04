import React from 'react'

import cssModules from 'react-css-modules'
import styles from './step.module.scss'


const Step = (props) => (
  <div styleName={'step ' + props.status} style={{width: props.width}} key={props.key}>
    <div styleName="icon"/>
    <div styleName="title">{props.title}</div>
    <div>{props.description}</div>
  </div>
)

export default cssModules(Step, styles, {allowMultiple: true})
