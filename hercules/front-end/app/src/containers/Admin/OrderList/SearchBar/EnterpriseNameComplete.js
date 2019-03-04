import React from 'react'
import {Select, AutoComplete} from 'antd'
import { connect } from 'react-redux'
import {bindActionCreators} from 'redux'
import * as action from '../actions';

function renderOption(enterprise) {
  return (
    <Select.Option key={enterprise.id} text={enterprise.name}>
      {enterprise.name}
    </Select.Option>
  );
}

const EnterpriseNameComplete = ({validEnterprises, onSelect, actions, value, changeCompleteValue}) => {
  return (
    <AutoComplete
      dataSource={validEnterprises.map(renderOption)}
      style={{width: 200, marginTop: 2}}
      onSelect={onSelect}
      placeholder="客户名称"
      onSearch={(value) => {
        if (value.length < 2) {
          onSelect('')
          return
        }
        actions.getValidEnterprise(value)
      }}
      onChange={changeCompleteValue}
      allowClear={true}
      value={value}
      />
  )
}

const mapStateToProps = (state) => ({
  validEnterprises: state.platformAdmin.validEnterprises
})

const mapDispatchToProps = dispatch => ({actions: bindActionCreators(action, dispatch)})

export default connect(mapStateToProps, mapDispatchToProps)(EnterpriseNameComplete)