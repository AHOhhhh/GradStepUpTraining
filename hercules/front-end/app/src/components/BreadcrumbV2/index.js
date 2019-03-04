import React, {Component, PropTypes} from 'react'; // eslint-disable-line
import {Link} from 'react-router';

import cssModules from 'react-css-modules';
import Breadcrumb from 'antd/lib/breadcrumb';
import styles from './index.module.scss';


class Bread extends Component { // eslint-disable-line
  constructor(props) { // eslint-disable-line
    super(props);
  }

  render() {
    const breadcrumb = this.props.breadcrumb;
    const items = breadcrumb.paths.map(path => <Breadcrumb.Item key={path}><Link to={path.url}>{path.item}</Link></Breadcrumb.Item>);
    return (
      <div className={styles.breadcrumb}>
        <Breadcrumb separator=">">
          {items}
          <Breadcrumb.Item className="current-page">{this.props.breadcrumb.currentPage}</Breadcrumb.Item>
        </Breadcrumb>
      </div>
    );
  }
}

Bread.propTypes = {
  breadcrumb: PropTypes.object.isRequired, // eslint-disable-line
};

export default cssModules(Bread, styles);
