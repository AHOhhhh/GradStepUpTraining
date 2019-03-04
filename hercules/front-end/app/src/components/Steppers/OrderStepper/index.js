import React, {Component, PropTypes} from 'react'
import cssModules from 'react-css-modules'
import styles from './index.module.scss'
import Step from './NewStep'


class OrderStepper extends Component { // eslint-disable-line
  static propTypes = {
    data: PropTypes.array,
    interval: PropTypes.string,
    current: PropTypes.number,
    imageSources: PropTypes.object
  };

  constructor(props) { // eslint-disable-line
    super(props);
  }

  render() {
    const {data, interval, current, imageSources} = this.props;
    return (
      <div className={styles.orderStepper}>
        {data.sort((a, b) => (a.id > b.id))
          .map(step => (
            <Step key={step.id} id={step.id} name={step.name} interval={interval} current={current} imageSources={imageSources} total={data.length}/>
          ))}
      </div>
    );
  }
}

export default cssModules(OrderStepper, styles);

