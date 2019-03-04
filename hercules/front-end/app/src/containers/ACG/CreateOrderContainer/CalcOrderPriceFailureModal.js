import React from 'react'
import cssModules from 'react-css-modules'
import { BasicModal } from 'components'
import styles from './index.module.scss'

const getFooter = ({onCancel}) => {
  return (<button
    type="button" className="button" styleName="button cancel"
    onClick={onCancel}>确定</button>)
}

const CalcOrderPriceFailureModal = ({onCancel, ...rest}) => {
  return (<BasicModal
    {...rest}
    onCancel={onCancel}
    footer={getFooter({onCancel})}
  >
    <div styleName="calc-price-failure">暂无合适航线，请联系客服 400-890-0505</div>
  </BasicModal>)
}

export default cssModules(CalcOrderPriceFailureModal, styles, {allowMultiple: true});
