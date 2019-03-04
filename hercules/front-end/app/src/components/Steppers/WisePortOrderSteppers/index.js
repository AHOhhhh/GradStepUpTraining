import React, {PropTypes} from 'react'; // eslint-disable-line
import cssModules from 'react-css-modules';
import Progress from 'antd/lib/progress'

import OrderStepItem from './OrderStepItem'
import styles from './index.module.scss';

const WisePortOrderSteppers = ({data, current}) => {
  const firstItem = data[0]
  const restItems = data.slice(1)

  const progress = ((current - 1) / restItems.length) * 100;
  const itemWidth = (100 / restItems.length) + '%'

  return (
    <div styleName="wise-port-order-steppers">
      <Progress percent={progress} showInfo={false} strokeWidth={6}/>
      <OrderStepItem item={firstItem} current={current} extraClass="first-step" />
      <ul>
        {
          restItems.map(item => (
            <li style={{width: itemWidth}} key={item.id}>
              <OrderStepItem item={item} current={current} />
            </li>
          ))
        }
      </ul>
    </div>
  );
}

WisePortOrderSteppers.propTypes = {
  data: PropTypes.array.isRequired,
  current: PropTypes.number.isRequired
}

export default cssModules(WisePortOrderSteppers, styles, {allowMultiple: true});
