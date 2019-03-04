import currencyFormatter from 'currency-formatter'
import {map, compact, pick, values, reduce} from 'lodash'
import React from 'react'
import { formatDateWithHourMinute } from 'utils/format'
import cssModules from 'react-css-modules'
import {LABELS_MAP, TRAVEL_TYPE} from './constants'
import styles from './index.module.scss'
import GoodList from './GoodList/index'


export const formatContact = (contact) => {
  if (!contact) return '---'
  const address = values(pick(contact, 'country', 'province', 'city', 'district', 'address')).join('');
  return (
    <div>
      <p>{contact.name}</p>
      <p>{address}</p>
      <p>{compact([contact.mobile, contact.phone]).join('/')}</p>
    </div>
  )
}

export const renderSection = (label, text) => {
  return (<section key={label} className={styles.item}>
    <span className={styles.label}>{label}：</span>
    {
      label === '陆运方式' ? <div className={styles.secondary}>
        {map(text, (value, key) => {
          return <p key={key}>{value}</p>
        })}
      </div> : <div className={styles.secondary}>{text}</div>

    }
  </section>)
}

const renderSections = (requirement) => {
  return map(requirement, (value, key) => {
    return renderSection(LABELS_MAP[key], value)
  })
}


const getTravelType = (shippingInfo) => {
  return compact(reduce(shippingInfo, (result, info, key) => {
    if (info.delivery) {
      result.push(TRAVEL_TYPE[key])
    }
    return result
  }, []))
}

const convertToRequirement = (detail) => {

  return {
    departure: detail.shippingInfo.departure.airportName,
    arrival: detail.shippingInfo.arrival.airportName,
    travelType: getTravelType(detail.shippingInfo),
    dueTo: formatDateWithHourMinute(detail.shippingInfo.estimatedDeliveryTime),
    budget: currencyFormatter.format(detail.price.total, {code: detail.currency})
  }
}

const renderTitle = (id, preview) => {
  if (preview) {
    return null
  }

  return (<div className={styles.title}>
    <h3>订单号：</h3>
    <span className={styles.secondary}>{id}</span>
  </div>)
}

const OrderDetailPanel = (props) => (
  <div className={styles.container}>
    {renderTitle(props.detail.id, props.preview)}

    <div className={styles.requirement}>
      {renderSections(convertToRequirement(props.detail))}
    </div>

    <div className={styles.description}>
      <sction>
        <h5>货品描述</h5>
        <GoodList goods={props.detail.goods}/>
      </sction>
    </div>
  </div>
)

export default cssModules(OrderDetailPanel, styles, {allowMultiple: true});