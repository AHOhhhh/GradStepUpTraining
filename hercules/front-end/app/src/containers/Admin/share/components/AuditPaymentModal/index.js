import React, { Component, PropTypes } from 'react'
import { Modal, Radio, Input, Spin, Button, message } from 'antd'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { noop, cloneDeep } from 'lodash'

import { formatPrice } from 'utils'

import * as actions from 'actions'

import styles from './index.module.scss'

const RadioGroup = Radio.Group
const TextArea = Input.TextArea

const initialState = {
  orderInfo: {},
  isConfirmed: null,
  comment: '',
  errors: {}
}

@connect(
  state => ({
    auditPayment: state.auditPayment
  }),
  dispatch => ({
    actions: bindActionCreators(actions, dispatch)
  }), null, { withRef: true }
)
export default class AuditPaymentModal extends Component {

  static propTypes = {
    onCloseCallback: PropTypes.func
  }

  state = {
    submitting: false,
    visible: false,
    ...initialState
  }

  showModal(orderInfo) {
    this.setState({
      visible: true,
      disabled: true,
      orderInfo,
    })

    this.props.actions.getLatestTransaction(orderInfo.id)
      .then(() => this.setState({ disabled: false }))
      .catch(() => message.error('获取订单失败，请稍后再试'))
  }

  closeModal() {
    this.setState({
      visible: false,
      ...initialState
    })
  }

  isDataValid() {
    const { isConfirmed, comment } = this.state

    const fields = [
      {
        key: 'isConfirmed',
        message: '请选择是否通过',
        valid: typeof isConfirmed === 'boolean'
      },
      {
        key: 'comment',
        message: '不通过时审核意见为必填项',
        valid: typeof isConfirmed === 'boolean' ? (isConfirmed || comment.trim()) : true
      },
      {
        key: 'comment',
        message: '审核意见字数不能超过100字',
        valid: !comment || comment.length < 100
      }
    ]

    const errors = {}

    fields.forEach(field => {
      if (!field.valid) {
        errors[field.key] = field.message
      }
    })

    this.setState({ errors })
    return Object.keys(errors).length
  }

  collectionData() {
    const { isConfirmed, comment = '' } = this.state
    const { transactionId } = this.props.auditPayment.fund
    return { isConfirmed, comment, transactionId }
  }

  onSubmit = () => {
    const { orderInfo } = this.state
    const { getOrderLatestStatus, submitAuditPaymentResult } = this.props.actions
    const { afterSubmit = noop } = this.props

    if (this.isDataValid()) {
      return
    }

    this.setState({ submitting: true })
    getOrderLatestStatus(orderInfo.id)
      .then(res => {
        if (res.data.status !== 'OfflinePaidAwaitingConfirm') {
          this.setState({ submitting: false })
          return message.error('该订单不在待支付审核状态，无法审核，请刷新列表。')
        }

        return submitAuditPaymentResult(this.collectionData())
          .then(() => {
            setTimeout(() => {
              this.setState({ submitting: false })
              message.success('支付审核完毕！')
              this.closeModal()
              afterSubmit()
            }, 2000)
          })
      }).catch(() => {
        this.setState({ submitting: false })
        message.error('提交失败，请稍后再试')
      })
  }

  renderDetail() {
    const fund = cloneDeep(this.props.auditPayment.fund)

    const list = [{
      key: 'bankTransactionNumber',
      label: '支付流水号：',
      value: fund.bankTransactionNumber || '--'
    }, {
      key: 'payAmount',
      label: '应付金额：',
      value: fund.payAmount ? formatPrice(fund.payAmount) : '--'
    }, {
      key: 'bankTransactionTime',
      label: '支付日期：',
      value: fund.bankTransactionTime || '--'
    }, {
      key: 'bankTransactionComment',
      label: '备注信息：',
      value: fund.bankTransactionComment || '--'
    }]

    return (
      <div className="detail">
        {
          list.map(item => (
            <div className="box" key={item.key}>
              <div className="label">{item.label}</div>
              <div className="value">{item.value}</div>
            </div>
          ))
        }
      </div>
    )
  }

  handleChange(key, evt) {
    const { errors } = this.state
    this.setState({
      [key]: evt.target.value,
      errors: { ...errors, [key]: '' }
    }, () => this.isDataValid())
  }

  renderForm() {
    const { isConfirmed, errors, comment } = this.state
    const placeholder = isConfirmed ?
      '您可以在此记录一些信息，仅供管理人员后台查看。' :
      '若不通过，请输入审核意见，以便企业修改与完善资质。'

    return (
      <div className="box form-box">
        <div className="label">审核意见：</div>
        <div className="value">
          <RadioGroup
            value={isConfirmed}
            onChange={this.handleChange.bind(this, 'isConfirmed')}
          >
            <Radio className="customRadio" value={true}>通过</Radio>
            <Radio className="customRadio" value={false}>不通过</Radio>
          </RadioGroup>
          <div className="error">{errors.isConfirmed}</div>

          <TextArea
            className="customTextArea"
            placeholder={placeholder}
            onChange={this.handleChange.bind(this, 'comment')}
            value={comment}
            onBlur={::this.isDataValid}
          />
          <div className="error">{errors.comment}</div>
        </div>
      </div>
    )
  }

  render() {
    const { visible, disabled, submitting } = this.state
    const { loading } = this.props.auditPayment

    return (
      <Modal
        width={640}
        title="审核线下付款"
        visible={visible}
        onCancel={::this.closeModal}
        maskClosable={false}
        footer={null}
        wrapClassName="edit-order-form"
      >
        <Spin spinning={loading}>
          <section className={styles.auditPaymentContainer}>
            {this.renderDetail()}
            {this.renderForm()}

            <div className="row operation">
              <Button
                type="primary"
                htmlType="button"
                className="button primary"
                loading={submitting}
                onClick={::this.onSubmit}
                disabled={disabled}>
                确定
              </Button>
              <Button type="button" className="button cancel" onClick={::this.closeModal}>
                取消
              </Button>
            </div>
          </section>
        </Spin>
      </Modal>
    )
  }
}
