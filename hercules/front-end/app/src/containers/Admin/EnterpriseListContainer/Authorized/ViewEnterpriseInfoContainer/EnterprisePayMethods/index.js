import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from 'redux';
import Form from 'antd/lib/form';
import Checkbox from 'antd/lib/checkbox';
import Message from 'antd/lib/message';
import {orderTypesMap, payMethodsMap, formatCheckboxesData, orderBusiness, sortByPaymentMethod} from './payMethodsUtil';
import styles from './index.module.scss';
import * as action from './actions';

const FormItem = Form.Item;
const CheckboxGroup = Checkbox.Group;

class ViewEnterpriseInfo extends Component{ // eslint-disable-line
  componentDidMount() {
    this.loadEnterprisePayMethods(this.props.enterpriseId);
  }

  loadEnterprisePayMethods(enterpriseId) {
    this.props.actions.getEnterprisePayMethods(enterpriseId);
  }

  handleSubmit(e) {
    e.preventDefault();

    this.props.form.validateFields((error, value) => {
      if (!error) {
        this.props.actions.updateEnterprisePayMethods(this.props.enterpriseId, formatCheckboxesData(value, this.props.enterprisePayMethods))
          .then(() => {
            Message.success('更改支付方式成功！');
          })
          .catch(() => {
            Message.error('更改支付方式失败！');
          });
      }
    });
  }

  generatePayMethods(payMethods, getFieldDecorator) {
    const renderPayMethods = payMethods.map((payMethod) => {
      const orderType = payMethod.orderType;
      const defaultValues = [];

      const checkBoxGroupData = payMethod.payMethods
        .filter((payMethod) => payMethod.editable)
        .map((item) => {
          if (item.enabled) {
            defaultValues.push(item.name);
          }

          return {label: payMethodsMap(item.name.toLowerCase()), value: item.name, disabled: !item.editable};
        });

      return (
        <div key={`${orderType}-checkboxes`} className="order-checkbox">
          <div className="order-type">{`${orderTypesMap(orderType)}: `}</div>
          <div className="pay-methods-checkboxes">
            <FormItem>
              {getFieldDecorator(orderType, {
                initialValue: defaultValues
              })(
                <CheckboxGroup options={sortByPaymentMethod(checkBoxGroupData)}/>
              )}
            </FormItem>
          </div>
        </div>
      )
    });
    return renderPayMethods;
  }

  render() {
    const payMethods = this.props.enterprisePayMethods || [];
    const {getFieldDecorator} = this.props.form;
    const renderPayMethods = this.generatePayMethods(orderBusiness(payMethods), getFieldDecorator);

    return (
      <div className={styles['pay-methods']}>
        <Form onSubmit={::this.handleSubmit}>
          { renderPayMethods }
          <div className="save-pay-methods">
            <button type="submit" className="button primary">保存设置</button>
          </div>
        </Form>
      </div>
    )
  }
}

const mapStateToProps = (state) => {
  return {
    enterprisePayMethods: state.platformAdmin.enterprisePayMethods.enterprisePayMethods
  }
};

const mapDispatchToProps = (dispatch) => {
  return {
    actions: bindActionCreators(action, dispatch)
  }
};

export default connect(mapStateToProps, mapDispatchToProps)(Form.create()(ViewEnterpriseInfo));