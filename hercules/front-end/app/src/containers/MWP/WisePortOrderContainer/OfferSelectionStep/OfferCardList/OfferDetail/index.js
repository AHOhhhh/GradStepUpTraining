import React, {Component} from 'react'
import cssModule from 'react-css-modules'
import currencyFormatter from 'currency-formatter'

import styles from './index.module.scss'

class OfferDetail extends Component {
  constructor(props) {
    super(props)
    this.state = {
      isDisplayAll: false
    }
  }

  changeDisplayAll() {
    this.setState({
      isDisplayAll: true
    })
  }

  render() {

    const {isDisplayAll} = this.state;
    const items = this.props.items || []

    const total = items.reduce((p, n) => {
      return p + (n.estimation || n.exactPrice || 0)
    }, 0)

    return (
      <div styleName="offer-detail">
        {
          items.map((item, i) => {
            const index = i
            const price = item.estimation || item.exactPrice || '--'
            return (
              <div key={index}>
                <div style={i === 0 || isDisplayAll ? {} : {display: 'none'}}>
                  <div>
                    {item.itemName} {price === '--' ? '--' : currencyFormatter.format(price, {code: 'CNY'})}
                  </div>
                  <div styleName="other-expenses">
                    {item.description ? `（ ${item.description} ）` : ''}
                  </div>
                </div>
              </div>
            )
          })
        }
        <div
          styleName="offer-detail__button"
          style={items.length <= 1 || isDisplayAll ? {display: 'none'} : {}}>
          <span onClick={this.changeDisplayAll.bind(this)}>
            ...全部明细
          </span>
        </div>
        <div styleName="offer-detail__span">
          <span styleName="offer-detail__total">总价：</span>
          <span styleName="offer-detail__price">{currencyFormatter.format(total, {code: 'CNY'})}</span>
        </div>
        <div styleName="other-expenses">
          *不含费率和实报实销项目*
        </div>
      </div>
    )
  }
}

export default cssModule(OfferDetail, styles)