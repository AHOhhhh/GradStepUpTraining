import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import {connect} from 'react-redux'
import {includes} from 'lodash'
import {formatPrice, formatDateTime} from 'utils'

import {Table, message} from 'antd'

import * as actions from './action'
import ReFundModal from './RefundModal'
import styles from './index.module.scss'

const payMethodMap = {
  OFFLINE: '线下',
  DEFERMENT: '后付',
  ONLINE: '线上',
}

const payTypeMap = {
  Expense: '支付',
  Income: '退款'
}

// const enableRefund = ['Paid', 'Closed', 'OrderTracking', 'Cancelled']
const enableRefund = []

@connect(
  state => ({auth: state.auth}),
  () => ({})
)
class FundInfo extends Component {
  state = {
    fund: [],
    loading: false,
    visible: false,
  }

  order = {}

  getOrderLatestTransaction(id) {
    this.setState({loading: true})
    actions.getSuccessFundInfo(id)
      .then(res => {
        const fund = res.data ? res.data : []
        this.setState({fund, loading: false})
      })
      .catch((err) => {
        if (err.status === 404) {
          this.setState({loading: false, fund: []})
        } else {
          message.error('查询资金详情失败')
        }
        this.setState({loading: false})
      })
  }

  componentDidMount() {
    const {id} = this.props.order || {}
    const {user} = this.props.auth
    const visible = user && ['PlatformAdmin', 'PlatformAdmin'].includes(user.role)
    this.setState({visible})

    if (id && visible) {
      this.getOrderLatestTransaction(id)
    }
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.order.id !== this.order.id && this.state.visible) {
      this.getOrderLatestTransaction(nextProps.order.id)
    }
  }

  openRefundModal() {
    this.RefundModal.open()
  }

  isDisplayOrderRefund() {
    return includes(enableRefund, this.props.order.status)
  }

  render() {
    if (!this.state.visible) {
      return null
    }

    const columns = [{
      title: '时间',
      dataIndex: 'paidTime',
      key: 'paidTime',
      render: (paidTime) =>
        <span>{paidTime ? formatDateTime(paidTime) : '--'}</span>
    }, {
      title: '交易类型',
      dataIndex: 'type',
      key: 'type',
      render: (type) =>
        <span>{type ? payTypeMap[type] : '--'}</span>
    }, {
      title: '交易方式',
      dataIndex: 'payMethod',
      key: 'payMethod',
      render: (payMethod) =>
        <span>{payMethod ? payMethodMap[payMethod] : '--'}</span>
    }, {
      title: '备注信息',
      dataIndex: 'comment',
      key: 'comment',
      render: (comment) =>
        <span>{comment || '--'}</span>
    }, {
      title: '金额',
      dataIndex: 'payAmount',
      key: 'payAmount',
      render: (payAmount, record) =>
        <span>{payAmount ? (record.type === 'Expense' ? '+ ' : '- ') + formatPrice(payAmount) : '--'}</span>
    }];
    return (
      <div className={styles.fundInfo}>
        <div className="title">
          <h2>资金信息</h2>
          {this.isDisplayOrderRefund() && <a onClick={::this.openRefundModal}>订单退款</a>}
        </div>
        <Table dataSource={this.state.fund} columns={columns} pagination={false}/>
        <ReFundModal
          getOrderTransaction={::this.getOrderLatestTransaction}
          order={this.props.order}
          ref={(ref) => {
            this.RefundModal = ref
          }}
        />
      </div>
    )
  }
}

export default cssModules(FundInfo, styles, {allowMultiple: true});
