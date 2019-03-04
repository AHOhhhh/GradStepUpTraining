import React from 'react';
import cssModules from 'react-css-modules';
import styles from './index.module.scss';
import {official} from '../../config/url'

const host = official.home

const ProductDropdown = () => (
  <div styleName="cooperate-dropdown">
    <ul styleName="css-reset">
      <li><i styleName="icon icon-car" /><a href={`${host}/cooperate/car`}>我有车</a></li>
      <li><i styleName="icon icon-money" /><a href={`${host}/cooperate/fund`}>我有资金</a></li>
      <li><i styleName="icon icon-store" /><a href={`${host}/cooperate/store`}>我有仓库</a></li>
      <li><i styleName="icon icon-software" /><a href={`${host}/cooperate/connect`}>我要对接</a></li>
    </ul>
  </div>
);

export default cssModules(ProductDropdown, styles, {allowMultiple: true});