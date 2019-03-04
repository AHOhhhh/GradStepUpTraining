import React from 'react'
import {Link} from 'react-router'
import {Col, Row} from 'antd'
import cssModules from 'react-css-modules'

import Attachments from './Attachments'
import styles from './index.module.scss'

const renderEnterpriseInfo = (enterpriseInfo) => {
  const {
    name,
    uniformSocialCreditCode,
    businessLicenseNumber,
    taxPayerNumber,
    organizationCode,
    registrationAddress,
    artificialPersonName,
    artificialPersonContact
  } = enterpriseInfo
  
  return (
    <div>
      <Row styleName="row">
        <Col span={4}><span styleName="label">企业名称：</span></Col>
        <Col span={7}><span styleName="content">{name}</span></Col>
        <Col offset={1} span={4}><span styleName="label">统一社会信用代码：</span></Col>
        <Col span={8}><span styleName="content">{uniformSocialCreditCode}</span></Col>
      </Row>
      <Row styleName="row">
        <Col span={4}><span styleName="label">营业执照编号：</span></Col>
        <Col span={7}><span styleName="content">{businessLicenseNumber}</span></Col>
        <Col offset={1} span={4}><span styleName="label">纳税人识别号：</span></Col>
        <Col span={8}><span styleName="content">{taxPayerNumber}</span></Col>
      </Row>
      <Row styleName="row">
        <Col span={4}><span styleName="label">组织机构代码：</span></Col>
        <Col span={7}><span styleName="content">{organizationCode}</span></Col>
        <Col offset={1} span={4}><span styleName="label">公司注册地址：</span></Col>
        <Col span={8}><span styleName="content">{registrationAddress}</span></Col>
      </Row>
      <Row styleName="row">
        <Col span={4}><span styleName="label">法人代表：</span></Col>
        <Col span={7}><span styleName="content">{artificialPersonName}</span></Col>
        <Col offset={1} span={4}><span styleName="label">法人联系电话：</span></Col>
        <Col span={8}><span styleName="content">{artificialPersonContact}</span></Col>
      </Row>
    </div>
  )
}

const renderInfoUpdateMessage = (user) => (
  <div styleName="without-verify-statement">暂未上传企业资质信息，
    <Link styleName="upload-link" to={{pathname: '/signup_enterprise', state: {isEdit: !!user.enterpriseId}}}>
      去上传资料&gt;
    </Link>
  </div>
)


const EnterpriseQualificationInfo = ({enterpriseInfo, showAttachment, user}) => {
  return (
    <div styleName="enterprise-qualification-info">
      <div styleName="qualification-title">企业资质信息</div>
      {
        enterpriseInfo.validationStatus ?
          <div styleName="qualification-content">
            {renderEnterpriseInfo(enterpriseInfo)}
            {showAttachment && <Attachments enterpriseInfo={enterpriseInfo}/>}
          </div>
          : renderInfoUpdateMessage(user)
      }
    </div>
  )
}

export default cssModules(EnterpriseQualificationInfo, styles, {allowMultiple: true})