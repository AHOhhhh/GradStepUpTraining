import React from 'react';
import {formatPrice, formatDate} from 'utils'

import cssModules from 'react-css-modules';
import styles from './index.module.scss';


const SCFOrderDetail = (props) => (
  <div styleName="scf-order-detail">
    <section styleName="basis-info">
      <section styleName="labeled-text">
        <span styleName="label">交易对手：</span>
        <div styleName="secondary">{props.counterPartyName}</div>
      </section>
    </section>

    <section styleName="size-info">
      <section styleName="labeled-text">
        <span styleName="label">账款规模：</span>
        <div styleName="secondary">{formatPrice(props.credit)}</div>
      </section>
      <section styleName="labeled-text">
        <span styleName="label">账款期限：</span>
        <div styleName="secondary">{formatDate(props.dueDate)}</div>
      </section>
    </section>

    <section styleName="purpose-info">
      <section styleName="labeled-text">
        <span styleName="label">意向金额：</span>
        <div styleName="secondary">{formatPrice(props.requireAmount)}</div>
      </section>

      <section styleName="labeled-text">
        <span styleName="label">意向利率：</span>
        <div styleName="secondary">{props.expectRate}</div>
      </section>
    </section>

    <section styleName="requirement-info">
      <section styleName="labeled-text">
        <span styleName="label">需求描述：</span>
        <div styleName="secondary">{props.description}</div>
      </section>
    </section>
  </div>
)

export default cssModules(SCFOrderDetail, styles, {allowMultiple: true});
