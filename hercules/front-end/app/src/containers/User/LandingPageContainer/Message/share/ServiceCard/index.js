import React from 'react'
import cssModules from 'react-css-modules'
import styles from './index.module.scss'

const ServiceCard = ({name, style}) => {
  return (
    <div styleName={style}>
      {name}
    </div>
  )
}

export default cssModules(ServiceCard, styles, {allowMultiple: true})
