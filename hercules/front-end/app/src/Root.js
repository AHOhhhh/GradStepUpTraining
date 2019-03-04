import React from 'react'
import { Router } from 'react-router'
import { Provider } from 'react-redux'
import { routes } from './routes'

export default (props) =>
  <Provider store={props.store}>
    <Router history={props.history} routes={routes} />
  </Provider>
