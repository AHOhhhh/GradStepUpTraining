import currencyFormatter from 'currency-formatter'

import {map, compact, pick, values} from 'lodash'
import React from 'react'
import moment from 'moment'
import {Row, Col} from 'antd'

import cssModules from 'react-css-modules';
import regionCode from 'components/RegionSelector/regionCode.json'

import {LABELS_MAP} from './constants'

import styles from './index.module.scss';
import {serviceTypeMap} from '../../constants'
import GoodList from '../../../../shared/GoodList/index'


export const formatContact = (contact) => {
  if (!contact) return '---'

  const countryItem = regionCode.find((item) => {
    return item.code === contact.countryCode
  }) || {country: ''}

  const address = countryItem.country + values(pick(contact, 'province', 'city', 'district', 'address')).join('');
  return (
    <div>
      <p>{contact.name}</p>
      <p>{address}</p>
      <p>{compact([contact.mobile, contact.phone]).join('/')}</p>
    </div>
  )
}

export const renderSection = (label, text) => {
  return (<Row key={label} className={styles.item}>
    <Col span={8} className={styles.label}><p>{label}：</p></Col>
    <Col span={16} className={styles.secondary}><p>{text}</p></Col>
  </Row>)
}

const renderSections = (requirement) => {
  return map(requirement, (value, key) => {
    return renderSection(LABELS_MAP[key], value)
  })
}

const servicesName = (services) => {
  return map(services || [], (service) => serviceTypeMap[service].text).join('|')
}

const formatDate = (timestamp) => {
  return moment(timestamp).format('YYYY-MM-DD')
}

const convertToRequirement = (detail) => {
  return {
    services: servicesName(detail.services),
    port: detail.portName,
    superviseType: detail.supervisionName,
    travelType: detail.transportName,
    dueTo: formatDate(detail.orderValidationTime),
    budget: detail.budgetAmount ? currencyFormatter.format(detail.budgetAmount, {code: detail.currency}) : '--'
  }
}

const renderPanelTitle = (id, preview) => {
  if (preview) {
    return null
  }
  return (
    <div className={styles.orderTitle}>
      <h3 className={styles.title}>
        <Row>
          <Col span={8} className={styles.label}>订单号：</Col>
          <Col span={16} className={styles.secondary}>{id}</Col>
        </Row>
      </h3>
      <hr/>
    </div>
  )
}

const OrderDetailPanel = (props) => (
  <div className={styles.container}>
    {renderPanelTitle(props.detail.id, props.preview)}
    <div className={styles.requirement}>
      {renderSections(convertToRequirement(props.detail))}
    </div>

    <div className={styles.description}>
      <sction>
        <h5>货品描述</h5>
        <GoodList goods={props.detail.goods}/>
      </sction>
    </div>

    <div className={styles.contact}>
      <Row>
        <Col span={8} className={styles.label}><p>联系方式：</p></Col>
        <Col span={16} className={styles.secondary}><p>{formatContact(props.detail)}</p></Col>
      </Row>
    </div>

  </div>
)

export default cssModules(OrderDetailPanel, styles, {allowMultiple: true});