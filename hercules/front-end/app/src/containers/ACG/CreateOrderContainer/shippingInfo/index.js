/* eslint-disable react/prefer-stateless-function */
import React, {Component} from 'react'
import {connect} from 'react-redux'
import cssModules from 'react-css-modules'
import moment from 'moment'
import {get} from 'lodash'
import DatePicker from 'antd/lib/date-picker'
import Form from 'antd/lib/form'
import Input from 'antd/lib/input'
import {changeEstimatedDeliveryTime, changeAcgPrimaryNum, changeAgentCode} from '../actionCreators/shippingInfo'
import {validateShippingInfo} from '../utils/validators'
import AirportSelect from './airportSelect'
import styles from './shipping_info.module.scss'

const layouts = {
  formItemLayout: {
    labelCol: {
      sm: {span: 6},
    },
    wrapperCol: {
      sm: {span: 18},
    },
  },
  nonLabelFormItemLayout: {
    wrapperCol: {
      sm: {
        offset: 6,
        span: 18,
      },
    }
  }
}

const disabledHours = function () {
  return [0]
}

class ShippingInfo extends Component {
  onEstimatedDeliveryTimeChange(t) {
    if (t) {
      // can not select 00:00~01:00
      const hour = t.hour()
      if (hour >= 0 && hour < 1) {
        t.hour(1)
        t.minute(0)
        t.second(0)
        t.millisecond(0)
      }
    }
    return this.props.dispatch(changeEstimatedDeliveryTime(moment(t).format('YYYY-MM-DD HH:mm')))
  }

  onAcgPrimaryNumChange(e) {
    return this.props.dispatch(changeAcgPrimaryNum(e.target.value))
  }

  onAgentCodeChange(e) {
    return this.props.dispatch(changeAgentCode(e.target.value))
  }

  render() {
    const props = this.props
    const {isValidating, departure, arrival} = props
    const errors = isValidating ? validateShippingInfo(props) : {}
    return (
      <div className={styles.container}>
        <div className={styles.wrapper}>
          <AirportSelect
            {...props}
            {...departure}
            errors={errors}
            layouts={layouts}
            port="departure"
            label="始发港"
            placeholder="请选择始发城市"
          />
        </div>

        <i className={styles.airplane}/>

        <div className={styles.wrapper}>
          <AirportSelect
            {...props}
            {...arrival}
            errors={errors}
            layouts={layouts}
            port="arrival"
            label="到达港"
            placeholder="请选择到达城市"
          />
        </div>

        <div className={styles.wrapper}>
          <Form.Item
            {...layouts.formItemLayout}
            {...get(errors, 'estimatedDeliveryTime[0]', {})}
            label="航运时效"
            required={true}
          >
            <DatePicker
              showTime={{format: 'HH:mm', disabledHours, hideDisabledOptions: true}}
              format="YYYY-MM-DD HH:mm"
              placeholder="请选择时间结点"
              disabledDate={date => date < moment().set({hour: 0, minute: 0, second: 0, millisecond: 0})}
              style={{width: '100%'}}
              onChange={::this.onEstimatedDeliveryTimeChange}
            />
          </Form.Item>
          <div className={styles.item}>
            <Form.Item
              {...layouts.formItemLayout}
              {...get(errors, 'acgPrimaryNum[0]', {})}
              label="航空主单号"
            >
              <Input ref={item => { this.acgPrimaryNum = item }} onChange={::this.onAcgPrimaryNumChange}/>
            </Form.Item>
          </div>
          <div className={styles.item}>
            <Form.Item
              {...layouts.formItemLayout}
              {...get(errors, 'agentCode[0]', {})}
              label="代理人代码"
            >
              <Input ref={item => { this.agentCode = item }} onChange={::this.onAgentCodeChange}/>
            </Form.Item>
          </div>
        </div>
      </div>
    )
  }
}


export default connect(state => ({
  ...state.acg.shippingInfo,
  airports: state.acg.airports,
  contacts: state.contact.contacts,
  contactStatus: state.contact.status,
  enterpriseId: state.auth.enterpriseId,
}), null, null, {withRef: true})(cssModules(ShippingInfo, styles))
