import React, {Component} from 'react'
import {bindActionCreators} from 'redux'
import {Link} from 'react-router'
import {connect} from 'react-redux'
import cssModules from 'react-css-modules'
import Form from 'antd/lib/form'
import Input from 'antd/lib/input'
import message from 'antd/lib/message'
import {ImageUploader, SignStepper} from 'components'
import * as action from 'actions'
import styles from './index.module.scss'
import certificateForUniformSocialCreditImage from './images/demo1.jpg'
import taxPayerNumberImage from './images/demo2.jpg'
import businessLicenseImage from './images/demo3.jpg'
import organizationCertificateImage from './images/demo4.jpg'
import {REG_ZH_EN_NUM_EXTEND_1, REG_ZH_EN_NUM_EXTEND_2, REG_PHONE_EXTEND_3} from './constants';

class SignUpEnterpriseContainer extends Component {
  constructor() {
    super()
    this.state = {
      showFileRequiredError: false,
      showBusinessLicenseImage: true,
      showTaxPayerNumberImage: true,
      showOrganizationCertificateImage: true
    }
  }

  componentDidMount() {
    if (this.props.location.state && this.props.location.state.isEdit) {
      const enterpriseId = this.props.auth.enterpriseId || this.props.auth.user.enterpriseId
      if (enterpriseId) {
        this.props.actions.getEnterpriseInfo(enterpriseId)
      }
    } else {
      this.setState({
        showBusinessLicenseImage: false,
        showTaxPayerNumberImage: false,
        showOrganizationCertificateImage: false
      })
    }
  }

  componentWillReceiveProps = (nextProps) => {
    const enterpriseInfo = nextProps.enterpriseInfo
    if (enterpriseInfo.id && enterpriseInfo.id !== this.props.enterpriseInfo.id) {
      this.props.form.setFieldsValue({
        ...enterpriseInfo,
      })
      const {certificateForBusinessLicense, certificateForTaxRegistration, certificateForOrganization} = this.props.form.getFieldsValue()
      this.setState({
        showBusinessLicenseImage: !!certificateForBusinessLicense,
        showTaxPayerNumberImage: !!certificateForTaxRegistration,
        showOrganizationCertificateImage: !!certificateForOrganization
      })
    }
  }

  handleSubmit = (e) => {
    e.preventDefault()
    const isEdit = this.props.location.state && this.props.location.state.isEdit
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        values.businessLicenseNumber = values.businessLicenseNumber === '' ? undefined : values.businessLicenseNumber
        values.taxPayerNumber = values.taxPayerNumber === '' ? undefined : values.taxPayerNumber
        values.organizationCode = values.organizationCode === '' ? undefined : values.organizationCode

        const data = this.trimValues(Object.assign({}, this.props.enterpriseInfo, values));
        this.props.actions.signupEnterprise(data, isEdit)
          .then(() => {
            message.success('提交成功')
          })
          .catch(() => {
            message.error('提交失败')
          })
      } else if (err.certificateForUniformSocialCreditCode) {
        this.setState({showFileRequiredError: true})
      }
    })
  }

  trimValues(obj) {
    Object.keys(obj).forEach(key => {
      obj[key] = typeof obj[key] === 'string' ? obj[key].trim() : obj[key];
    });
    return obj
  }

  showUploader(filed) {
    if (filed === 'businessLicense') {
      this.setState({showBusinessLicenseImage: true})
    } else if (filed === 'taxPayerNumber') {
      this.setState({showTaxPayerNumberImage: true})
    } else {
      this.setState({showOrganizationCertificateImage: true})
    }
  }

  hideUploader(filed) {
    const {getFieldValue} = this.props.form
    if (filed === 'businessLicense') {
      this.setState({showBusinessLicenseImage: false})
      this.deleteFile(getFieldValue('businessLicense'))
      this.refs.businessLicense.reset()
    } else if (filed === 'taxPayerNumber') {
      this.setState({showTaxPayerNumberImage: false})
      this.deleteFile(getFieldValue('taxRegistrationCertificate'))
      this.refs.taxRegistrationCertificate.reset()
    } else {
      this.setState({showOrganizationCertificateImage: false})
      this.deleteFile(getFieldValue('organizationCertificate'))
      this.refs.organizationCertificate.reset()
    }
  }

  deleteFile(fileName) {
    if (fileName) {
      this.props.actions.deleteFile(fileName)
    }
  }

  render() {
    const FormItem = Form.Item
    const {getFieldDecorator} = this.props.form
    const isEdit = this.props.location.state && this.props.location.state.isEdit
    return (
      <div className="container signup-form">
        <div styleName="enterprise-signup">
          {!isEdit && (<div styleName="step-box">
            <SignStepper current={1}>
              <SignStepper.Step title="Step1" description="填写账户基本信息"/>
              <SignStepper.Step title="Step2" description="填写公司资质信息"/>
              <SignStepper.Step title="Step3" description="提交成功，等待审核"/>
            </SignStepper>
          </div>)
          }
          <Form styleName="form-box" onSubmit={this.handleSubmit}>
            <div styleName="box-title">
              {isEdit ? '编辑' : '填写'}公司资质基本信息
            </div>
            <hr/>
            <h3 styleName="section-title">公司资质</h3>
            <div styleName="row">
              <div styleName="title" className="label-required">企业名称:</div>
              <FormItem hasFeedback>
                {getFieldDecorator('name', {
                  rules: [{
                    required: true, message: '请输入企业名称'
                  }, {
                    max: 100, message: '企业名称不能大于100位'
                  }],
                })(
                  <Input placeholder="填写您企业的名称"/>
                )}
              </FormItem>
            </div>

            <div styleName="row">
              <div styleName="title" className="label-required">统一社会信用代码:</div>
              <FormItem hasFeedback>
                {getFieldDecorator('uniformSocialCreditCode', {
                  rules: [{
                    pattern: /^([0-9A-Za-z]){18}$/, message: '统一社会信用代码由18位字母或数字构成',
                  }, {
                    required: true, message: '请输入统一社会信用代码'
                  }]
                })(
                  <Input/>
                )}
              </FormItem>
            </div>

            <div styleName="row">
              <div styleName="title"/>
              <FormItem>
                <ImageUploader
                  form={this.props.form}
                  name="certificateForUniformSocialCreditCode"
                  demoImg={certificateForUniformSocialCreditImage}
                  isRequired={true}
                  callback={() => this.setState({showFileRequiredError: false})}
                />
                {this.state.showFileRequiredError && <span styleName="error-message">请上传统一社会信用代码证照</span>}
              </FormItem>
            </div>

            <div styleName="row">
              <div styleName="title">营业执照编号:</div>
              <FormItem hasFeedback>
                {getFieldDecorator('businessLicenseNumber', {
                  rules: [
                    {
                      pattern: /[0-9]{15}/, message: '营业执照编号由15位数字构成'
                    },
                    {
                      max: 15, message: '营业执照编号不能大于15位'
                    }
                  ]
                })(
                  <Input/>
                )}
              </FormItem>
              <span
                onClick={this.showUploader.bind(this, 'businessLicense')}
                className="anticon"
                styleName="add-image-icon"
              >
              添加证照附件
            </span>
            </div>

            {this.state.showBusinessLicenseImage &&
            (<div styleName="row">
              <div styleName="title"/>
              <FormItem>
                <ImageUploader
                  ref="businessLicense"
                  form={this.props.form}
                  name="certificateForBusinessLicense"
                  demoImg={taxPayerNumberImage}/>
                <span
                  className="anticon"
                  styleName="close-uploader-icon"
                  onClick={this.hideUploader.bind(this, 'businessLicense')}/>
              </FormItem>
            </div>)}

            <div styleName="row">
              <div styleName="title">纳税人识别号:</div>
              <FormItem hasFeedback>
                {getFieldDecorator('taxPayerNumber', {
                  rules: [
                    {
                      max: 20, message: '纳税人识别号不能大于20位'
                    }, {
                      pattern: /[0-9a-zA-Z]{20}/, message: '纳税人识别由20位字母和数字构成'
                    }
                  ]
                })(
                  <Input/>
                )}
              </FormItem>
              <span
                onClick={this.showUploader.bind(this, 'taxPayerNumber')}
                className="anticon"
                styleName="add-image-icon"
              >添加证照附件
            </span>
            </div>
            {this.state.showTaxPayerNumberImage &&
            (<div styleName="row">
              <div styleName="title"/>
              <FormItem>
                <ImageUploader
                  ref="taxRegistrationCertificate"
                  form={this.props.form}
                  name="certificateForTaxRegistration"
                  demoImg={businessLicenseImage}/>
                <span className="anticon" styleName="close-uploader-icon" onClick={this.hideUploader.bind(this, 'taxPayerNumber')}/>
              </FormItem>
            </div>)}

            <div styleName="row">
              <div styleName="title">组织机构代码:</div>
              <FormItem hasFeedback>
                {getFieldDecorator('organizationCode', {
                  rules: [
                    {
                      pattern: /^[a-zA-Z0-9]{8}-[a-zA-Z0-9]$/, message: '组织机构代码包含8位字母和数字加"-"和一位数字或字母构成'
                    }
                  ],
                })(
                  <Input/>
                )}
              </FormItem>
              <span
                onClick={this.showUploader.bind(this, 'organizationCertificate')}
                className="anticon" styleName="add-image-icon">添加证照附件</span>
            </div>
            {this.state.showOrganizationCertificateImage &&
            (<div styleName="row">
              <div styleName="title"/>
              <FormItem>
                <ImageUploader
                  ref="organizationCertificate"
                  form={this.props.form}
                  name="certificateForOrganization"
                  demoImg={organizationCertificateImage}
                />
                <span
                  className="anticon"
                  styleName="close-uploader-icon"
                  onClick={this.hideUploader.bind(this, 'organizationCertificate')}
                />
              </FormItem>
            </div>)}

            <div styleName="row">
              <div styleName="title" className="label-required">公司注册地址:</div>
              <FormItem hasFeedback>
                {getFieldDecorator('registrationAddress', {
                  rules: [{
                    required: true, message: '请输入公司注册地址'
                  }, {
                    max: 100, message: '公司注册地址不能大于100位'
                  }, {
                    pattern: REG_ZH_EN_NUM_EXTEND_2, message: '公司注册地址不得包含特殊字符'
                  }],
                })(
                  <Input placeholder="请输入公司注册地址"/>
                )}
              </FormItem>
            </div>
            <hr styleName="divider-thin"/>
            <h3 styleName="section-title">法人信息</h3>
            <div styleName="row">
              <div styleName="title" className="label-required">法人代表姓名:</div>
              <FormItem hasFeedback>
                {getFieldDecorator('artificialPersonName', {
                  rules: [{
                    required: true, message: '请输入法人代表姓名'
                  }, {
                    max: 30, message: '法人代表姓名不能大于30位'
                  }, {
                    pattern: REG_ZH_EN_NUM_EXTEND_1, message: '法人代表不能包含特殊字符'
                  }]
                })(
                  <Input placeholder="请输入法人代表的真实姓名"/>
                )}
              </FormItem>
            </div>
            <div styleName="row">
              <div styleName="title" className="label-required">法人联系电话:</div>
              <FormItem hasFeedback>
                {getFieldDecorator('artificialPersonContact', {
                  rules: [{
                    required: true, message: '请输入法人联系电话'
                  }, {
                    max: 20, message: '法人联系电话不能超过20位'
                  }, {
                    pattern: REG_PHONE_EXTEND_3, message: '法人联系电话仅允许包括数字和特殊字符（）、-_'
                  }]
                })(
                  <Input placeholder="请输入法人代表的联系电话"/>
                )}
              </FormItem>
            </div>
            <div styleName="operate">
              {isEdit ? (<button type="submit" styleName="button" className="button primary">确定</button>) :
                (<button type="submit" styleName="button" className="button primary">下一步</button>)}
              <Link to="/enterprise_info">
                <button type="button" styleName="button" className="button">取消</button>
              </Link>
            </div>
          </Form>
        </div>
      </div>
    )
  }
}

export default connect(
  state => ({
    auth: state.auth,
    enterpriseInfo: state.enterpriseInfo.info
  }),
  dispatch => ({actions: bindActionCreators(action, dispatch)})
)(Form.create()(cssModules(SignUpEnterpriseContainer, styles, {allowMultiple: true})))
