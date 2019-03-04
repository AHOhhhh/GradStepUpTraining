import React, {Component} from 'react'
import Button from 'antd/lib/button'
import cssModules from 'react-css-modules'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import * as action from 'actions'

import styles from './index.module.scss';

@connect(
  state => ({
    user: state.auth.user,
    token: state.auth.token
  }),
  dispatch => ({actions: bindActionCreators(action, dispatch)})
)
class AuthorizeContainer extends Component {
  authorize() {
    const {query} = this.props.location
    this.props.actions.authorizeApplication(query, this.props.token)
  }

  componentDidMount() {
    const {query} = this.props.location
    this.props.actions.authorizeApplication(query, this.props.token)
  }

  render() {
    return (
      <div className={styles['authorize-container']}>
        <h1>授权应用</h1>
        <p>
          应用想要访问你在HLA的用户信息，请授权
        </p>
        <Button
          disabled
          type="primary"
          htmlType="button"
          className="button primary"
          onClick={::this.authorize}>
          授权
        </Button>
      </div>
    )
  }
}

export default cssModules(AuthorizeContainer, styles)
