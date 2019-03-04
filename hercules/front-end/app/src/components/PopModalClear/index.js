import React, {Component} from 'react'

import BasicModal from '../BasicModal'
import styles from './index.module.scss'

class PopModalClear extends Component {
  constructor(props) {
    super(props);
    this.state = {
      filteredInfo: {},
      currentId: this.props.currentId,
      selectedRowKeys: []
    }
  }

  render() {
    return (
      <div>
        <BasicModal
          {...this.props}
          footer={[
            <button
              type="submit" className="button primary submit" onClick={() => this.props.handleSubmit(this.state.currentId)}>确定</button>,
            <button type="button" className="button" onClick={this.props.onCancel}>取消</button>
          ]}
        >
          <div className={styles.modalClear}>
            <div className="text">确定清空联系方式？</div>
          </div>
        </BasicModal>
      </div>
    );
  }
}

export default PopModalClear;
