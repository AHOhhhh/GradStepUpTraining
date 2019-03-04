import React, {Component} from 'react'
import cssModules from 'react-css-modules'
import {Input, Form, Button} from 'antd'
import {compact, includes, join, compose} from 'lodash/fp'
import code from './code.json'
import BasicModal from '../BasicModal'
import RegionSelector from '../RegionSelector'
import styles from './index.module.scss'

const VALIDATION_MESSAGES = {
  NOT_NULL_USERNAME: '联系人姓名不能为空',
  NOT_NULL_ADDRESS_DETAIL: '详细地址不能为空',
  NOT_NULL_COUNTRY: '所在国家或地区不能为空',
  NOT_NULL_DISTRICT: '所在区域不能为空',
  NOT_NULL_PHONE: '联系电话不能为空，请至少输入一项',
  NOT_NULL_CELLPHONE: '手机号码不能为空',
  NOT_NULL_EMAIL: '邮箱地址不能为空',
  NOT_NULL_POSTCODE: '邮政编码不能为空',

  ADDRESS_NOT_MATCH_REGION_LIMIT: '该地址无法提供上门取货服务',

  MAX_USERNAME: '用户名称不能大于25个字符',
  MIN_ADDRESS_DETAIL: '地址不能小于5个字符',
  MAX_ADDRESS_DETAIL: '地址不能大于100个字符',

  FORMAT_CELLPHONE: '手机号码必须为数字，且长度为11个字符',
  FORMAT_TELEPHONE: '固定电话号码必须为数字与字符, 且长度为6~20个字符',
  FORMAT_POSTCODE: '邮政编码必须为数字且最长为20位',
  FORMAT_EMAIL: '邮箱地址必须符合格式且不超过50个字符',
};

class PhoneInput extends Component {
  constructor(props) {
    super(props);
    const value = this.props.value || {};
    this.state = {
      cellphone: value.cellphone,
      telephone: value.telephone
    }
  }

  componentWillReceiveProps(nextProps) {
    if ('value' in nextProps) {
      const value = nextProps.value;
      this.setState({
        cellphone: value.cellphone,
        telephone: value.telephone
      });
    }
  }

  handleCellphoneChange(event) {
    const cellphone = event.target.value;
    this.setState({
      cellphone
    });
    this.props.onChange(Object.assign({}, this.state, {
      cellphone
    }));

  }

  handleTelephoneChange(event) {
    const telephone = event.target.value;
    this.setState({
      telephone
    });
    this.props.onChange(Object.assign({}, this.state, {
      telephone
    }));
  }

  render() {
    return (
      <div>
        <Input
          placeholder="输入手机号码" value={this.state.cellphone} onChange={::this.handleCellphoneChange}
          style={{marginBottom: '24px'}}
        />
        <Input
          placeholder="输入固定电话号码" value={this.state.telephone} onChange={::this.handleTelephoneChange}
        />
      </div>
    )
  }
}

class PopModalEdit extends Component { // eslint-disable-line
  static propTypes = {};

  constructor(props) {
    super(props);
    this.state = {
      contact: {}
    }
  }

  componentWillReceiveProps = (nextProps) => {
    if (!nextProps.visible) {
      this.handleReset();
    }
    if (nextProps.contact) {
      this.setState({
        contact: nextProps.contact
      })
    }
  };

  onSelectChange = (selectedRowKeys) => {
    this.setState({selectedRowKeys});
  };

  checkEmptyString(value) {
    if (value === '') {
      return undefined;
    }
    return value
  }

  handleSubmit = (e) => {
    e.preventDefault();
    const region = this.props.form.getFieldsValue().region
    this.props.form.setFieldsValue({
      region: Object.assign({}, region, {checkRegion: true})
    })
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {

        const contact = {
          name: this.checkEmptyString(values.name),
          address: this.checkEmptyString(values.address),
          cellphone: this.checkEmptyString(values.phone.cellphone),
          telephone: this.checkEmptyString(values.phone.telephone),
          country: this.checkEmptyString(values.region.country),
          countryAbbr: this.checkEmptyString(values.region.countryAbbr),
          province: this.checkEmptyString(values.region.province),
          provinceId: this.checkEmptyString(values.region.provinceId),
          city: this.checkEmptyString(values.region.city),
          cityId: this.checkEmptyString(values.region.cityId),
          district: this.checkEmptyString(values.region.district),
          districtId: this.checkEmptyString(values.region.districtId),
          postcode: this.checkEmptyString(values.postcode),
          email: this.checkEmptyString(values.email),
          enterpriseId: this.props.enterpriseId
        };
        this.props.handleSubmit(contact);
      }
    });
  };

  handleReset = () => {
    this.props.form.resetFields();
  };

  checkAddressIsMatchRegionLimit(regionLimit, value) {
    return compose(
      includes(regionLimit),
      join(''),
      compact,
    )([value.country, value.province, value.district, value.city])
  }

  checkRegion = (rule, value, callback) => {
    const {regionLimit} = this.props

    if (!value.country) {
      callback(VALIDATION_MESSAGES.NOT_NULL_COUNTRY);
    }

    if (value.country === '中国大陆' &&
      (!value.countryAbbr || !value.province || !value.provinceId
      || !value.city || !value.cityId || !value.district || !value.districtId) && value.checkRegion) {
      callback(VALIDATION_MESSAGES.NOT_NULL_DISTRICT);
    }

    if (regionLimit && !this.checkAddressIsMatchRegionLimit(regionLimit, value)) {
      callback(VALIDATION_MESSAGES.ADDRESS_NOT_MATCH_REGION_LIMIT);
    }

    callback();
  };

  checkPhone = (rule, value, callback) => {
    const regCellphone = /^[0-9]{11}$/;
    const regTelephone = /^[0-9、\-_（）().]{6,20}$/;

    if (!value.cellphone) {
      callback(VALIDATION_MESSAGES.NOT_NULL_CELLPHONE);
      return
    }
    if (value.cellphone && !(regCellphone.test(value.cellphone))) {
      callback(VALIDATION_MESSAGES.FORMAT_CELLPHONE);
      return
    }
    if (value.telephone && !(regTelephone.test(value.telephone))) {
      callback(VALIDATION_MESSAGES.FORMAT_TELEPHONE);
      return
    }
    callback();
  };

  checkPostcode = (rule, value, callback) => {
    const regPostcode = /^[0-9]{1,20}$/;

    if (value && !regPostcode.test(value)) {
      callback(VALIDATION_MESSAGES.FORMAT_POSTCODE);
      return
    }
    callback();
  };

  checkEmail = (rule, value, callback) => {
    const regEmail = /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;

    if (value && (!regEmail.test(value) || (value.length > 50))) {
      callback(VALIDATION_MESSAGES.FORMAT_EMAIL);
      return
    }
    callback();
  };

  render() {
    const {getFieldDecorator} = this.props.form;
    const {title, visible, onCancel, width, height, notice} = this.props;
    const contact = this.state.contact;
    const FormItem = Form.Item;
    const {TextArea} = Input;
    const formItemLayout = {
      labelCol: {
        xs: {span: 24},
        sm: {span: 6},
      },
      wrapperCol: {
        xs: {span: 24},
        sm: {span: 14},
      },
    };

    const validationRules = {
      name: [
        {
          required: true,
          message: VALIDATION_MESSAGES.NOT_NULL_USERNAME,
        },
        {
          max: 25,
          message: VALIDATION_MESSAGES.MAX_USERNAME
        }
      ],
      phone: [
        {
          required: true,
        },
        {
          validator: this.checkPhone
        }
      ],
      region: [
        {
          required: true,
        },
        {
          validator: this.checkRegion
        }
      ],
      address: [
        {
          required: true,
          message: VALIDATION_MESSAGES.NOT_NULL_ADDRESS_DETAIL,
        },
        {
          min: 5,
          message: VALIDATION_MESSAGES.MIN_ADDRESS_DETAIL
        },
        {
          max: 100,
          message: VALIDATION_MESSAGES.MAX_ADDRESS_DETAIL
        }
      ],
      postcode: [
        {
          required: true,
          message: VALIDATION_MESSAGES.NOT_NULL_POSTCODE,
        },
        {
          validator: this.checkPostcode
        }
      ],
      email: [
        {
          required: true,
          message: VALIDATION_MESSAGES.NOT_NULL_EMAIL,
        },
        {
          validator: this.checkEmail
        }
      ]
    };

    return (
      <div>
        <BasicModal
          title={title}
          visible={visible}
          handleSubmit={() => {
          }}
          onCancel={onCancel}
          width={width}
          height={height}
          footer={[
            <Button
              key="submitBtn"
              type="submit"
              className="button primary submit"
              onClick={this.handleSubmit}>确定</Button>,
            <Button
              key="cancelBtn"
              type="button"
              className="button"
              onClick={onCancel}>取消</Button>
          ]}
        >
          <div className={styles.modalList}>
            <div className="notice">{notice && notice}</div>
            <Form onSubmit={this.handleSubmit}>
              <FormItem
                {...formItemLayout}
                label="联系人姓名"
              >
                {getFieldDecorator('name', {
                  initialValue: contact.name,
                  rules: validationRules.name,
                })(
                  <Input placeholder="输入联系人姓名"/>
                )}
              </FormItem>

              <FormItem
                {...formItemLayout}
                label="联系方式"
              >
                {getFieldDecorator('phone', {
                  initialValue: {
                    cellphone: contact.cellphone,
                    telephone: contact.telephone
                  },
                  rules: validationRules.phone,
                })(
                  <PhoneInput/>
                )}
              </FormItem>

              <FormItem
                {...formItemLayout}
                label="所在区域">
                {getFieldDecorator('region', {
                  initialValue: {
                    ...contact,
                  },
                  rules: validationRules.region,
                })(
                  <RegionSelector
                    options={code}
                  />
                )}
              </FormItem>

              <FormItem
                className="textarea"
                {...formItemLayout}
                label="详细地址"
              >
                {getFieldDecorator('address', {
                  initialValue: contact.address,
                  rules: validationRules.address,
                })(
                  <TextArea rows={3} placeholder="输入地址信息" className="textarea"/>
                )}
              </FormItem>

              <FormItem
                {...formItemLayout}
                label="邮政编码"
              >
                {getFieldDecorator('postcode', {
                  initialValue: contact.postcode,
                  rules: validationRules.postcode,
                })(
                  <Input placeholder="输入邮政编码"/>
                )}
              </FormItem>

              <FormItem
                {...formItemLayout}
                label="邮箱地址"
              >
                {getFieldDecorator('email', {
                  initialValue: contact.email,
                  rules: validationRules.email,
                })(
                  <Input placeholder="输入邮箱地址"/>
                )}
              </FormItem>
            </Form>
          </div>
        </BasicModal>
      </div>
    );
  }
}

export default Form.create()(cssModules(PopModalEdit, styles));
