import React, { Component } from 'react'
import { connect } from 'react-redux';
import { get } from 'lodash';
import cssModules from 'react-css-modules';
import { getCompletedSCFOrders } from 'actions'
import Card from './Card'
import loanIcon from '../assets/loan.svg'
import styles from './index.module.scss'

const pageSize = 9


class SCFOrderList extends Component {
  state = {
    size: 0,
    isLoading: true,
    hasMore: true
  }

  hasMore(totalElements, size) {
    return totalElements > size
  }

  componentDidMount() {
    // this.getOrders()
  }

  loadMore = () => {
    const { size } = this.state
    this.setState({
      isLoading: true,
      size: size + pageSize
    }, () => {
      this.getOrders(this.state.size)
    })
  }

  getOrders = (currentSize = 3) => {
    const { enterpriseId, getCompletedSCFOrders } = this.props
    const pageInfo = {
      size: currentSize,
      enterpriseId,
      page: 0
    }
    getCompletedSCFOrders(pageInfo).then((res) => {
      this.setState({
        size: res.data.content.length,
        isLoading: false
      })
    })
  }

  renderLoadMore = () => {
    const totalElements = get(this.props, 'scfOrders.totalElements', 0)
    const { size } = this.state
    return (
      <div styleName="load-more">
        {this.hasMore(totalElements, size)
          ? <div onClick={this.loadMore}>查看更多</div>
          : <p>已无更多</p>}
      </div>)
  }

  render() {
    const orders = get(this.props, 'scfOrders.content', null)
    const isDisplay = orders && orders.some(order => {
      return order.scfFinancierOffer
    })

    if (isDisplay) {
      return (
        <div styleName="list-container">
          <h2>
            <img src={loanIcon} />
            <span>当前融资</span>
          </h2>
          {orders.map((item, index) => <Card key={index} data={item} />)}
          {this.renderLoadMore()}
        </div>)
    }
    return null
  }
}

const mapStateToProps = state => ({
  enterpriseId: state.auth.enterpriseId,
  scfOrders: state.admin.scfOrders
})

export default connect(
  mapStateToProps, { getCompletedSCFOrders }
)(cssModules(SCFOrderList, styles, { allowMultiple: true }))
