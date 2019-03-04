/* eslint-disable global-require */
import React from 'react'
import { render, unmountComponentAtNode } from 'react-dom'
import { install } from 'offline-plugin/runtime'
import {AppContainer as ReactHotLoader} from 'react-hot-loader'
import 'assets/fonts/font_zck90zmlh7hf47vi.eot' // eslint-disable-line
import 'assets/fonts/font_zck90zmlh7hf47vi.svg' // eslint-disable-line
import 'assets/fonts/font_zck90zmlh7hf47vi.ttf' // eslint-disable-line
import 'assets/fonts/font_zck90zmlh7hf47vi.woff' // eslint-disable-line
import '../styles/antd.less'
import '../styles/index.scss'
import '../assets/favicon.ico'
import { getStoreInstance, history } from './store'

const isProduction = process.env.NODE_ENV === 'production'
const MOUNT_NODE = document.getElementById('app')

const renderApp = () => {
  const Root = require('./Root').default
  render(
    <ReactHotLoader>
      <Root store={getStoreInstance()} history={history}/>
    </ReactHotLoader>,
    MOUNT_NODE
  )
}


if (isProduction) {
  install()
} else {
  /* eslint-disable */
  if (module.hot) {
    module.hot.accept('./Root', () => {
      unmountComponentAtNode(MOUNT_NODE)
      renderApp()
    })
  }
  /* eslint-enable */
}

renderApp()