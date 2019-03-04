import React, {Component, PropTypes} from 'react'
import cssModules from 'react-css-modules'
import styles from './step.module.scss'

class Step extends Component { // eslint-disable-line
  static propTypes = {
    id: PropTypes.number,
    name: PropTypes.string,
    interval: PropTypes.string,
    current: PropTypes.number
  };

  constructor(props) { // eslint-disable-line
    super(props);
  }

  getStatus(currentId, stepId) {
    if (currentId > stepId) {
      return 'done';
    } else if (currentId === stepId) {
      return 'active';
    }
    return 'inactive';
  }

  getImageSource(imageSources, currentId, stepId) {
    if (this.getStatus(currentId, stepId) === 'done') {
      return imageSources['step' + stepId + 'Done'];
    } else if (this.getStatus(currentId, stepId) === 'active') {
      return imageSources['step' + stepId + 'Active'];
    }
    return imageSources['step' + stepId + 'Inactive'];
  }

  getImageClass(currentId, stepId) {
    return this.getStatus(currentId, stepId) === 'active'
      ? 'icon icon-large'
      : 'icon icon-normal';
  }

  getNameClass(currentId, stepId) {
    if (this.getStatus(currentId, stepId) === 'done') {
      return 'nameDone';
    } else if (this.getStatus(currentId, stepId) === 'active') {
      return 'nameActive';
    }
    return 'nameInactive';
  }

  getDisplayName(id, name) {
    return 'Step' + id + ': ' + name;
  }

  render() {
    const {id, name, interval, current, imageSources, total} = this.props;
    return (
      <div className={styles.step} style={{width: interval}}>
        <div className="step">
          <div className="icon-box">
            <img className={this.getImageClass(current, id)} src={this.getImageSource(imageSources, current, id)}/>
          </div>
          {id === 1 && <div className="firstBar"/>}
          {id === total && <div className="lastBar"/>}
          {id !== 1 && id !== total && <div className="normalBar"/>}
        </div>

        <div className={this.getNameClass(current, id)}>{this.getDisplayName(id, name)}</div>

      </div>
    );
  }
}

export default cssModules(Step, styles);

