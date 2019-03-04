import React, {Component} from 'react'
import {browserHistory} from 'react-router'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import {cookie} from 'utils'
import * as action from 'actions'
import {getStoreInstance} from '../../../store'

@connect(
  dispatch => ({actions: bindActionCreators(action, dispatch)})
)
class LogoutContainer extends Component {
  componentWillMount() {
    const store = getStoreInstance()
    store.getState().auth.user = null
    cookie.remove('TOKEN')
    window.localStorage.clear()
    browserHistory.push('/login')
  }

  render() {
    return <div/>
  }
}

export default LogoutContainer