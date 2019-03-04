import React, {Component, PropTypes} from 'react';
import Form from 'antd/lib/form';
import Input from 'antd/lib/input';
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import * as actions from 'actions'
import {DatePicker, Table} from 'antd';
import {orderBy, forIn} from 'lodash';
import moment from 'moment';
import {serviceTypeMap} from 'constants/order';
import EditableCell from './editableCell';
import styles from './index.module.scss';
import {
  toData,
  checkInteger,
  checkPositiveNumber,
  checkPrice,
  checkNumberAndLetter,
  checkNonNegativeNumber
} from './dataFormatter';

const FormItem = Form.Item;
const {TextArea} = Input;
const wrongCell = 'wrong-cell';

@connect(
  () => {
    return {}
  },
  dispatch => ({
    actions: bindActionCreators(actions, dispatch)
  })
)

class EditOrderForm extends Component { //eslint-disable-line

  static propTypes = {
    chargingRules: PropTypes.array
  };

  constructor(props) { // eslint-disable-line
    super(props);
    this.chargingRulesColumns = [
      {
        title: '最低单量',
        dataIndex: 'quantityFrom',
        key: 'quantityFrom',
        render: (text, record) => (this.renderColumns(text, record, 'quantityFrom'))
      },
      {
        title: '最高单量',
        dataIndex: 'quantityTo',
        key: 'quantityTo',
        render: (text, record) => {
          text = isNaN(text) ? '' : text
          return (this.renderColumns(text, record, 'quantityTo'))
        }
      },
      {
        title: '单价（元）',
        dataIndex: 'unitPrice',
        key: 'unitPrice',
        render: (text, record) => (this.renderColumns(text, record, 'unitPrice'))
      },
      {
        title: '操作',
        dataIndex: 'operation',
        key: 'operation',
        render: (text, record) => (this.renderDeleteChargingRule(record.key))
      }
    ];
    this.originalChargingRule = [{key: 0, quantityFrom: 0, quantityTo: 0, unitPrice: 0, editable: false, timeoutId: null}];
    const chargingRules = (this.props.orderInfo.chargingRules && this.props.orderInfo.chargingRules.length > 0) || this.originalChargingRule;
    chargingRules.forEach((rule, index) => {
      rule.key = index
    });
    this.state = {chargingRules};
  }

  renderDeleteChargingRule(key) {
    return (this.state.chargingRules.length > 1 ?
      <span className="delete" onClick={() => this.deleteChargingRule(key)}/> : null);
  }

  renderColumns(text, record, column) {
    return (
      <EditableCell
        editable={record.editable}
        value={text}
        onChange={value => this.handleCellChange(value, record.key, column)}
        onClicked={() => this.handleCellClick(record)}
        onBlur={() => this.cellOnBlur()}
      />
    );
  }

  cellOnBlur() {
    const chargingRules = this.state.chargingRules;
    chargingRules.forEach((rule, index) => {
      rule.timeoutId = setTimeout(() => {
        rule.editable = false
        rule.timeoutId = null
        this.forceUpdate()
      }, 500)
      forIn(rule, (value, key) => {
        if (value === '' && index !== (chargingRules.length - 1)) {
          rule[key] = 0
        }
      })
    });
    this.setState({chargingRules});
  }

  handleCellClick(record) {
    const key = record.key;
    const chargingRules = [...this.state.chargingRules];
    const target = chargingRules.filter(item => key === item.key)[0];
    if (target) {
      if (target.timeoutId) {
        clearTimeout(target.timeoutId)
        target.timeoutId = null
      }
      chargingRules.forEach((data) => {
        data.editable = (data.key === key);
      });
      this.setState({chargingRules});
    }
  }

  handleCellChange(value, key, column) {
    const chargingRules = [...this.state.chargingRules];
    const target = chargingRules.filter(item => key === item.key)[0];
    if (target) {
      chargingRules.forEach((data) => {
        if (data.key === key && this.billingValuesValidate(value)) {
          value = (column === 'unitPrice' ? value : this.convertBillingValue(value))
          data[column] = value;
        }
      });
      this.setState({chargingRules});
    }
  }

  billingValuesValidate(value) {
    return checkNonNegativeNumber(value) || value === ''
  }

  convertBillingValue(value) {
    return value === '' ? '' : parseInt(value)
  }

  handleSubmit(e) {
    e.preventDefault();
    this.props.form.setFields({chargingRules: {errors: null}});
    const chargingRuleRows = document.getElementsByClassName('charging-rule-row');
    Array.prototype.forEach.call(chargingRuleRows, (ruleRow) => {
      Array.prototype.forEach.call(ruleRow.getElementsByTagName('td'), td => {
        td.className = '';
      });
    });
    this.props.form.validateFields((error, value) => {
      value.chargingRules = this.state.chargingRules;
      if (!error) {
        this.props.actions.platformAdminUpdateWMSOrder(this.props.orderInfo.id, toData(value))
          .then(() => {
            this.props.onSubmitCallback();
            this.setState({chargingRules: [{key: 0, quantityFrom: 0, quantityTo: 0, unitPrice: 0, editable: false}]});
          })
          .catch((err) => {
            throw err;
          });
      }
    });
  }

  closeModal() {
    const chargingRuleRows = document.getElementsByClassName('charging-rule-row');
    Array.prototype.forEach.call(chargingRuleRows, (ruleRow) => {
      Array.prototype.forEach.call(ruleRow.getElementsByTagName('td'), td => {
        td.className = '';
      });
    });
    this.props.form.resetFields();
    this.setState({chargingRules: this.originalChargingRule});
    this.props.onCancelCallback();
  }

  handleMaxPrice(rule, value, callback) {
    const {getFieldValue} = this.props.form;
    if (value) {
      if (!checkInteger(value)) {
        callback('最高价为数字');
      } else if (value.length > 8) {
        callback('价格不能超过八位');
      } else if (parseFloat(value) <= parseFloat(getFieldValue('minPrice'))) {
        callback('输入的最高价不能低于最低价');
      }
    }
    callback();
  }

  handleMinPrice(rule, value, callback) {
    const {getFieldValue} = this.props.form;
    if (!value) {
      callback('最低价不能为空');
    } else if (!checkInteger(value)) {
      callback('最低价为正整数');
    } else if (value.length > 8) {
      callback('价格不能超过八位');
    } else if (parseFloat(value) >= parseFloat(getFieldValue('maxPrice'))) {
      callback('输入的最低价不能高于最高价');
    }
    callback();
  }

  handleEffectiveTo(rule, value, callback) {
    const {getFieldValue, validateFields, getFieldError} = this.props.form;
    if (getFieldError('effectiveFrom')) {
      validateFields(['effectiveFrom'], {force: true})
    } else if (!value) {
      callback('截止日期不能为空');
    } else if (value < getFieldValue('effectiveFrom')) {
      callback('截止日期不能早于起始日期');
    }
    callback();
  }

  handleEffectiveFrom(rule, value, callback) {
    const {getFieldValue, validateFields, getFieldError} = this.props.form;
    if (getFieldError('effectiveTo')) {
      validateFields(['effectiveTo'], {force: true})
    } else if (!value) {
      callback('起始日期不能为空');
    } else if (value.format('YYYY-MM-DD 23:59:59') < moment(this.props.orderInfo.createdAt)) {
      callback(`起始日期不能早于: ${moment(this.props.orderInfo.createdAt)}`);
    } else if (value > getFieldValue('effectiveTo')) {
      callback('起始日期不能晚于截止日期');
    }
    callback();
  }

  handleApprovedPrice(rule, value, callback) {
    if (value) {
      const valueNumber = value.replace(/,/g, '');
      const {getFieldValue} = this.props.form;
      const minPrice = getFieldValue('minPrice');
      if (!checkInteger(valueNumber)) {
        callback('核定价格只能为数字');
      } else if (valueNumber.length > 8) {
        callback('价格不能超过八位');
      } else if (minPrice && parseInt(valueNumber) < parseInt(getFieldValue('minPrice'))) {
        callback('核定价格必须大于最低价');
      }
    }
    callback();
  }

  addRow() {
    const rules = this.state.chargingRules;
    if (rules.length <= 10) {
      rules.forEach((rule) => {
        delete rule.editable;
      });
      const previousQuantityTo = rules[rules.length - 1].quantityTo;
      const result = rules.concat(
        {
          key: rules.length,
          quantityFrom: checkInteger(previousQuantityTo) ? (parseInt(previousQuantityTo)) : 0,
          quantityTo: 0,
          unitPrice: 0,
          editable: true
        }
      );
      this.setState({chargingRules: result});
    }
  }

  deleteChargingRule(key) {
    const chargingRules = [...this.state.chargingRules];
    const results = chargingRules.filter(rule => rule.key !== key);
    results.forEach((rule, index) => {
      rule.key = index;
    });
    this.setState({chargingRules: results});
  }

  handleChargingRules(rule, value, callback) {
    const chargingRules = this.state.chargingRules;
    if (chargingRules.length <= 0) {
      callback('定价规则不能为空');
      return callback();
    } else if (chargingRules.length > 10) {
      callback('定价规则不能超过10个');
      return callback();
    }

    chargingRules.forEach((chargingRule, index) => {
      const wrongRow = document.getElementsByClassName('charging-rule-row')[index].getElementsByTagName('td');
      const isLastRule = index === (chargingRules.length - 1)
      const unitPrice = chargingRule.unitPrice;
      if (!checkInteger(chargingRule.quantityFrom)) {
        wrongRow[0].className = wrongCell;
        callback('最低单量为正整数');
      } else if (!checkInteger(chargingRule.quantityTo) && !isLastRule) {
        wrongRow[1].className = wrongCell;
        callback('最高单量为正整数');
      } else if ((chargingRule.quantityTo <= chargingRule.quantityFrom) && !isLastRule) {
        wrongRow[1].className = wrongCell;
        callback('最高单量不能低于等于最低单量');
      } else if (index === 0 && chargingRule.quantityFrom !== 0) {
        wrongRow[0].className = wrongCell;
        callback('首个计费区间的最低单量必须是0');
      } else if (isLastRule && chargingRule.quantityTo !== '') {
        wrongRow[1].className = wrongCell;
        callback('最后一个计费区间的最高单量必须为空');
      } else if (!checkPrice(unitPrice)) {
        wrongRow[2].className = wrongCell;
        callback('单价必须有两位小数');
      } else if (!checkPositiveNumber(chargingRule.unitPrice)) {
        wrongRow[2].className = wrongCell;
        callback('单价必须为正数');
      }
    });

    let chargingRulesWithIndex = chargingRules;

    chargingRulesWithIndex.forEach((chargingRule, index) => {
      chargingRule.originalIndex = index;
      chargingRule.quantityFrom = chargingRule.quantityFrom === '' ? '' : parseInt(chargingRule.quantityFrom);
      chargingRule.quantityTo = chargingRule.quantityTo === '' ? '' : parseInt(chargingRule.quantityTo);
    });
    chargingRulesWithIndex = orderBy(chargingRulesWithIndex, ['quantityFrom'], ['asc']);

    chargingRulesWithIndex.forEach((chargingRuleWithIndex, index) => {
      if (index < 1) return;
      if (chargingRuleWithIndex.quantityFrom !== (chargingRulesWithIndex[index - 1].quantityTo)) {
        const wrongQuantityFrom = document.getElementsByClassName('charging-rule-row')[chargingRuleWithIndex.originalIndex].getElementsByTagName('td');
        const wrongQuantityTo = document.getElementsByClassName('charging-rule-row')[chargingRuleWithIndex.originalIndex - 1].getElementsByTagName('td');
        wrongQuantityFrom[0].className = wrongCell;
        wrongQuantityTo[1].className = wrongCell;
        callback('数据不连续');
      }
    });

    callback();
  }

  handleEnterpriseShortName(rule, value, callback) {
    if (!value) {
      callback('企业简称不能为空');
    } else if (!checkNumberAndLetter(value)) {
      callback('企业简称只能为数字和字母')
    } else if (value.length > 15) {
      callback('企业简称不能超过十五位');
    }
    callback();
  }

  disabledDate(startValue) {
    const createAt = this.props.orderInfo.createdAt;
    const days = 1;
    if (createAt && startValue) {
      return createAt && (startValue.valueOf() < (Date.now() - (days * 24 * 60 * 60 * 1000)));
    }
    return false;
  }

  render() {
    const {getFieldDecorator} = this.props.form;
    const chargingRulesColumns = this.chargingRulesColumns;
    const orderInfo = this.props.orderInfo;
    const orderType = serviceTypeMap[orderInfo.type];
    const dataSource = this.state.chargingRules.map((rule, index) => {
      return {
        key: index,
        quantityFrom: rule.quantityFrom,
        quantityTo: rule.quantityTo,
        unitPrice: rule.unitPrice,
        editable: rule.editable
      };
    });
    const addRowStyle = this.state.chargingRules.length >= 10 ? 'add-row remove' : 'add-row';
    return (
      <div className={styles['edit-order-modal']}>
        <Form className="form-box" onSubmit={::this.handleSubmit}>
          <div className="row">
            <div className="left">
              <div>订单类型：</div>
            </div>
            <div className="right">{`WMS账号${orderType}`}</div>
          </div>
          <div className="row service-intro">
            <div className="title left">
              <div>服务内容：</div>
            </div>
            <div className="right">
              <FormItem>
                {getFieldDecorator('serviceIntro', {
                  rules: [{max: 500, message: '服务名称不能大于500位'}]
                })(<TextArea placeholder="服务内容" rows={5}/>)}
              </FormItem>
            </div>
          </div>
          <div className="row enterprise-short-name">
            <div className="left">
              <div className="left-wrapper"><i className="user-icon">*</i>企业简称：</div>
            </div>
            <div className="right">
              <FormItem>
                {getFieldDecorator('enterpriseShortName', {
                  rules: [{validator: ::this.handleEnterpriseShortName}]
                })(<Input placeholder="企业简称"/>)}
              </FormItem>
            </div>
          </div>
          <div className="row price-range">
            <div className="left">
              <div className="left-wrapper"><i className="user-icon">*</i>价格区间：</div>
            </div>
            <div className="right">
              <div className="starting">
                <FormItem>
                  {getFieldDecorator('minPrice', {
                    rules: [{validator: ::this.handleMinPrice}]
                  })(<Input placeholder="最低价"/>)}
                </FormItem>
                <div className="unit">元</div>
              </div>
              <div className="to"> -</div>
              <div className="ending">
                <FormItem>
                  {getFieldDecorator('maxPrice', {
                    rules: [{validator: ::this.handleMaxPrice}]
                  })(<Input placeholder="最高价"/>)}
                </FormItem>
                <div className="unit">元</div>
              </div>
            </div>
          </div>
          <div className="row">
            <div className="left">
              <div className="left-wrapper"><i className="user-icon">*</i>计费规则：</div>
              <div className="desc">(点击空白处进行表格编辑)</div>
            </div>
            <div className="right charging-rules-range">
              <FormItem>
                {getFieldDecorator('chargingRules', {
                  rules: [{validator: ::this.handleChargingRules}]
                })(
                  <Table
                    columns={chargingRulesColumns}
                    dataSource={dataSource}
                    rowClassName={() => 'charging-rule-row'}
                    pagination={false}
                    onChange={::this.handleChargingRules}
                    locale={{emptyText: ''}}/>
                )}
              </FormItem>
              <div className={addRowStyle} onClick={::this.addRow}>+增行</div>
            </div>
          </div>
          <div className="row effective-date">
            <div className="left">
              <div className="left-wrapper"><i className="user-icon">*</i>账号期限：</div>
            </div>
            <div className="right duration">
              <div className="starting">
                <FormItem>
                  {getFieldDecorator('effectiveFrom', {
                    initialValue: orderInfo.effectiveFrom || moment(this.props.orderInfo.createdAt),
                    rules: [{validator: ::this.handleEffectiveFrom}]
                  })(<DatePicker disabledDate={::this.disabledDate}/>)}
                </FormItem>
              </div>
              <div className="to"> -</div>
              <div className="ending">
                <FormItem>
                  {getFieldDecorator('effectiveTo', {
                    initialValue: orderInfo.effectiveTo,
                    rules: [{validator: ::this.handleEffectiveTo}]
                  })(<DatePicker disabledDate={::this.disabledDate}/>)}
                </FormItem>
              </div>
            </div>
          </div>
          <div className="row approved-price">
            <div className="left">
              <div className="left-wrapper"><i className="user-icon">*</i>核定价格：</div>
            </div>
            <div className="right">
              <FormItem>
                {getFieldDecorator('approvedPrice', {
                  rules: [{required: true, message: '核定价格不能为空'},
                    {validator: ::this.handleApprovedPrice}]
                })(<Input placeholder="核定价格"/>)}
              </FormItem>
              <div className="unit">元</div>
            </div>
          </div>
          <div className="row operation">
            <div className="left"/>
            <div className="right">
              <button type="submit" className="button primary">确定</button>
              <button type="button" className="button cancel" onClick={::this.closeModal}>取消</button>
            </div>
          </div>
        </Form>
      </div>
    )
  }
}

export default Form.create()(EditOrderForm)