import React, {Component, PropTypes} from 'react'; // eslint-disable-line

import cssModules from 'react-css-modules';
import Modal from 'antd/lib/modal';
import styles from './index.module.scss';

class PopModal extends Component { // eslint-disable-line
  static propTypes = {
    title: PropTypes.string,
    handleSubmit: PropTypes.func,
    onCancel: PropTypes.func,
    visible: PropTypes.bool,
    width: PropTypes.string,
    height: PropTypes.string
  };

  render() {
    return (
      <div>
        <Modal
          mask={true}
          width={this.props.width}
          bodyStyle={{height: this.props.height}}
          title={this.props.title}
          visible={this.props.visible}
          onCancel={this.props.onCancel}
          wrapClassName={styles.modal + ' container'}
        >
          <div className="customerContent">
            {this.props.children}
          </div>
          <div className="operate">
            <button type="submit" className="button primary submit" onClick={this.props.handleSubmit}>确定</button>
            <button type="button" className="button" onClick={this.props.onCancel}>取消</button>
          </div>
        </Modal>
      </div>
    );
  }
}

export default cssModules(PopModal, styles);
