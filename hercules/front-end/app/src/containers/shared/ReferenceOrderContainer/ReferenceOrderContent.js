import React, {PropTypes} from 'react'
import cssModules from 'react-css-modules'
import { Link } from 'react-router'
import { Icon } from 'antd'
import styles from './ReferenceOrderContent.module.scss'

const ReferenceOrderContent = ({data: {title, tip, link, location, imgSrc}}) => (
  <div styleName="adContainer">
    <div styleName="adContent">
      <div>
        <p styleName="title">{title}</p>
        <p styleName="tips"><i/>{tip}</p>
      </div>
      <Link styleName="link" to={location}>
        {link}<Icon type="arrow-right"/>
      </Link>
    </div>
    <div styleName="imageContainer">
      <img src={imgSrc} alt="mwp" />
    </div>
  </div>)

ReferenceOrderContent.propTypes = {
  data: PropTypes.shape({
    title: PropTypes.string,
    tip: PropTypes.string,
    link: PropTypes.string,
    location: PropTypes.object,
    imgSrc: PropTypes.any
  }).isRequired
}

export default cssModules(ReferenceOrderContent, styles)
