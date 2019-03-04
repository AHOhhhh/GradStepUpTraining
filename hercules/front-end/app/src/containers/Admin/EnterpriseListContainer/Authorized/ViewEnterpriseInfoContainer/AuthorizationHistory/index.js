import React, {Component} from 'react'
// import cssModules from 'react-css-modules'
import {Table} from 'antd'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import _ from 'lodash'
import moment from 'moment'

import * as action from './actions'

// import styles from './index.module.scss'

class AuthorizationHistory extends Component { // eslint-disable-line

  componentDidMount() {
    this.props.actions.getEnterpriseHistories(this.props.enterpriseId)
      .then((resp) => {
        if (resp) {
          const ids = resp.data.map(item => item.updatedBy)
          _.uniq(ids).forEach((id) => {
            this.props.actions.getAdminName(id)
          })
        }
      })
  }

  render() {

    const columns = [{
      title: '审核日期',
      dataIndex: 'updatedAt',
      key: 'authDate',
    }, {
      title: '审核人',
      dataIndex: 'updatedBy',
      key: 'authPerson',
    }, {
      title: '通过与否',
      dataIndex: 'isPassed',
      key: 'isPassed',
    }, {
      title: '审核意见',
      dataIndex: 'comment',
      key: 'authSuggestion',
    }];

    const dataSource = this.props.enterpriseHistories.map((history) => {
      const {validationStatus, updatedAt, ...info} = history
      const date = moment(updatedAt).format('YYYY-MM-DD HH:mm:ss')
      const isPassed = validationStatus === 'Authorized' ? '是' : '否'

      return {...info, isPassed, updatedAt: date}
    })

    return (
      <Table
        columns={columns}
        dataSource={dataSource}
        pagination={false}
        rowKey={(record) => record.updatedAt}
      />
    )
  }
}

const mapStateToProps = (state) => {
  return {
    enterpriseHistories: state.platformAdmin.enterpriseHistories
  }
}

const mapDispatchToProps = (dispatch) => {
  return {
    actions: bindActionCreators(action, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(AuthorizationHistory)