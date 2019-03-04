import React, {Component} from 'react'
import {Select} from 'antd'
import cssModules from 'react-css-modules'
import styles from './index.module.scss'

const initVendors = [{
  text: '全部',
  value: 'all'
}, {
  text: 'WMS',
  value: 'wms'
}, {
  text: '大力神货运',
  value: 'yzra'
}, {
  text: 'Hercules金服',
  value: 'ssh'
}, {
  text: '口岸报关',
  value: 'mwp'
}]

const orderTypes = [{
  text: '全部',
  value: 'all',
  vendors: ['all', 'wms', 'yzra', 'ssh', 'mwp']
}, {
  text: '仓储管理',
  value: 'wms',
  vendors: ['all', 'wms']
}, {
  text: '航空货运',
  value: 'acg',
  vendors: ['all', 'yzra']
}, {
  text: '供应链金融',
  value: 'scf',
  vendors: ['all', 'ssh']
}, {
  text: '关务服务',
  value: 'mwp',
  vendors: ['all', 'mwp']
}]

const filterMap = {
  AcgOrderAccessPrivilege: {
    text: '航空货运',
    value: 'acg',
    vendors: ['yzra']
  },
  MwpOrderAccessPrivilege: {
    text: '关务服务',
    value: 'mwp',
    vendors: ['mwp']
  },
  ScfOrderAccessPrivilege: {
    text: '供应链金融',
    value: 'scf',
    vendors: ['ssh']
  },
  WmsOrderAccessPrivilege: {
    text: 'WMS',
    value: 'wms',
    vendors: ['wms']
  }
}


class OrderTypeSelect extends Component {
  state = {
    vendors: initVendors,
    vendor: undefined,
    type: undefined,
    displayOrderTypes: [],
    displayVendors: []
  }

  formatVendors(vendorValues) {
    return vendorValues.map((item) => {
      return initVendors.find((vendor) => {
        return vendor.value === item
      })
    })
  }

  handleTypeChange(type) {
    const vendorValues = orderTypes.find((item) => {
      return item.value === type
    }).vendors

    const vendors = this.formatVendors(vendorValues)
    this.setState({
      vendors
    }, () => {
      this.changeValue(type, undefined)
    })
  }

  handleVendorChange(vendor) {
    const type = this.state.type
    this.changeValue(type, vendor)
  }

  changeValue(type, vendor) {
    this.setState({
      type,
      vendor
    }, () => {
      const {onChange} = this.props
      let {type, vendor} = this.state
      if (type === 'all') {
        type = undefined
      }
      if (vendor === 'all') {
        vendor = undefined
      }
      onChange(type, vendor)
    })
  }

  clearSelect() {
    this.setState({
      vendor: undefined,
      type: undefined
    })
  }

  componentDidMount() {
    const privileges = this.props.user.privileges || []
    const operatorAdmin = privileges.find(privilege => privilege.includes('OrderAccessPrivilege'))
    const displayOrderTypes = operatorAdmin ? [filterMap[operatorAdmin]] : orderTypes
    const displayVendors = operatorAdmin ? this.formatVendors(filterMap[operatorAdmin].vendors) : this.state.vendors

    this.setState({
      displayOrderTypes,
      displayVendors
    })
  }

  render() {
    const orderTypeOptions = this.state.displayOrderTypes.map(orderType => <Select.Option
      disabled={orderType.disabled === true}
      value={orderType.value}
      key={orderType.value}>{orderType.text}</Select.Option>)
    const vendorOptions = this.state.displayVendors.map(vendor => <Select.Option
      value={vendor.value}
      key={vendor.value}>{vendor.text}</Select.Option>)

    return (
      <div styleName="fix-gap">
        <Select value={this.state.type} styleName="type-select" placeholder="产品名称" onChange={::this.handleTypeChange}>
          {orderTypeOptions}
        </Select>
        <Select
          value={this.state.vendor} styleName="vendor-select" placeholder="服务商名称"
          onChange={::this.handleVendorChange}>
          {vendorOptions}
        </Select>
      </div>
    )
  }
}

export default cssModules(OrderTypeSelect, styles, {allowMultiple: true})