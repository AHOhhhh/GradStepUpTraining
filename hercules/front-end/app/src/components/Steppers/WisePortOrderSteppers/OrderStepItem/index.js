import React, {PropTypes} from 'react';
import cssModules from 'react-css-modules';
import Icon from 'antd/lib/icon'

import styles from './index.module.scss';

const getIconType = (item, current) => {
  switch (true) {
    case item.id > current:
      return 'custom-waiting';
    case item.id === current:
      return item.iconType;
    case item.id < current:
      return 'check';
    default:
      return '';
  }
}

const getWrapperClass = (itemId, current) => {
  switch (true) {
    case itemId > current:
      return 'waiting';
    case itemId === current:
      return 'progressing';
    case itemId < current:
      return 'finished';
    default:
      return '';
  }
}

const OrderStepItem = ({item, current, extraClass}) => (
  <span styleName={'order-step-item-wrapper ' + extraClass + ' ' + getWrapperClass(item.id, current)}>
    {item.img
      ? <div styleName="item-img"><img src={item.img} alt="order-icon" /></div>
      : <Icon type={getIconType(item, current)}/>}
    <span styleName="item-text">{item.text}</span>
    <span styleName="triangle" hidden={item.id !== current}/>
  </span>)

OrderStepItem.propTypes = {
  item: PropTypes.object.isRequired,
  current: PropTypes.number.isRequired,
  extraClass: PropTypes.string
}

OrderStepItem.defaultProps = {
  extraClass: '',
}

export default cssModules(OrderStepItem, styles, {allowMultiple: true})
