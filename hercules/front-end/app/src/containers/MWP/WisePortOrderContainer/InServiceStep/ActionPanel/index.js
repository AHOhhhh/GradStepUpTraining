import React from 'react'
import cssModules from 'react-css-modules'

import Card from 'antd/lib/card'

import SSOUploadButton from 'components/SSOUploadButton'

import styles from './index.module.scss'

const ActionPanel = () => (
  <div styleName="action-panel">
    <Card style={{ width: 270 }}>
      <p styleName="hints">服务商正在为您服务，当前缺少部分资料</p>
      <SSOUploadButton businessLine="mwp" buttonClass="primary button-large">去上传</SSOUploadButton>
    </Card>
  </div>
)

export default cssModules(ActionPanel, styles)