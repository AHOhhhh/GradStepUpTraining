import React from 'react'
import cssModules from 'react-css-modules'
import styles from './index.module.scss'

const ModalFooterButtons = (props) => (
  <div styleName="modal-footer">
    <button className="button primary" styleName="button" onClick={props.onOk}>{props.okText}</button>
    <button className="button" styleName="button cancel" onClick={props.onCancel}>{props.cancelText}</button>
  </div>
)

export default cssModules(ModalFooterButtons, styles, {allowMultiple: true})
