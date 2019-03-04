import React from 'react'
import cssModules from 'react-css-modules'
import { BasicModal } from 'components'
import styles from './index.module.scss'

const getFooter = ({status, handleSubmit, onCancel}) => {
  const submitBtn = (<button
    type="submit" className="button primary" styleName="button"
    onClick={handleSubmit}>提交订单</button>)

  const cancelBtn = (text) => (<button
    type="button" className="button" styleName="button cancel"
    onClick={onCancel}>{text}</button>)

  const tips = () => (
    <div styleName="tips">
      <span styleName="tips-icon"/>
      <span styleName="tips-info">该报价为参考报价，最终报价请参照支付页面报价。</span>
    </div>
  )
  return [
    <div key="modalFooter" styleName="modal-footer">
      {status === 'success' && submitBtn}
      {status === 'success' && cancelBtn('取消')}
      {status === 'success' && tips()}
      {status === 'error' && cancelBtn('关闭')}
    </div>
  ]
}

const OrderPriceModal = ({onCancel, handleSubmit, orderPrice, ...rest}) => {
  const {status} = orderPrice
  return (<BasicModal
    {...rest}
    onCancel={onCancel}
    footer={getFooter({status, handleSubmit, onCancel})}
  >
    {status === 'success' && (<div styleName="price-items">
      <div className="row">
        <div styleName="label">上门取货：</div>
        <div styleName="text">¥{orderPrice.pickUpFee}</div>
      </div>
      <div className="row">
        <div styleName="label">航空货运：</div>
        <div styleName="text">¥{orderPrice.airlineFee}</div>
      </div>
      <div className="row">
        <div styleName="label">机场派送：</div>
        <div styleName="text">¥{orderPrice.dropOffFee}</div>
      </div>
      <div className="row">
        <div styleName="label">订单总额：</div>
        <div styleName="text total-price">¥{orderPrice.total}</div>
      </div>
    </div>)}

    {status === 'loading' && (<div styleName="price-items">
      <b>正在核算价格</b>
    </div>)}

    {status === 'error' && (<div styleName="price-items">
      <b>核算价格失败</b>
    </div>)}
  </BasicModal>)
}

export default cssModules(OrderPriceModal, styles, {allowMultiple: true});
