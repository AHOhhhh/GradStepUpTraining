import React from 'react';
import Modal from 'antd/lib/modal';
import styles from './index.module.scss';

const BasicModal = (props) => (
  <div>
    <Modal
      {...props}
      wrapClassName={styles.modal + ' container'}
    >
      <div className="customize-content">
        {props.children}
      </div>
    </Modal>
  </div>
)

export default BasicModal
