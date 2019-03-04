import React from 'react';
import { formatPrice, formatDate } from 'utils'
import cssModules from 'react-css-modules';
import styles from './index.module.scss';

const getAccountTimeLimit = (props) => {
  const hideDateStatus = ['Submitted'];
  if (hideDateStatus.includes(props.status)) {
    return '包年'
  }
  return `${formatDate(props.effectiveFrom)} - ${formatDate(props.effectiveTo)}`
}

const WMSOrderDetail = (props) => {
  if (props.orderSubTypes.includes('Recharge')) {
    return (
      <div styleName="wms-order-detail">
        <section styleName="block">
          <section styleName="labeled-text">
            <span styleName="label">核定价格：</span>
            <div styleName="secondary">{props.approvedPrice !== null ? formatPrice(props.approvedPrice) : '面议'}</div>
          </section>
        </section>
        <section styleName="block">
          <section styleName="labeled-text">
            <span styleName="label">服务商电话：</span>
            <div styleName="secondary">{props.serviceTelephone}</div>
          </section>
        </section>
      </div>
    )
  }

  return (
    <div styleName="wms-order-detail">
      <section styleName="basis-info">
        <section styleName="labeled-text">
          <span styleName="label">账号期限：</span>
          <div styleName="secondary">{getAccountTimeLimit(props)}</div>
        </section>

        <section styleName="labeled-text">
          <span styleName="label">价格区间：</span>
          <div styleName="secondary">{formatPrice(props.minPrice)} - {formatPrice(props.maxPrice)}</div>
        </section>

        <section styleName="labeled-text">
          <span styleName="label">核定价格：</span>
          <div styleName="secondary">{props.approvedPrice !== null ? formatPrice(props.approvedPrice) : '面议'}</div>
        </section>
      </section>

      <section styleName="vendor-info">
        <section styleName="labeled-text">
          <span styleName="label">服务商电话：</span>
          <div styleName="secondary">{props.serviceTelephone}</div>
        </section>
      </section>

      <section styleName="block">
        <section styleName="labeled-text">
          <span styleName="label">服务内容：</span>
          <div styleName="secondary">{props.serviceIntro}</div>
        </section>
      </section>
    </div>
  )
}

export default cssModules(WMSOrderDetail, styles, { allowMultiple: true });
