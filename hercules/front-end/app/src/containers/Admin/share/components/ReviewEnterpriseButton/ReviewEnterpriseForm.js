import React, {Component} from 'react'
import {browserHistory} from 'react-router'

import httpClient from 'utils/http/index'
import message from 'antd/lib/message';
import Form from 'antd/lib/form'
import Input from 'antd/lib/input'
import Radio from 'antd/lib/radio'

const RadioGroup = Radio.Group;
const FormItem = Form.Item
const TextArea = Input.TextArea;

class ReviewEnterpriseForm extends Component {
  state = {
    enterprise: {},
    isCommentVisible: false,
  }

  handleSubmit = (e) => {
    e.preventDefault()
    this.props.form.validateFieldsAndScroll((err) => {
      if (!err) {
        const {id} = this.props.enterprise
        const {validationStatus, comment} = this.props.form.getFieldsValue()
  
        this.reviewEnterprise(id, {validationStatus, comment})
          .then(() => {
            message.success('企业审核完毕！');
            browserHistory.push('/admin/enterprise_list')
          })
          .catch(() => {
            message.error('企业审核失败！');
          })
      }
    })
  }
  
  reviewEnterprise(id, params) {
    return httpClient.post('/enterprises/' + id + '/approve', params)
  }
  
  handleChange(e) {
    this.setState({
      isCommentVisible: e.target.value === 'Unauthorized'
    })
  }
  
  closeModal() {
    this.props.onCancel()
  }
  
  render() {
    const {getFieldDecorator} = this.props.form
    const {isCommentVisible} = this.state
    const formItemLayout = {
      labelCol: { span: 3 },
      wrapperCol: { span: 21 }
    };
    
    return (
      <Form onSubmit={this.handleSubmit}>
        <FormItem {...formItemLayout} label="状态">
          {getFieldDecorator('validationStatus', {initialValue: 'Authorized'})(
            <RadioGroup onChange={::this.handleChange}>
              <Radio value="Authorized">通过</Radio>
              <Radio value="Unauthorized">不通过</Radio>
            </RadioGroup>
          )}
        </FormItem>
        {
          isCommentVisible &&
          <FormItem wrapperCol={{offset: 3}}>
            {getFieldDecorator('comment', {
              rules: [
                {required: true, message: '请输入审核意见'},
                {max: 100, message: '最多100个字符'}]
            })(
              <TextArea rows={4} placeholder="若不通过，请输入审核意见，以便企业修改与完善资质。"/>
            )}
          </FormItem>
        }
        <div className="operate">
          <button type="submit" className="button primary">确认</button>
          <button type="button" className="button" onClick={::this.closeModal}>取消</button>
        </div>
      </Form>
    )
  }
}

export default Form.create()(ReviewEnterpriseForm)