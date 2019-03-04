import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import moment from 'moment'
import {connect} from 'react-redux'
import {Section} from 'components'
import {isEmpty, get} from 'lodash'
import Form from 'antd/lib/form'
import Input from 'antd/lib/input'
import DatePicker from 'antd/lib/date-picker'
import Modal from 'antd/lib/modal'
import Button from 'antd/lib/button'
import Radio from 'antd/lib/radio'
import Row from 'antd/lib/row'
import Col from 'antd/lib/col'
import message from 'antd/lib/message'
import {createSCFOrder, getEnterpriseInfo, getEnterpriseSCFQualification} from 'actions'
import {getUTCTime} from 'utils'
import styles from './index.module.scss'
import Contact from '../../shared/Contact/index'

const FormItem = Form.Item
const RadioGroup = Radio.Group
const {TextArea} = Input

class CreateSCFOrderContainer extends Component {
  constructor(props) {
    super(props)
    this.state = {
      isSelectedContact: true,
      enterpriseModalStyle: {
        width: '520px',
        height: '206px'
      }
    }
  }

  componentDidMount() {
    this.props.getEnterpriseInfo(this.props.enterpriseId)
    this.props.getEnterpriseSCFQualification(this.props.enterpriseId)
  }

  getEnterpriseSCFQualification() {
    this.props.getEnterpriseSCFQualification(this.props.enterpriseId).then(
      (data) => {
        if (data.qualifiedStatus === 0) {
          message.success('认证成功！')
        }
      }
    )
  }

  handleSubmit(e) {
    e.preventDefault()
    if (!isEmpty(this.props.contact)) {
      const contact = this.props.contact
      this.props.form.validateFieldsAndScroll((err, values) => {
        if (!err) {
          const {user, enterpriseId} = this.props

          this.props.createSCFOrder(
            Object.assign({},
              values,
              {userId: user.id},
              {userRole: user.role},
              {contacts: [contact]},
              {currency: 'CNY'},
              {enterpriseId},
              {dueDate: getUTCTime(values.dueDate)}
            )
          )
        }
      })
    } else {
      this.setState({isSelectedContact: false})
      message.error('请添加联系人')
    }
  }

  goBack() {
    history.back()
  }

  ssoQualification() {
    const {orderQualification} = this.props
    if (orderQualification.applyForQualificationUrl) {
      window.open(orderQualification.applyForQualificationUrl, '_blank')
    }
  }

  disabledDate = current => current && moment(current.valueOf()).isBefore(moment(), 'day')

  renderTaxForm() {
    const formItemLayout = {
      labelCol: {
        xs: {span: 9},
        sm: {span: 9}
      },
      wrapperCol: {
        xs: {span: 15},
        sm: {span: 15}
      }
    }
    const {getFieldDecorator} = this.props.form
    return (
      <div>

        <FormItem
          {...formItemLayout}
          label="交易对手名称"
        >
          {getFieldDecorator('counterPartyName', {
            rules: [
              {
                max: 100, message: '最多100个字符'
              },
              {
                required: true, message: '交易对手名称不能为空'
              }]
          })(
            <Input placeholder="请输入交易对手名称, 字符100字以内"/>
          )}
        </FormItem>

        <FormItem
          {...formItemLayout}
          label="账款规模"
        >
          {getFieldDecorator('credit', {
            rules: [
              {
                max: 9, message: '最多输入9位'
              },
              {
                required: true, message: '请输入账款规模金额'
              }, {
                pattern: /^[+]?([0-9]+(?:[\.][0-9]*)?|\.[0-9]+)$/, message: '请输入正数' // eslint-disable-line
              }]
          })(
            <Input type="number"/>
          )}
          <span styleName="input-unit">元</span>
        </FormItem>

        <FormItem
          {...formItemLayout}
          label="账款账期"
        >
          {getFieldDecorator('dueDate', {
            rules: [{
              required: true, message: '请选择账款账期'
            }]
          })(
            <DatePicker
              popupStyle={{width: '340px'}}
              disabledDate={this.disabledDate}
            />
          )}
        </FormItem>

        <FormItem
          {...formItemLayout}
          label="需求描述"
        >
          {getFieldDecorator('description', {
            rules: [
              {
                required: true, message: '请对您的需求进行简单的描述, 字数100字符以内'
              }, {
                max: 100, message: '最多输入100个字符'
              }]
          })(
            <TextArea rows={4}/>
          )}
        </FormItem>

        <FormItem
          {...formItemLayout}
          label="意向额度"
        >
          {getFieldDecorator('requireAmount', {
            rules: [
              {
                required: true, message: '请输入意向额度'
              },
              {
                max: 9, message: '最多输入9位'
              }, {
                pattern: /^([1-9]\d*|0)(\.\d{1,2})?$/, message: '请输入小数点后两位的正数'
              }]
          })(
            <Input type="number" step=".01" styleName="input"/>
          )}
          <span styleName="input-unit">元</span>
        </FormItem>

        <FormItem
          {...formItemLayout}
          label="意向利率"
        >
          {getFieldDecorator('expectRate', {
            rules: [
              {
                required: true, message: '请输入意向利率'
              },
              {
                pattern: /^(0|[1-9]\d{0,2})(\.\d{1,2})?$/, message: '请输入小数点后两位百分比'
              }]
          })(
            <Input type="text" placeholder="0.00" styleName="input"/>
          )}
          <span styleName="input-unit">%</span>
        </FormItem>

      </div>
    )
  }

  renderEnterpriseInfo() {
    const formItemLayout = {
      labelCol: {
        xs: {span: 5},
        sm: {span: 5}
      },
      wrapperCol: {
        xs: {span: 19},
        sm: {span: 19}
      }
    }
    const {getFieldDecorator} = this.props.form
    const exposeEnterpriseInfoOptions = [
      {label: '是', value: true},
      {label: '否', value: false}
    ]

    const {
      name, businessLicenseNumber, organizationCode, taxPayerNumber,
      uniformSocialCreditCode, registrationAddress,
      artificialPersonName, artificialPersonContact
    } = this.props.enterpriseInfo

    const leftContent = [
      {label: '企业名称:', value: name},
      {label: '营业执照编号:', value: businessLicenseNumber},
      {label: '组织机构代码:', value: organizationCode},
      {label: '法人代表:', value: artificialPersonName}
    ]
    const rightContent = [
      {label: '统一社会代码:', value: uniformSocialCreditCode},
      {label: '纳税人识别号:', value: taxPayerNumber},
      {label: '公司注册地址:', value: registrationAddress},
      {label: '法人联系电话:', value: artificialPersonContact}
    ]

    return (
      <div>
        <Row>
          <Col span={12} styleName="left-content">
            {leftContent.map((item, index) => {
              return (<div key={`left_${index}`}>
                <span styleName="label">{item.label}</span>
                <span styleName="value">{item.value}</span>
              </div>)
            })}
          </Col>
          <Col span={12}>
            {rightContent.map((item, index) => {
              return (<div key={`right_${index}`}>
                <span styleName="label">{item.label}</span>
                <span styleName="value">{item.value}</span>
              </div>)
            })}
          </Col>
        </Row>

        <hr/>

        <FormItem
          {...formItemLayout}
          label="将企业信息披露给第三方"
        >
          {getFieldDecorator('canPublic', {initialValue: true},
            {
              rules: [{
                required: true, message: '请选择是否将企业信息披露给第三方'
              }]
            })(<RadioGroup options={exposeEnterpriseInfoOptions}/>
          )}
        </FormItem>
      </div>
    )
  }

  handleCancel() {
    const isFromLandingPage = get(this.props.location.state, 'isFromLandingPage', false)
    if (isFromLandingPage) {
      return this.props.router.goBack()
    }

    return window.close()
  }

  changeModalStyle(action) {
    const map = {
      up: {
        width: '520px',
        height: '206px'
      },
      down: {
        width: '517px',
        height: '201px'
      }
    }

    this.setState({
      enterpriseModalStyle: map[action]
    })
  }

  render() {
    return (
      <div styleName="create-order">
        <div styleName="title">填写订单信息</div>

        <div>
          <Form className="form-box" onSubmit={::this.handleSubmit}>

            <Section title="融资需求描述">
              {this.renderTaxForm()}
            </Section>

            <Section title="企业信息">
              {this.renderEnterpriseInfo()}
            </Section>

            <Section>
              <Contact/>
            </Section>

            <div className="operate" styleName="operate">
              <button type="button" className="button" styleName="button" onClick={::this.goBack}>取消</button>
              <button type="submit" className="button primary" styleName="button primary">提交订单</button>
            </div>

          </Form>
        </div>
        <Modal
          onCancel={::this.handleCancel}
          style={{top: 300}}
          visible={this.props.orderQualification.qualifiedStatus && (this.props.orderQualification.qualifiedStatus !== '0')}
          width={this.state.enterpriseModalStyle.width}
          height={this.state.enterpriseModalStyle.height}
          footer={null}>
          <div styleName="auth-modal">
            <i styleName="error-icon" className="anticon anticon-exclamation-circle"/>
            <div styleName="tip-text">您未通过企业信息认证，请前往业务系统进行认证</div>
            <div styleName="button-group">
              <Button
                className="button"
                onMouseDown={this.changeModalStyle.bind(this, 'down')}
                onMouseUp={this.changeModalStyle.bind(this, 'up')}
                onClick={::this.getEnterpriseSCFQualification}>认证完成</Button>
              <Button className="button primary" onClick={::this.ssoQualification}>去认证</Button>
            </div>
          </div>
        </Modal>
      </div>
    )
  }
}

export default connect(
  state => ({
    user: state.auth.user,
    contact: state.contact.contact,
    enterpriseId: state.auth.enterpriseId,
    enterpriseInfo: state.enterpriseInfo.info,
    orderQualification: state.scf.orderQualification
  }),
  {createSCFOrder, getEnterpriseInfo, getEnterpriseSCFQualification}
)(Form.create()(cssModules(CreateSCFOrderContainer, styles, {allowMultiple: true})))
