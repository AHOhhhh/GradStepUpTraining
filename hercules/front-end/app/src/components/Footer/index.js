import React from 'react';
import cssModules from 'react-css-modules';
import styles from './index.module.scss';
import items from './data/items';

const itemsMap = items.map(item => (
  <div styleName="item" key={item.key}>
    <a target="_blank" rel="noopener noreferrer" href={item.url}>{item.title}</a>
  </div>
));

const Footer = () => (
  <footer styleName="footer">
    <div className="container" styleName="container">
      <div className="row" styleName="row">
        <div styleName="col-left">
          <div styleName="image" />
        </div>
        <div styleName="col-right">
          <div styleName="group"><span styleName="icon link" /> <h3>友情链接</h3>
            {itemsMap}
          </div>
          {/* <div styleName="copyright">沪ICP备14043888号-2</div> */}
        </div>
      </div>
    </div>
  </footer>
);

export default cssModules(Footer, styles, {allowMultiple: true});