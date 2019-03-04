import React from 'react'  // eslint-disable-line
import cssModule from 'react-css-modules'
import {Modal, Button, Icon} from 'antd'

import styles from '../PromptModal/index.module.scss'

const PromptModalWithCancel = ({visible, onClose, onOk, content, iconType}) => {
  return (
    <Modal
      wrapClassName={styles['prompt-modal']}
      footer={null}
      visible={visible}
      closable={false}
    >
      <div className="content">
        <Icon type={iconType || 'exclamation-circle'} className="icon warning" />
        <div className="text">{content}</div>
      </div>
      <div className="operation with-cancel">
        <Button className="button" onClick={onOk}>确定</Button>
        <Button className="button" onClick={onClose}>取消</Button>
      </div>
    </Modal>
  )
}

export default cssModule(PromptModalWithCancel, styles)