import React from 'react'
import cssModules from 'react-css-modules'
import styles from './index.module.scss'
import {official} from '../../config/url'

const host = official.home

const ProductDropdown = () => (
  <div styleName="product-menu">
    <div styleName="group">
      <div styleName="title"><span styleName="icon icon0"/>航空货运</div>
      <ul styleName="css-reset">
        <li styleName="item"><a href={`${host}/product/0`}>门到门服务</a></li>
        <li styleName="item"><a href={`${host}/product/1`}>国内空运</a></li>
        <li styleName="item"><a href={`${host}/product/03`}>国际空运</a></li>
      </ul>
    </div>
    <div styleName="group">
      <div styleName="title"><span styleName="icon icon2"/>关务服务</div>
      <ul styleName={styles.cssReset}>
        <li styleName="item"><a href={`${host}/product/5`}>报关报检</a></li>
      </ul>
    </div>
    <div styleName="group">
      <div styleName="title"><span styleName="icon icon1"/>供应链金融</div>
      <ul styleName={styles.cssReset}>
        <li styleName="item"><a href={`${host}/product/2`}>应收账款融资</a></li>
        <li styleName="item"><a href={`${host}/product/3`}>采购订单融资</a></li>
        <li styleName="item"><a href={`${host}/product/4`}>综合融资方案</a></li>
      </ul>
    </div>
    <div styleName="group">
      <div styleName="title"><span styleName="icon icon3"/>仓储管理</div>
      <ul styleName={styles.cssReset}>
        <li styleName="item"><a href={`${host}/product/7`}>仓储管理SAAS服务</a></li>
      </ul>
    </div>
    {/* <div styleName="group"> */}
    {/* <div styleName="title"><span styleName="icon icon4"/>大道物流</div> */}
    {/* <ul styleName={styles.cssReset}> */}
    {/* <li styleName="item"><a href={`${host}/product/10`}>大道物流货主平台</a></li> */}
    {/* <li styleName="item"><a href={`${host}/product/11`}>大道物流车主平台</a></li> */}
    {/* <li styleName="item"><a href={`${host}/product/12`}>大道物流货损险</a></li> */}
    {/* </ul> */}
    {/* </div> */}
  </div>
)

export default cssModules(ProductDropdown, styles, {allowMultiple: true})

