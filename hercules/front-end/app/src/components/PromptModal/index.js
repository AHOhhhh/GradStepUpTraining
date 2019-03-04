import React from 'react'  // eslint-disable-line
import cssModule from 'react-css-modules'
import {Modal, Button, Icon} from 'antd'

import styles from './index.module.scss'

const PromptModal = ({visible, onClose, content, iconType}) => {
  return (
    <Modal
      wrapClassName={styles['prompt-modal']}
      footer={null}
      visible={visible}
      closable={false}
    >
      <div className="content">
        <Icon type={iconType || 'exclamation-circle'} className="icon" />
        <div className="text">{content}</div>
      </div>
      <div className="operation">
        <Button className="button" onClick={onClose}>确定</Button>
      </div>
      
    </Modal>
  )
}

export default cssModule(PromptModal, styles)