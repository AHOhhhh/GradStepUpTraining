import React from 'react'
import cssModules from 'react-css-modules'
import Progress from 'antd/lib/progress'
import brokenLineImg from '../../../shared/assets/broken-line.svg'
import styles from './index.module.scss'

const UpdateProfileStep = ({order: {status, supplementaryFiles, uploadFileUrl}}) => (
  <div>
    <div styleName="container">
      <h2>申请所需材料</h2>
      <div styleName="progress-container">
        <p styleName="tip">当前材料完整度</p>
        <img styleName="broken-line" src={brokenLineImg} alt="broken-line"/>
        <Progress type="circle" width={200} percent={supplementaryFiles ? supplementaryFiles.completenessPercentage : 0} strokeWidth={4}/>
        {uploadFileUrl && <a styleName="link" disabled={supplementaryFiles && supplementaryFiles.rejected} href={uploadFileUrl} target="_blank" rel="noopener noreferrer">去补充</a>}
      </div>
    </div>
    <div styleName="container">
      <h2>材料反馈信息</h2>
      {supplementaryFiles && status === 'OrderRejected' && (<div styleName="rejected">
        <i styleName="error-icon" className="anticon anticon-exclamation-circle"/>
        <p>您补充的材料不符合准入要求，订单已中止，可选择重新下单</p>
      </div>)}
      {supplementaryFiles && !supplementaryFiles.rejected && supplementaryFiles.feedback && (<div styleName="feedback">
        {supplementaryFiles.feedback}
      </div>)}
      {(!supplementaryFiles || (!supplementaryFiles.rejected && !supplementaryFiles.feedback)) && (<div styleName="no-feedback">
        暂无反馈，请尽快补充材料
      </div>)}
    </div>
  </div>
)

export default cssModules(UpdateProfileStep, styles, {allowMultiple: true})
