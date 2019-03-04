import React, {Component} from 'react'
import {Table} from 'antd'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import moment from 'moment'
import result from 'lodash/result'
import * as action from './actions'

class AdminOperationHistory extends Component { // eslint-disable-line

  state = {
    pageSize: 10,
    pageNum: 0
  }

  componentDidMount() {
    this.loadAdminOperationHistories()
  }

  loadAdminOperationHistories() {
    this.props.actions.getAdminOperationHistories(this.props.enterpriseId, this.state.pageSize, this.state.pageNum)
  }

  generatePaginationConfig() {
    const total = result(this.props.adminOperationHistories, 'totalElements', 0)
    return {
      total,
      showSizeChanger: true,
      onChange: (pageNum) => {
        this.setState({
          pageNum: pageNum - 1
        }, () => {
          this.loadAdminOperationHistories()
        })
      },
      onShowSizeChange: (pageNum, pageSize) => {
        this.setState({
          pageNum: pageNum - 1,
          pageSize
        }, () => {
          this.loadAdminOperationHistories()
        })
      }
    }
  }

  render() {
    const columns = [{
      title: '编号',
      dataIndex: 'index',
      key: 'index',
      render: index => <span>{index < 10 ? ('0' + index) : index}</span>
    }, {
      title: '操作员',
      dataIndex: 'operatorName',
      key: 'operatorName'
    }, {
      title: '操作时间',
      dataIndex: 'createdAt',
      key: 'createdAt'
    }, {
      title: '操作记录',
      dataIndex: 'typeDescription',
      key: 'typeDescription'
    }]

    const operationHistoryList = this.props.adminOperationHistories.content || []

    const dataSource = operationHistoryList.map(history => {
      history.createdAt = moment(history.createdAt).format('YYYY-MM-DD HH:mm:ss')
      return history
    })

    const pagination = this.generatePaginationConfig()

    return this.props.adminOperationHistories && (
      <Table
        columns={columns}
        dataSource={dataSource}
        pagination={pagination}
        rowKey={(record) => record.index}
      />
    )
  }
}

const mapStateToProps = (state) => {
  return {
    adminOperationHistories: state.platformAdmin.adminOperationHistory
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    actions: bindActionCreators(action, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(AdminOperationHistory)