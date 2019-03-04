import React, {Component, PropTypes} from 'react'
import _ from 'lodash'

import PromptModal from 'components/PromptModal'
import OfferCard from './OfferCard'

class OfferCardList extends Component {

  static propTypes = {
    onChange: PropTypes.func.isRequired,
  }

  state = {
    checkedOffers: [],
    visible: false
  }

  handleItemChange(offer, checked) {
    let checkedOffers = [...this.state.checkedOffers]

    if (checked && !this.canOfferAppend(offer)) {
      this.setState({
        visible: true
      })
      return
    }

    if (checked) {
      checkedOffers.push(offer)
      checkedOffers = _.uniqBy(checkedOffers, 'offerId')
    } else {
      _.remove(checkedOffers, {
        offerId: offer.offerId
      })
    }

    this.setState({checkedOffers}, () => {
      this.props.onChange(checkedOffers)
    })
  }

  canOfferAppend(offer) {
    const hasDifferences = _.difference(offer.services, this.props.order.services).length > 0

    const hasSameServices = this.state.checkedOffers.some((item) => {
      const itemServices = item.services || []
      const newServices = offer.services || []
      return _.intersection(itemServices, newServices).length > 0
    })

    return !hasDifferences && !hasSameServices
  }

  isChecked(offer) {
    return this.state.checkedOffers.some((item) => {
      return item.offerId === offer.offerId
    })
  }


  closeModal() {
    this.setState({
      visible: false
    })
  }

  render() {
    const {offers, preview} = this.props
    return (
      <div>
        <ul>
          {
            offers.map((offer) => {
              const cardBorderStyle = this.state.checkedOffers.find(c => c === offer) ? 'card-border_focus' : 'card-border'

              return (
                <li key={offer.offerId}>
                  <OfferCard
                    preview={preview}
                    cardBorderStyle={cardBorderStyle}
                    offer={offer}
                    checked={::this.isChecked(offer)}
                    onChange={(checked) => {
                      ::this.handleItemChange(offer, checked)
                    }}/>
                </li>
              )
            })
          }
        </ul>
        <PromptModal
          visible={this.state.visible} onClose={::this.closeModal}
          content="您选择的服务商报价项目与您的需求不符 , 请确认后再提交。"/>
      </div>
    )
  }
}


export default OfferCardList