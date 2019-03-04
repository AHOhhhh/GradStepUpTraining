import React, {Component, PropTypes} from 'react'
import {connect} from 'react-redux'
import cssModules from 'react-css-modules'
import {bindActionCreators} from 'redux'
import _ from 'lodash'
import message from 'antd/lib/message'
import {USER_ROLE} from 'constants'
import Form from 'antd/lib/form'
import Input from 'antd/lib/input'
import * as actions from 'actions'
import styles from './index.module.scss'

const FormItem = Form.Item

class EditUserForm extends Component {
  static propTypes = {
    form: PropTypes.object,
    onSubmitCallback: PropTypes.func,
    user: PropTypes.object.isRequired
  }

  constructor(props) { // eslint-disable-line
    super(props)
    this.state = {
      confirmDirty: false
    }
  }

  componentDidMount() {
    this.props.form.setFieldsValue({
      ...this.props.user
    })
  }

  componentWillReceiveProps = (nextProps) => {
    if (!_.isEqual(nextProps.user, this.props.user) || !this.props.form.getFieldValue('username')) {
      this.props.form.setFieldsValue({
        ...nextProps.user
      })
    }
  }

  handleConfirmBlur = (e) => {
    const value = e.target.value
    this.setState({confirmDirty: this.state.confirmDirty || !!value})
  }

  checkConfirm = (rule, value, callback) => {
    const form = this.props.form
    if (value && this.state.confirmDirty) {
      form.validateFields(['confirm'], {force: true})
    }
    callback()
  }

  handleSubmit = (e) => {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        const userId = this.props.user.id
        const isAdmin = this.props.user.role === USER_ROLE.enterpriseAdmin
        this.props.actions.updateUser(userId, isAdmin, Object.assign({}, this.props.user, values))
          .then(() => {
            message.success('编辑成功！');
            this.props.onSubmitCallback()
          })
      }
    })
  }

  closeModal() {
    this.props.form.resetFields()
    this.props.onSubmitCallback()
  }

  render() {
    const {getFieldDecorator} = this.props.form
    return (
      <div className="signup" styleName="editUserForm">
        <Form styleName="form-box" onSubmit={this.handleSubmit}>
          <div styleName="row">
            <div styleName="title"><i styleName="user-icon">*</i>用户名称：</div>
            <FormItem hasFeedback styleName="form-item">
              {getFieldDecorator('username', {
                rules: [{
                  min: 6, message: '用户名称不能小于6位'
                }, {
                  max: 18, message: '用户名称不能大于18位'
                }, {
                  pattern: /^[a-zA-Z0-9]*$/, message: '用户名称只能包含字母和数字'
                }, {
                  required: true, message: '请输入用户名称'
                }],
              })(
                <Input styleName="disabled" disabled/>
              )}
            </FormItem>
          </div>

          <div styleName="row">
            <div styleName="title"><span styleName="required">姓名：</span></div>
            <FormItem hasFeedback styleName="form-item">
              {getFieldDecorator('fullname', {
                rules: [{
                  required: true, message: '请输入姓名'
                }, {
                  max: 30, message: '姓名不能大于30位'
                }]
              })(
                <Input placeholder="联系人的真实姓名"/>
              )}
            </FormItem>
          </div>

          <div styleName="row">
            <div styleName="title">联系方式：</div>
            <div styleName="inline-box">
              <div styleName="row">
                <div styleName="title"><span styleName="required">手机：</span></div>
                <FormItem hasFeedback styleName="form-item">
                  {getFieldDecorator('cellphone', {
                    rules: [{
                      required: true, message: '请输入手机号码'
                    }, {
                      max: 11, message: '手机不能大于11位'
                    }, {
                      min: 11, message: '手机不能小于11位'
                    }]
                  })(
                    <Input/>
                  )}
                </FormItem>
              </div>
              <div styleName="row">
                <div styleName="no-margin">
                  <div styleName="title">固定电话：</div>
                  <FormItem hasFeedback styleName="form-item">
                    {getFieldDecorator('telephone', {
                      rules: [{
                        max: 20, message: '固定电话不能大于20位'
                      }]
                    })(
                      <Input/>
                    )}
                  </FormItem>
                </div>
              </div>
            </div>
          </div>
          <div styleName="row">
            <div styleName="title"><span styleName="required">邮箱：</span></div>
            <FormItem hasFeedback styleName="form-item">
              {getFieldDecorator('email', {
                rules: [{
                  type: 'email', message: '邮箱格式不正确'
                }, {
                  max: 50, message: '邮箱不能大于50位'
                }, {
                  required: true, message: '请输入邮箱'
                }]
              })(
                <Input placeholder="联系人的邮箱"/>
              )}
            </FormItem>
          </div>
          <div className="operate">
            <button type="submit" className="button primary">确认</button>
            <button type="button" className="button" onClick={::this.closeModal}>取消</button>
          </div>
        </Form>
      </div>
    )
  }
}

export default Form.create()(connect(
  null,
  dispatch => ({actions: bindActionCreators(actions, dispatch)})
)(cssModules(EditUserForm, styles)))